package com.arakviel.persistence.entity;

import com.arakviel.persistence.entity.proxy.Tags;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

// Ігнорюємо картину при порів.
public record Post(
        UUID id,
        String slug,
        String title,
        String description,
        String body,
        byte[] image,
        boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID userId,
        User user,
        Tags tags)
        implements Entity, Comparable<Post> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return isPublished == post.isPublished && Objects.equals(id, post.id)
               && Objects.equals(user, post.user) && Objects.equals(tags, post.tags)
               && Objects.equals(slug, post.slug) && Objects.equals(body, post.body)
               && Objects.equals(userId, post.userId) && Objects.equals(title,
            post.title) && Objects.equals(description, post.description)
               && Objects.equals(createdAt, post.createdAt) && Objects.equals(
            updatedAt, post.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug, title, description, body, isPublished, createdAt, updatedAt,
            userId, user, tags);
    }

    public Set<Tag> getTagsLazy() {
        return tags.get(id);
    }

    @Override
    public int compareTo(Post o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
