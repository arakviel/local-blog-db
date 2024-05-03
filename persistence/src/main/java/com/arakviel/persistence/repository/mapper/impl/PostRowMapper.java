package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.proxy.Tags;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** TODO: make lazy loading for User and Post */
@Component
public class PostRowMapper implements RowMapper<Post> {

    private final UserRepository userRepository;
    private final Tags tags;

    public PostRowMapper(UserRepository userRepository, Tags tags) {
        this.userRepository = userRepository;
        this.tags = tags;
    }

    @Override
    public Post mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                STR."Не вдалось знайти користувача по id: \{
                                                        userId}"));
        return new Post(
                id,
                rs.getString("slug"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("body"),
                rs.getString("image"),
                rs.getBoolean("is_published"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                userId,
                user,
                tags);
    }
}
