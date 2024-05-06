package com.arakviel.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// ігноруємо аватарки
public record User(
        UUID id,
        String username,
        String email,
        String password,
        byte[] avatar,
        LocalDate birthday,
        Role role)
        implements Entity, Comparable<User> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && role == user.role && Objects.equals(
            email, user.email) && Objects.equals(username, user.username)
               && Objects.equals(password, user.password) && Objects.equals(
            birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, birthday, role);
    }

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
