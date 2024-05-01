package com.arakviel.persistence.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DatabaseInitializer {
    public static void init() {
        try (Connection connection = ConnectionManager.get();
                Statement statementForDDL = connection.createStatement();
                Statement statementForDML = connection.createStatement()) {
            statementForDDL.execute(getSQL("ddl.sql"));
            statementForDML.execute(getSQL("dml.sql"));
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private static String getSQL(final String resourceName) {
        return new BufferedReader(
                        new InputStreamReader(
                                Objects.requireNonNull(
                                        ConnectionManager.class
                                                .getClassLoader()
                                                .getResourceAsStream(resourceName))))
                .lines()
                .collect(Collectors.joining("\n"));
    }
}
