package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.filter.CommentFilterDto;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.CommentRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.CommentRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepositoryImpl extends GenericJdbcRepository<Comment>
    implements CommentRepository {

    public CommentRepositoryImpl(ConnectionManager connectionManager, CommentRowMapper rowMapper) {
        super(connectionManager, rowMapper, TableNames.COMMENTS.getName());
    }

    protected Map<String, Object> tableValues(Comment comment) {
        Map<String, Object> values = new LinkedHashMap<>();

        if (!comment.body().isBlank()) {
            values.put("body", comment.body());
        }
        if (Objects.nonNull(comment.userId())) {
            values.put("user_id", comment.userId());
        }
        if (Objects.nonNull(comment.postId())) {
            values.put("post_id", comment.postId());
        }
        if (Objects.nonNull(comment.createdAt())) {
            values.put("created_at", comment.createdAt());
        }
        if (Objects.nonNull(comment.updatedAt())) {
            values.put("updated_at", comment.updatedAt());
        }

        return values;
    }

    @Override
    public Set<Comment> findAllByPostId(UUID postId) {
        return findAllWhere(STR."post_id = \{postId}");
    }

    @Override
    public Set<Comment> findAllByUserId(UUID userId) {
        return findAllWhere(STR."user_id = \{userId}");
    }

    @Override
    public Set<Comment> findAll(int offset, int limit, String sortColumn, boolean ascending,
        CommentFilterDto commentFilterDto) {
        return findAll(offset, limit, sortColumn, ascending, commentFilterDto, "");
    }

    @Override
    public Set<Comment> findAllByPostId(UUID postId, int offset, int limit, String sortColumn,
        boolean ascending, CommentFilterDto commentFilterDto) {
        return findAll(offset, limit, sortColumn, ascending, commentFilterDto,
            STR."post_id = \{postId}");
    }

    @Override
    public Set<Comment> findAllByUserId(UUID userId, int offset, int limit, String sortColumn,
        boolean ascending, CommentFilterDto commentFilterDto) {
        return findAll(offset, limit, sortColumn, ascending, commentFilterDto,
            STR."user_id = \{userId}");
    }

    private Set<Comment> findAll(int offset, int limit, String sortColumn, boolean ascending,
        CommentFilterDto commentFilterDto, String wherePrefix) {
        StringBuilder where = new StringBuilder(STR."\{wherePrefix} ");
        HashMap<String, Object> filters = new HashMap<>();

        // Додавання фільтрів до where-умови
        if (!commentFilterDto.body().isBlank()) {
            filters.put("body", commentFilterDto.body());
        }
        if (Objects.nonNull(commentFilterDto.userId())) {
            filters.put("user_id", commentFilterDto.userId());
        }

        // Фільтр по created_at
        if (Objects.nonNull(commentFilterDto.createdAtStart())
            && Objects.nonNull(commentFilterDto.createdAtEnd())) {
            where.append(
                STR."AND created_at BETWEEN \{commentFilterDto.createdAtStart()} AND \{commentFilterDto.createdAtEnd()} ");
        } else if (Objects.nonNull(commentFilterDto.createdAtStart())) {
            where.append(STR."AND created_at >= \{commentFilterDto.createdAtStart()} ");
        } else if (Objects.nonNull(commentFilterDto.createdAtEnd())) {
            where.append(STR."AND created_at <= \{commentFilterDto.createdAtEnd()} ");
        }

        // Фільтр по updated_at
        if (Objects.nonNull(commentFilterDto.updatedAtStart())
            && Objects.nonNull(commentFilterDto.updatedAtEnd())) {
            where.append(
                STR."AND updated_at BETWEEN \{commentFilterDto.updatedAtStart()} AND \{commentFilterDto.updatedAtEnd()} ");
        } else if (Objects.nonNull(commentFilterDto.updatedAtStart())) {
            where.append(STR."AND updated_at >= \{commentFilterDto.updatedAtStart()} ");
        } else if (Objects.nonNull(commentFilterDto.updatedAtEnd())) {
            where.append(STR."AND updated_at <= \{commentFilterDto.updatedAtEnd()} ");
        }

        return findAll(offset, limit, sortColumn, ascending, filters, where.toString());
    }
}
