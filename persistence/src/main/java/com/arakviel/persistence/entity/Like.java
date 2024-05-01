package com.arakviel.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public record Like(
        UUID id,
        UUID postId,
        Post post,
        UUID userId,
        User user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
        implements Entity, Comparable<Like> {

    @Override
    public int compareTo(Like o) {
        return this.createdAt.compareTo(o.createdAt);
    }
}
