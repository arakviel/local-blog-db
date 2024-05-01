package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs) throws SQLException {
        return new User(
                UUID.fromString(rs.getString("id")),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getObject("avatar", String.class),
                rs.getTimestamp("birthday").toLocalDateTime().toLocalDate(),
                User.Role.valueOf(rs.getString("role")));
    }
}
