package com.arakviel.persistence.entity.proxy.contract;

import com.arakviel.persistence.entity.Post;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface Posts {

    Set<Post> get(UUID postId);
}
