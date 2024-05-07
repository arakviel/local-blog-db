package com.arakviel.persistence;

import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static AnnotationConfigApplicationContext persistenceContext;

    public static void main(String[] args) {
        String hexString = "0x89504E470D0A1A0A0000000D4948445200000030000000300806000000F9B78C0000000970485973000000EC400000EC40195F36F000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC400000EC40195F36F000000017352474200000000527443436F6C6F7253706163652E6465660000000049454E44AE426082";
        byte[] imageBytes = hexStringToByteArray(hexString);

        persistenceContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = persistenceContext.getBean(ConnectionManager.class);
        var databaseInitializer = persistenceContext.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            var persistenceContext = Main.persistenceContext.getBean(PersistenceContext.class);
            persistenceContext.users.registerNew(
                    new User(
                            null,
                            "arakviel",
                            "arakviel@gmail.com",
                            "password",
                        imageBytes,
                            LocalDate.of(1998, 2, 25),
                            Role.ADMIN));

            persistenceContext.users.registerNew(
                new User(
                    null,
                    "arakviel2",
                    "arakviel2@gmail.com",
                    "password2",
                    imageBytes,
                    LocalDate.of(1998, 2, 25),
                    Role.ADMIN));

            persistenceContext.users.commit();

            persistenceContext.users.registerModified(
                new User(UUID.fromString("018f39f9-1826-7cb9-9775-feee72794e6a"),
                    "mike_wilson1",
                    "mike.wilson@gmail.com",
                    "password5",
                    imageBytes,
                    LocalDate.of(1998, 2, 25),
                    Role.GENERAL)
            );

            persistenceContext.users.registerModified(
                new User(UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
                    "emily_brown2",
                    "emily.brown2@gmail.com",
                    "password5",
                    imageBytes,
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

    private static byte[] hexStringToByteArray(String hexString) {
        if (hexString.startsWith("0x")) {
            hexString = hexString.substring(2);
        }

        // Ensure the hex string length is even
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }

        int len = hexString.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                  + Character.digit(hexString.charAt(i + 1), 16));
        }

        return data;
    }
}
