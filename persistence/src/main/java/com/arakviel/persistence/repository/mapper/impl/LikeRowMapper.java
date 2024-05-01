package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Like;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.PostRepository;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LikeRowMapper implements RowMapper<Like> {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeRowMapper(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Like mapRow(ResultSet rs) throws SQLException {
        UUID postId = UUID.fromString(rs.getString("post_id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        return new Like(
                UUID.fromString(rs.getString("id")),
                postId,
                postRepository
                        .findById(postId)
                        .orElseThrow(
                                () ->
                                        new EntityNotFoundException(
                                                STR."Не вдалось знайти пост по id: \{postId}")),
                userId,
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
