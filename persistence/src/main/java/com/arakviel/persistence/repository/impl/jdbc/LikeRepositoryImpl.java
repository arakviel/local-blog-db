package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Like;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.LikeRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.LikeRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepositoryImpl extends GenericJdbcRepository<Like> implements LikeRepository {

    public LikeRepositoryImpl(ConnectionManager connectionManager, LikeRowMapper rowMapper) {
        super(connectionManager, rowMapper, TableNames.LIKES.getName());
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("post_id", "user_id", "created_at", "updated_at");
    }

    @Override
    protected List<Object> tableValues(Like like) {
        return List.of(like.postId(), like.userId(), like.createdAt(), like.updatedAt());
    }
}
