package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.PostRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.mapper.impl.PostRowMapper;
import com.arakviel.persistence.repository.mapper.impl.TagRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepositoryImpl extends GenericJdbcRepository<Post> implements PostRepository {

    private final ConnectionManager connectionManager;
    private final TagRowMapper tagRowMapper;
    private final JdbcManyToMany<Tag> jdbcManyToMany;

    public PostRepositoryImpl(
            ConnectionManager connectionManager,
            PostRowMapper rowMapper,
            TagRowMapper tagRowMapper,
            JdbcManyToMany<Tag> jdbcManyToMany) {
        super(connectionManager, rowMapper, TableNames.POSTS.getName());
        this.connectionManager = connectionManager;
        this.tagRowMapper = tagRowMapper;
        this.jdbcManyToMany = jdbcManyToMany;
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of(
                "title",
                "description",
                "body",
                "image",
                "is_published",
                "created_at",
                "updated_at",
                "user_id");
    }

    @Override
    protected List<Object> tableValues(Post post) {
        return List.of(
                post.title(),
                post.description(),
                post.body(),
                post.image(),
                post.isPublished(),
                post.createdAt(),
                post.updatedAt(),
                post.userId());
    }

    @Override
    public Set<Tag> getTags(UUID postId) {
        final String sql =
                """
                SELECT t.id,
                       t.name
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
