package com.arakviel.persistence.repository;

import static java.lang.StringTemplate.STR;

import com.arakviel.persistence.entity.Entity;
import com.arakviel.persistence.exception.EntityDeleteException;
import com.arakviel.persistence.exception.EntityInsertException;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.mapper.RowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class GenericJdbcRepository<T extends Entity> implements Repository<T> {

    private final ConnectionManager connectionManager;
    private final RowMapper<T> rowMapper;
    private final String tableName;

    public GenericJdbcRepository(
            ConnectionManager connectionManager, RowMapper<T> rowMapper, String tableName) {
        this.connectionManager = connectionManager;
        this.rowMapper = rowMapper;
        this.tableName = tableName;
    }

    // Переписати на аспекти spring Context
    @Override
    public Optional<T> findById(UUID id) {
        return findBy("id", id);
    }

    @Override
    public Optional<T> findBy(String column, Object value) {
        final String sql =
                STR."""
            SELECT *
              FROM \{
                        tableName}
             WHERE \{
                        column} = ?
        """;

        UUID id = (UUID) value;
        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();
            return Optional.ofNullable(rowMapper.mapRow(resultSet));
        } catch (SQLException throwables) {
            throw new EntityNotFoundException(
                    STR."Не вдалось знайти запис в базі даних за таким id: \{id.toString()}");
        }
    }

    @Override
    public Set<T> findAll() {
        final String sql = STR."""
            SELECT *
              FROM \{tableName}
        """;

        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Set<T> entities = new LinkedHashSet<>();
            while (resultSet.next()) {
                entities.add(rowMapper.mapRow(resultSet));
            }
            // LOGGER.info("found all users - [%s]".formatted(users));
            return entities;
        } catch (SQLException throwables) {
            throw new EntityNotFoundException(
                    STR."Помилка при отриманні всіх запитів з таблиці: \{tableName}");
        }
    }

    @Override
    public T save(final T entity) {
        List<Object> values = tableValues(entity);

        T newEntity;
        if (Objects.isNull(entity.id())) {
            UUID newId = UUID.randomUUID();
            values.addFirst(newId);
            newEntity = insert(values);
        } else {
            values.addLast(entity.id());
            newEntity = update(values);
        }

        return newEntity;
    }

    protected T insert(List<Object> values) {
        List<String> attributes = tableAttributes();
        String attributesString = "id, " + String.join(", ", attributes);
        String placeholders =
                Stream.generate(() -> "?")
                        .limit(attributes.size() + 1)
                        .collect(Collectors.joining(", "));
        String sql =
                STR."""
                INSERT INTO \{
                        tableName} (\{
                        attributesString})
                VALUES (\{
                        placeholders})
        """;

        if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
            values.add(LocalDateTime.now()); // created_at
            values.add(LocalDateTime.now()); // updated_at
        }

        return updateExecute(values, sql, "Помилка при додаванні нового запису в таблицю");
    }

    protected T update(List<Object> values) {
        List<String> attributes = tableAttributes();
        String attributesString =
                attributes.stream()
                        .filter(a -> !a.contains("created_at"))
                        .map(a -> STR."\{a} = ?")
                        .collect(Collectors.joining(", "));
        String sql =
                STR."""
              UPDATE \{
                        tableName}
                 SET \{
                        attributesString}
               WHERE id = ?
        """;

        if (attributes.stream().anyMatch(a -> a.equals("updated_at"))) {
            values.add(LocalDateTime.now()); // updated_at
        }

        return updateExecute(values, sql, "Помилка при оновленні існуючого запису в таблиці");
    }

    private T updateExecute(List<Object> values, String sql, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }

            statement.executeUpdate();

            return findById((UUID) values.getFirst())
                    .orElseThrow(
                            () ->
                                    new EntityNotFoundException(
                                            "Дивовижна помилка, яка ніколи не мала статись"));
        } catch (SQLException throwables) {
            throw new EntityInsertException(exceptionMessage);
        }
    }

    @Override
    public boolean delete(UUID id) {
        final String sql =
                STR."""
            DELETE FROM \{
                        tableName}
                  WHERE id = ?
        """;

        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id.toString());

            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(
                    STR."Помилка при видаленні запису з таблиці по id: \{id.toString()}");
        }
    }

    protected abstract List<String> tableAttributes();

    protected abstract List<Object> tableValues(T entity);
}
