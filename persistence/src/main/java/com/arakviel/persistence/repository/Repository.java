package com.arakviel.persistence.repository;

import com.arakviel.persistence.entity.Entity;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Repository<T extends Entity> {

    Optional<T> findById(UUID id);

    Optional<T> findBy(String column, Object value);

    Set<T> findAll();

    T save(T entity);

    boolean delete(UUID id);
}
