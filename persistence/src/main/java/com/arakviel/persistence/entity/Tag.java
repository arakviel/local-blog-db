package com.arakviel.persistence.entity;

import com.arakviel.persistence.entity.proxy.Posts;
import java.util.Set;
import java.util.UUID;

public record Tag(UUID id, String name, String slug, Posts posts) implements Entity, Comparable<Tag> {

    public Set<Post> getPostsLazy() {
        return posts.get(id);
    }

    @Override
    public int compareTo(Tag o) {
        return this.name.compareTo(o.name);
    }
}
