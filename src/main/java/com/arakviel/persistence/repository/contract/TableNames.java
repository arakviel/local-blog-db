package com.arakviel.persistence.repository.contract;

public enum TableNames {
    COMMENTS("comments"),
    LIKES("likes"),
    POSTS("posts"),
    TAGS("tags"),
    USERS("users");

    private final String name;

    TableNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
