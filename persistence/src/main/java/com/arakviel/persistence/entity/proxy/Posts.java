package com.arakviel.persistence.entity.proxy;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface Posts {
    Set<Post> get(UUID postId);
}