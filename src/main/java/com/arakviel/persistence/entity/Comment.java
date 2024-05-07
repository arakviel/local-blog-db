package com.arakviel.persistence.entity;

import com.arakviel.persistence.entity.proxy.contract.PostProxy;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record Comment(
    UUID id,
    String body,
    UUID userId,
    UserProxy user,
    UUID postId,
    PostProxy post,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
    implements Entity, Comparable<Comment> {

    @Override
    public int compareTo(Comment o) {
        return this.createdAt.compareTo(o.createdAt);
    }

    public User getUserLazy() {
        return user.get(id);
    }

    public Post getPostLazy() {
        return post.get(id);
    }
}
