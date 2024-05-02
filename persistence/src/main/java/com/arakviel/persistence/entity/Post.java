package com.arakviel.persistence.entity;

import com.arakviel.persistence.entity.proxy.Tags;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record Post(
        UUID id,
        String slug,
        String title,
        String description,
        String body,
        String image,
        boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID userId,
        User user,
        Tags tags)
        implements Entity, Comparable<Post> {

    public Set<Tag> getTagsLazy() {
        return tags.get(id);
    }

    @Override
    public int compareTo(Post o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
