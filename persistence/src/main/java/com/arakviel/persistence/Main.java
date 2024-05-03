package com.arakviel.persistence;

import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        var connectionManager = context.getBean(ConnectionManager.class);
        var databaseInitializer = context.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            var persistenceContext = context.getBean(PersistenceContext.class);
            persistenceContext.users.registerNew(
                    new User(
                            null,
                            "arakviel",
                            "arakviel@gmail.com",
                            "password",
                            "avatar.jpg",
                            LocalDate.of(1998, 2, 25),
                            Role.ADMIN));

            persistenceContext.users.registerNew(
                new User(
                    null,
                    "arakviel2",
                    "arakviel2@gmail.com",
                    "password2",
                    "avatar2.jpg",
                    LocalDate.of(1998, 2, 25),
                    Role.ADMIN));

            persistenceContext.users.commit();

            persistenceContext.users.registerModified(
                new User(UUID.fromString("018f39f9-1826-7cb9-9775-feee72794e6a"),
                    "mike_wilson1",
                    "mike.wilson@gmail.com",
                    "password5",
                    "avatar5.jpg",
                    LocalDate.of(1998, 2, 25),
                    Role.GENERAL)
            );

            persistenceContext.users.registerModified(
                new User(UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
                    "emily_brown2",
                    "emily.brown2@gmail.com",
                    "password5",
                    "avatar5.jpg",
                    LocalDate.of(1998, 2, 25),
                    Role.GENERAL)
            );

            persistenceContext.users.commit();

            persistenceContext.users.registerDeleted(
                UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061"));
            persistenceContext.users.registerDeleted(
                UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5"));
            persistenceContext.users.commit();
        } finally {
            connectionManager.closePool();
        }
    }
}
