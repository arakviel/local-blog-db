package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.proxy.contract.PostProxy;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.PostRepository;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CommentRowMapper implements RowMapper<Comment> {

    private final PostProxy postProxy;
    private final UserProxy userProxy;

    public CommentRowMapper(PostProxy postProxy, UserProxy userProxy) {
        this.postProxy = postProxy;
        this.userProxy = userProxy;
    }

    @Override
    public Comment mapRow(ResultSet rs) throws SQLException {
        UUID userId = UUID.fromString(rs.getString("user_id"));
        UUID postId = UUID.fromString(rs.getString("post_id"));
        return new Comment(
            UUID.fromString(rs.getString("id")),
            rs.getString("body"),
            userId,
            userProxy,
            postId,
            postProxy,
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime());
    }
}
