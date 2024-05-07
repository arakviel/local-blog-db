package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.proxy.contract.Tags;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * TODO: make lazy loading for User and Post
 */
@Component
public class PostRowMapper implements RowMapper<Post> {

    private final Tags tags;
    private final UserProxy userProxy;

    public PostRowMapper(Tags tags, UserProxy userProxy) {
        this.tags = tags;
        this.userProxy = userProxy;
    }

    @Override
    public Post mapRow(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        return new Post(
            id,
            rs.getString("slug"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("body"),
            rs.getBytes("image"),
            rs.getBoolean("is_published"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime(),
            userId,
            userProxy,
            tags);
    }
}
