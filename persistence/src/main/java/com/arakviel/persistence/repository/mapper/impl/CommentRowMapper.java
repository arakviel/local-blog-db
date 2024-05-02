package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CommentRowMapper implements RowMapper<Comment> {

    private final UserRepository userRepository;

    public CommentRowMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Comment mapRow(ResultSet rs) throws SQLException {
        UUID userId = UUID.fromString(rs.getString("user_id"));
        return new Comment(
                UUID.fromString(rs.getString("id")),
                rs.getString("body"),
                userId,
                //null
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                STR."Не вдалось знайти користувача по id: \{
                                                        userId}")),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime());
    }
}
