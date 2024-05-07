package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Like;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.LikeRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.LikeRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryImpl extends GenericJdbcRepository<Like> implements LikeRepository {

    public LikeRepositoryImpl(ConnectionManager connectionManager, LikeRowMapper rowMapper) {
        super(connectionManager, rowMapper, TableNames.LIKES.getName());
    }

    protected Map<String, Object> tableValues(Like like) {
        Map<String, Object> values = new LinkedHashMap<>();

        if (Objects.nonNull(like.userId())) {
            values.put("user_id", like.userId());
        }
        if (Objects.nonNull(like.postId())) {
            values.put("post_id", like.postId());
        }
        if (Objects.nonNull(like.createdAt())) {
            values.put("created_at", like.createdAt());
        }
        if (Objects.nonNull(like.updatedAt())) {
            values.put("updated_at", like.updatedAt());
        }

        return values;
    }
}
