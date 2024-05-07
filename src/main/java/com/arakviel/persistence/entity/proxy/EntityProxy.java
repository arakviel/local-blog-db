package com.arakviel.persistence.entity.proxy;

import java.util.UUID;

@FunctionalInterface
public interface EntityProxy<T> {

    T get(UUID entityId);
}
