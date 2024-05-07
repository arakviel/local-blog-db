package com.arakviel.persistence.entity.proxy.contract;

import com.arakviel.persistence.entity.Tag;
import java.util.Set;
import java.util.UUID;

@FunctionalInterface
public interface Tags {

    Set<Tag> get(UUID postId);
}
