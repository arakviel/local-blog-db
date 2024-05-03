package com.arakviel.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.exception.EntityUpdateException;
import com.arakviel.persistence.init.FakeDatabaseInitializer;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.impl.jdbc.UserRepositoryImpl;
import com.arakviel.persistence.repository.mapper.impl.UserRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.PropertyManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

// TODO: removes, batch tests
class UserRepositoryTest {
    private static ConnectionManager connectionManager;
    private static UserRepository userRepository;

    @BeforeAll
    static void setup() {
        PropertyManager propertyManager = new PropertyManager(
            UserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("application.properties")
        );
        connectionManager = new ConnectionManager(propertyManager);
        userRepository = new UserRepositoryImpl(connectionManager, new UserRowMapper());
    }

    @BeforeEach
    void init() throws SQLException {
        FakeDatabaseInitializer.run(connectionManager.get());
    }

    @Test
    void findOneById_whenUserExists_thenReturnsUser() {
        // Given
        UUID userId = UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061");
        User expectedUser = new User(userId,
            "john_doe",
            "john.doe@example.com",
            "password1",
            "avatar1.jpg",
            LocalDate.of(1990, 5, 15),
            Role.ADMIN);

        // When
        Optional<User> actualOptionalUser = userRepository.findById(userId);

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findOneById_whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        Optional<User> actualOptionalUser = userRepository.findById(id);

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the address with this id does not exist");
    }

    @Test
    @Tag("slow")
    void findAll_thenReturnsSetOfUser() {
        // Given
        int usersSize = 5;

        // When
        Set<User> users = userRepository.findAll();

        // Then
        assertNotNull(users);
        assertEquals(usersSize, users.size());
    }

    @Test
    void save_whenInsertNewUser_thenReturnsAddressEntityWithGeneratedId() {
        // Given
        User expectedUser = new User(
            null,
            "emma_jones",
            "emma.jones@example.com",
            "emma1234",
            "avatar3.jpg",
            LocalDate.of(1995, 3, 8),
            Role.GENERAL
        );

        // When
        User actualUser = userRepository.save(expectedUser);
        UUID id = actualUser.id();
        Optional<User> optionalFoundedUser = userRepository.findById(id);

        // Then
        assertNotNull(id);
        assertTrue(optionalFoundedUser.isPresent());
        assertEquals(actualUser, optionalFoundedUser.orElse(null));
    }

    @Test
    void save_whenUpdateExistUser_thenReturnsUser() {
        // Given
        UUID userId = UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5");
        User expectedUser = new User(
            userId,
            "jane_brown",
            "jane.brown@example.com",
            "P@ssword2",
            "avatar2.jpg",
            LocalDate.now(),
            Role.GENERAL
        );

        // When
        userRepository.save(expectedUser);
        var optionalUser = userRepository.findById(userId);

        // Then
        assertEquals(expectedUser, optionalUser.orElse(null));
    }

    @Test
    void save_whenUpdateNotExistUser_thenThrowEntityUpdateException() {
        // Given
        UUID userId = UUID.randomUUID();
        User expectedUser = new User(
            userId,
            "jane_brown",
            "jane.brown@example.com",
            "P@ssword2",
            "avatar2.jpg",
            LocalDate.now(),
            Role.GENERAL
        );

        // When
        Executable executable = () -> {
            userRepository.save(expectedUser);
            var optionalUser = userRepository.findById(userId);
        };

        // Then
        assertThrows(EntityUpdateException.class, executable);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connectionManager.closePool();
    }
}
