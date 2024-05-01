package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.contract.TagRepository;
import com.arakviel.persistence.repository.mapper.impl.PostRowMapper;
import com.arakviel.persistence.repository.mapper.impl.TagRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepositoryImpl extends GenericJdbcRepository<Tag> implements TagRepository {
    private final PostRowMapper postRowMapper;
    private final JdbcManyToMany<Post> jdbcManyToMany;

    public TagRepositoryImpl(
            ConnectionManager connectionManager,
            TagRowMapper rowMapper,
            PostRowMapper postRowMapper,
            JdbcManyToMany<Post> jdbcManyToMany) {
        super(connectionManager, rowMapper, TableNames.TAGS.getName());
        this.postRowMapper = postRowMapper;
        this.jdbcManyToMany = jdbcManyToMany;
    }

    @Override
    protected List<String> tableAttributes() {
        return List.of("name", "slug");
    }

    @Override
    protected List<Object> tableValues(Tag tag) {
        return List.of(tag.name(), tag.slug());
    }

    @Override
    public Set<Post> getPosts(UUID tagId) {
        final String sql =
                """
                SELECT p.id,
                       p.user_id,
                       p.title,
                       p.body,
                       p.created_at,
                       p.updated_at
                  FROM posts AS p
                       JOIN post_tag AS pt
                         ON p.id = pt.post_id
                 WHERE pt.tag_id = ?;
                """;

        return jdbcManyToMany.getByPivot(
                tagId,
                sql,
                postRowMapper,
                STR."Помилка при отриманні всіх постів тега по id: \{tagId}");
    }

    @Override
    public boolean attach(UUID postId, UUID tagId) {
        final String sql =
                """
                INSERT INTO post_tag(post_id, tag_id)
                VALUES (?, ?);
                """;

        return jdbcManyToMany.executeUpdate(
                postId, tagId, sql, STR."Помилка при додаванні нового поста до тегу");
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
                STR."Помилка при видаленні запису з таблиці по postId: \{
                        postId.toString()} і tagId: \{
                        tagId.toString()}");
    }
}