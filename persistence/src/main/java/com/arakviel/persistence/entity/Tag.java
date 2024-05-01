package com.arakviel.persistence.entity;

import java.util.UUID;

public record Tag(UUID id, String name, String slug) implements Entity, Comparable<Tag> {

    @Override
    public int compareTo(Tag o) {
        return this.name.compareTo(o.name);
    }
}
