package com.arakviel.domain;

import com.arakviel.persistence.PersistenceMain;
import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.entity.User;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        PersistenceContext persistenceContext = PersistenceMain.persistenceContext.getBean(PersistenceContext.class);

        Set<User> users = persistenceContext.users.repository.findAll();

        users.forEach(System.out::println);

        System.out.println("Hello world!");
    }
}