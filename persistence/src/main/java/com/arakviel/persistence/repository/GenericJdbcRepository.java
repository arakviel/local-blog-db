package com.arakviel.persistence.repository;

import static java.lang.StringTemplate.STR;

import com.arakviel.persistence.entity.Entity;
import com.arakviel.persistence.exception.EntityDeleteException;
import com.arakviel.persistence.exception.EntityUpdateException;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.mapper.RowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
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
            statement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.ofNullable(rowMapper.mapRow(resultSet));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<T> findAllWhere(String whereQuery) {
        final String sql = STR."""
            SELECT *
              FROM \{tableName}
             WHERE \{whereQuery}
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
                      UPDATE \{tableName}
                         SET \{attributesString}
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
                if(values.get(i) instanceof Enum) {
                    statement.setString(i + 1, values.get(i).toString());
                } else  {
                    statement.setObject(i + 1, values.get(i), Types.OTHER);
                }
            }

            statement.executeUpdate();

            UUID id = (UUID) values.stream()
                .filter(UUID.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UUID not found"));

            return findById(id).orElseThrow();
        } catch (SQLException | NoSuchElementException e) {
            throw new EntityUpdateException(exceptionMessage);
        }
    }

    @Override
    public Set<T> save(Collection<T> entities) {
        Set<T> results;
        List<List<Object>> listOfValues = new ArrayList<>(new ArrayList<>());

        for(var entity: entities) {
            List<Object> values = tableValues(entity);
            if (Objects.isNull(entity.id())) {
                values.addFirst(UUID.randomUUID());
            } else {
                values.addLast(entity.id());
            }
            listOfValues.add(values);
        }

        if(entities.stream().allMatch(e -> Objects.isNull(e.id()))) {
            results = batchInsert(listOfValues);
        } else {
            results = batchUpdate(listOfValues);
        }
        return results;
    }
    protected Set<T> batchInsert(List<List<Object>> listOfValues) {
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
            listOfValues.forEach(values -> {
                values.add(LocalDateTime.now()); // created_at
                values.add(LocalDateTime.now()); // updated_at
            });
        }

        return batchExecute(listOfValues, sql, "Помилка при додаванні нового запису в таблицю");
    }
    protected Set<T> batchUpdate(List<List<Object>> listOfValues) {
        List<String> attributes = tableAttributes();
        String attributesString =
            attributes.stream()
                .filter(a -> !a.contains("created_at"))
                .map(a -> STR."\{a} = ?")
                .collect(Collectors.joining(", "));
        String sql =
            STR."""
                      UPDATE \{tableName}
                         SET \{attributesString}
                       WHERE id = ?
                    """;

        if (attributes.stream().anyMatch(a -> a.equals("updated_at"))) {
            listOfValues.forEach(values -> {
                values.add(LocalDateTime.now()); // updated_at
            });
        }

        return batchExecute(listOfValues, sql, "Помилка при оновленні існуючого запису в таблиці");
    }
    private Set<T> batchExecute(List<List<Object>> listOfValues, String sql, String exceptionMessage) {
        Set<T> results = new LinkedHashSet<>();
        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for(var values: listOfValues) {
                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i), Types.OTHER);
                }
                statement.addBatch();
            }

            statement.executeBatch();

            // тут ми додаєм результат додавання
            results = getEntitiesAfterBatchExecute(listOfValues);
        } catch (SQLException throwables) {
            throw new EntityUpdateException(exceptionMessage);
        }
        return results;
    }
    private Set<T> getEntitiesAfterBatchExecute(List<List<Object>> listOfValues) {
        Set<T> results;
        List<String> ids = listOfValues.stream().map(values -> {
            UUID id = (UUID) values.stream()
                .filter(UUID.class::isInstance)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UUID not found"));

            return STR."'\{id.toString()}'";
        }).toList();

        results = findAllWhere(STR."id IN(\{String.join(", ", ids)})");
        return results;
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
            statement.setObject(1, id, Types.OTHER);

            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(
                    STR."Помилка при видаленні запису з таблиці по id: \{id.toString()}");
        }
    }
    public boolean delete(Collection<UUID> ids) {
        final String sql =
            STR."""
                    DELETE FROM \{tableName}
                          WHERE id = ?
                """;

        try (Connection connection = connectionManager.get();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            for(var id: ids) {
                statement.setObject(1, id, Types.OTHER);
                statement.addBatch();
            }
            statement.executeBatch();

            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(
                STR."Помилка при видаленні записів з таблиці по ids: \{ids.toString()}");
        }
    }

    protected abstract List<String> tableAttributes();

    protected abstract List<Object> tableValues(T entity);
}
