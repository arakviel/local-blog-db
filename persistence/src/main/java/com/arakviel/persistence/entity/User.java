package com.arakviel.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;
import javax.management.relation.Role;

public record User(
        UUID id,
        String username,
        String email,
        String password,
        String avatar,
        LocalDate birthday,
        Role role)
        implements Entity, Comparable<User> {

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.username);
    }

    public enum Role {
        ADMIN("admin"),
        GENERAL("general");

        String name;

        Role(String name) {

            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
