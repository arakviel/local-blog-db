package com.arakviel.persistence;

import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseInitializer.init();
        } finally {
            ConnectionManager.closePool();
        }
    }
}
