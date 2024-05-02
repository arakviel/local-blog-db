package com.arakviel.persistence.entity.proxy;

import com.arakviel.persistence.entity.Tag;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface Tags {
    Set<Tag> get(UUID postId);
}
