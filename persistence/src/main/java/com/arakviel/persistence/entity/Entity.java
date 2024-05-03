package com.arakviel.persistence.entity;

import java.util.UUID;

@FunctionalInterface
public interface Entity {
    UUID id();
}
