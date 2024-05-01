package com.arakviel.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record Comment(
        UUID id,
        String body,
        UUID userId,
        User user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
        implements Entity, Comparable<Comment> {

    @Override
    public int compareTo(Comment o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
