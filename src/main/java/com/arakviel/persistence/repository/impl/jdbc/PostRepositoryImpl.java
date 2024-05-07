package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.PostRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.PostRowMapper;
import com.arakviel.persistence.repository.mapper.impl.TagRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl extends GenericJdbcRepository<Post> implements PostRepository {

    private final TagRowMapper tagRowMapper;
    private final JdbcManyToMany<Tag> jdbcManyToMany;

    public PostRepositoryImpl(
        ConnectionManager connectionManager,
        PostRowMapper postRowMapper,
        TagRowMapper tagRowMapper,
        JdbcManyToMany<Tag> jdbcManyToMany) {
        super(connectionManager, postRowMapper, TableNames.POSTS.getName());
        this.tagRowMapper = tagRowMapper;
        this.jdbcManyToMany = jdbcManyToMany;
    }

    protected Map<String, Object> tableValues(Post post) {
        Map<String, Object> values = new LinkedHashMap<>();

        if (post.slug().isBlank()) {
            values.put("slug", post.slug());
        }
        if (post.title().isBlank()) {
            values.put("title", post.title());
        }
        if (post.description().isBlank()) {
            values.put("description", post.description());
        }
        if (post.body().isBlank()) {
            values.put("body", post.body());
        }
        if (Objects.nonNull(post.image())) {
            values.put("image", post.image());
        }
        values.put("is_published", post.isPublished());
        if (Objects.nonNull(post.createdAt())) {
            values.put("created_at", post.createdAt());
        }
        if (Objects.nonNull(post.updatedAt())) {
            values.put("updated_at", post.updatedAt());
        }
        if (Objects.nonNull(post.userId())) {
            values.put("user_id", post.userId());
        }

        return values;
    }

    @Override
    public Set<Tag> findAllTags(UUID postId) {
        final String sql =
            """
                SELECT t.id,
                       t.name,
                       t.slug
                  FROM tags AS t
                       JOIN post_tag AS pt
                         ON t.id = pt.tag_id
                 WHERE pt.post_id = ?;
                """;

        return jdbcManyToMany.getByPivot(
            postId,
            sql,
            tagRowMapper,
            STR."Помилка при отриманні всіх тегів поста по id: \{postId}");
    }

    @Override
    public boolean attach(UUID postId, UUID tagId) {
        final String sql =
            """
                INSERT INTO post_tag(post_id, tag_id)
                VALUES (?, ?);
                """;
        return jdbcManyToMany.executeUpdate(
            postId, tagId, sql, STR."Помилка при додаванні нового тегу до поста");
    }

    @Override
    public boolean detach(UUID postId, UUID tagId) {
        final String sql =
            """
                DELETE FROM post_tag
                      WHERE post_id = ? AND tag_id = ?;
                """;
        return jdbcManyToMany.executeUpdate(
            postId,
            tagId,
            sql,
            STR."Помилка при видаленні запису з таблиці по postId: \\{postId.toString()} і tagId: \\{tagId.toString()}");
    }
}
