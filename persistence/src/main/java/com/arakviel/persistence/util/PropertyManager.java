package com.arakviel.persistence.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream applicationProperties =
                PropertyManager.class
                        .getClassLoader()
                        .getResourceAsStream("application.properties")) {
            PROPERTIES.load(applicationProperties);
        } catch (IOException e) {
            // LOGGER.error("failed to read properties. %s".formatted(e));
            throw new RuntimeException(e);
        }
    }

    private PropertyManager() {}
}
