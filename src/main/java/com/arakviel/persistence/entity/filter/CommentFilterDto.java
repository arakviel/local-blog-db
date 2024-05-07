package com.arakviel.persistence.entity.filter;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentFilterDto(
    String body,
    UUID userId,
    UUID postId,
    LocalDateTime createdAtStart,
    LocalDateTime createdAtEnd,
    LocalDateTime updatedAtStart,
    LocalDateTime updatedAtEnd
) {

}
