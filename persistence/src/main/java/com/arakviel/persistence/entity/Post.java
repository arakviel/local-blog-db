package com.arakviel.persistence.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record Post(
        UUID id,
        String title,
        String description,
        String body,
        String image,
        boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID userId,
        User user,
        Set<Tag> tags)
        implements Entity, Comparable<Post> {

    @Override
    public int compareTo(Post o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
