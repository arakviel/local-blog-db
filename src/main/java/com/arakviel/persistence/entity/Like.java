package com.arakviel.persistence.entity;

import com.arakviel.persistence.entity.proxy.contract.PostProxy;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import java.time.LocalDateTime;
import java.util.UUID;

public record Like(
    UUID id,
    UUID postId,
    PostProxy post,
    UUID userId,
    UserProxy user,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
    implements Entity, Comparable<Like> {

    @Override
    public int compareTo(Like o) {
        return this.createdAt.compareTo(o.createdAt);
    }

    public User getUserLazy() {
        return user.get(id);
    }

    public Post getPostLazy() {
        return post.get(id);
    }
}
