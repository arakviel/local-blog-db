package com.arakviel.persistence.repository.mapper.impl;

import com.arakviel.persistence.entity.Like;
import com.arakviel.persistence.entity.proxy.contract.PostProxy;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import com.arakviel.persistence.repository.mapper.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LikeRowMapper implements RowMapper<Like> {


    private final PostProxy postProxy;
    private final UserProxy userProxy;

    public LikeRowMapper(PostProxy postProxy, UserProxy userProxy) {
        this.postProxy = postProxy;
        this.userProxy = userProxy;
    }

    @Override
    public Like mapRow(ResultSet rs) throws SQLException {
        UUID postId = UUID.fromString(rs.getString("post_id"));
        UUID userId = UUID.fromString(rs.getString("user_id"));
        return new Like(
            UUID.fromString(rs.getString("id")),
            postId,
            postProxy,
            userId,
            userProxy,
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime());
    }
}
