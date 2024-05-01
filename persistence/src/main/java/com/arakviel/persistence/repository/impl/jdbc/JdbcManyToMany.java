package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.exception.EntityDeleteException;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.mapper.RowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JdbcManyToMany<T> {
    private final ConnectionManager connectionManager;

    public JdbcManyToMany(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected Set<T> getByPivot(
            UUID entityId, String sql, RowMapper<T> rowMapper, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entityId.toString());
            ResultSet resultSet = statement.executeQuery();

            Set<T> entities = new LinkedHashSet<>();
            while (resultSet.next()) {
                entities.add(rowMapper.mapRow(resultSet));
            }
            return entities;
        } catch (SQLException throwables) {
            throw new EntityNotFoundException(exceptionMessage);
        }
    }

    protected boolean executeUpdate(
            UUID firstId, UUID secondId, String sql, String exceptionMessage) {
        try (Connection connection = connectionManager.get();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstId.toString());
            statement.setString(2, secondId.toString());
            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new EntityDeleteException(exceptionMessage);
        }
    }
}
