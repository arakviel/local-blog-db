package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.entity.proxy.contract.Posts;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    private final Posts posts;

    public TagRowMapper(Posts posts) {
        this.posts = posts;
    }

    @Override
    public Tag mapRow(ResultSet rs) throws SQLException {
        return new Tag(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("slug"),
            posts);
    }
}
