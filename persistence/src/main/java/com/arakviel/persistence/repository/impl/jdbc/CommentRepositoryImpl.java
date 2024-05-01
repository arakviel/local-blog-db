package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.CommentRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.CommentRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericJdbcRepository<Comment>
        implements CommentRepository {

    public CommentRepositoryImpl(ConnectionManager connectionManager, CommentRowMapper rowMapper) {
        super(connectionManager, rowMapper, TableNames.COMMENTS.getName());
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("body", "user_id", "created_at", "updated_at");
    }

    @Override
    protected List<Object> tableValues(Comment comment) {
        return List.of(comment.body(), comment.userId(), comment.createdAt(), comment.updatedAt());
    }
}
