package com.arakviel.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
    private static byte[] imageBytes;

    @BeforeAll
    static void setup() {
        PropertyManager propertyManager = new PropertyManager(
            UserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("application.properties")
        );
        connectionManager = new ConnectionManager(propertyManager);
        userRepository = new UserRepositoryImpl(connectionManager, new UserRowMapper());

        String hexString = "0x089504E470D0A1A0A0000000D4948445200000030000000300806000000F9B78C0000000970485973000000EC400000EC40195F36F000000017352474200AECE1CE90000000467414D410000B18F0BFC6105000000097048597300000EC400000EC40195F36F000000017352474200000000527443436F6C6F7253706163652E6465660000000049454E44AE426082";

        imageBytes = hexStringToByteArray(hexString);
    }

    @BeforeEach
    void init() throws SQLException {
        FakeDatabaseInitializer.run(connectionManager.get());
    }

    @Test
    void findById_whenUserExists_thenReturnsUser() {
        // Given
        UUID userId = UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061");
        User expectedUser = new User(userId,
            "john_doe",
            "john.doe@example.com",
            "password1",
            imageBytes,
            LocalDate.of(1990, 5, 15),
            Role.ADMIN);

        // When
        Optional<User> actualOptionalUser = userRepository.findById(userId);

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findById_whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        Optional<User> actualOptionalUser = userRepository.findById(id);

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the user with this id does not exist");
    }

    @Test
    void findBy_whenUserExists_thenReturnsUser() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findBy("birthday", LocalDate.of(1992, 3, 28));

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findBy_whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findBy("birthday", LocalDate.of(1993, 4, 20));

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the user with this birthday does not exist");
    }

    @Test
    void findByUsername_whenUserExists_thenReturnsUser() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findByUsername("emily_brown");

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findByUsername_whenUserDoesNotExist_thenReturnsEmptyOptional() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findByUsername("the_emily_brown");

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the user with this username does not exist");
    }

    @Test
    void findByEmail_whenUserExists_thenReturnsUser() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findByEmail("emily.brown@example.com");

        // Then
        assertTrue(actualOptionalUser.isPresent(), "The found User object is not null");
        assertEquals(expectedUser, actualOptionalUser.get(), "The searched object is equal to the found one");
    }

    @Test
    void findByEmail_whenDoesNotExist_thenEmptyOptional() {
        // Given
        User expectedUser = new User(
            UUID.fromString("018f39f9-05de-704c-bdc4-9fb5e1432e19"),
            "emily_brown",
            "emily.brown@example.com",
            "password4",
            imageBytes,
            LocalDate.of(1992, 3, 28),
            Role.GENERAL);

        // When
        Optional<User> actualOptionalUser = userRepository.findByEmail("the_emily.brown@example.com");

        // Then
        assertTrue(actualOptionalUser.isEmpty(), "Empty optional if the user with this email does not exist");
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
    @Tag("slow")
    void findAll_WithPaginationAndFiltersAndDescSorting_thenReturnsSetOfUser() {
        // Given
        int usersFirstPageSize = 3;
        int usersSecondPageSize = 2;
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", "@example.com");

        // When
        Set<User> firstPageOfUsers = userRepository.findAll(0, 3, "username", false, filters);
        Set<User> secondPageOfUsers = userRepository.findAll(3, 3, "username", false, filters);

        // Then
        assertNotNull(firstPageOfUsers);
        assertNotNull(secondPageOfUsers);
        assertEquals(usersFirstPageSize, firstPageOfUsers.size());
        assertEquals(usersSecondPageSize, secondPageOfUsers.size());
    }

    @Test
    @Tag("slow")
    void count_thenReturnsCountOfRows() {
        // Given
        int usersSize = 5;

        // When
        long count = userRepository.count();

        // Then
        assertNotEquals(count, 0);
        assertEquals(usersSize, count);
    }

    @Test
    void save_whenInsertNewUser_thenReturnsUserWithGeneratedId() {
        // Given
        User expectedUser = new User(
            null,
            "emma_jones",
            "emma.jones@example.com",
            "emma1234",
            imageBytes,
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
    void save_whenInsertCollectionOfUsers_thenReturnsCollectionOfUsersWithGeneratedId() {
        // Given
        Set<User> users = new LinkedHashSet<>(3);
        users.add(new User(
            null,
            "emma_jones",
            "emma.jones@example.com",
            "emma1234",
            imageBytes,
            LocalDate.of(1995, 3, 8),
            Role.GENERAL
        ));
        users.add(new User(
            null,
            "sarah_smith",
            "sarah.smith@example.com",
            "sarah1234",
            imageBytes,
            LocalDate.of(1988, 11, 24),
            Role.GENERAL
        ));
        users.add(new User(
            null,
            "michael_davis",
            "michael.davis@example.com",
            "michael1234",
            imageBytes,
            LocalDate.of(1983, 6, 20),
            Role.ADMIN
        ));

        // When
        Set<User> expectedUsers = userRepository.save(users);
        List<String> ids = expectedUsers.stream().map(User::id).map(id -> STR."'\{id}'").toList();
        Set<User> actualUsers = userRepository.findAllWhere(STR."id IN(\{String.join(", ", ids)})");

        // Then
        assertNotNull(ids);
        assertEquals(users.size(), expectedUsers.size());
        assertEquals(expectedUsers, actualUsers);
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
            imageBytes,
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
            imageBytes,
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

    @Test
    void save_whenUpdateCollectionOfUsers_thenReturnsCollectionOfUsers() {
        // Given
        Set<User> users = new LinkedHashSet<>(3);
        users.add(new User(
            UUID.fromString("018f39f8-6850-75d3-b6b1-be865de4d061"),
            "the_john_doe",
            "the.john.doe@example.com",
            "the_password1",
            imageBytes,
            LocalDate.of(1990, 5, 16),
            Role.ADMIN
        ));
        users.add(new User(
            UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5"),
            "the_jane_smith",
            "the_jane.smith@example.com",
            "the_password2",
            imageBytes,
            LocalDate.of(1985, 9, 11),
            Role.GENERAL
        ));
        users.add(new User(
            UUID.fromString("018f39f8-ea26-7c8d-945b-85c7e9f7cb4c"),
            "the_alex_jones",
            "the_alex.jones@example.com",
            "the_password3",
            imageBytes,
            LocalDate.of(1988, 12, 10),
            Role.ADMIN
        ));
        long expectedCount = userRepository.count();

        // When
        Set<User> expectedUsers = userRepository.save(users);
        List<String> ids = expectedUsers.stream().map(User::id).map(id -> STR."'\{id}'").toList();
        Set<User> actualUsers = userRepository.findAllWhere(STR."id IN(\{String.join(", ", ids)})");
        long actualCount = userRepository.count();

        // Then
        assertNotNull(expectedUsers);
        assertEquals(users.size(), expectedUsers.size());
        assertEquals(expectedCount, actualCount);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void save_whenUpdateNotExistCollectionOfUsers_thenThrowEntityUpdateException() {
        // Given
        UUID userId = UUID.randomUUID();
        Set<User> users = new LinkedHashSet<>(3);
        users.add(new User(
            userId,
            "the_john_doe",
            "the.john.doe@example.com",
            "the_password1",
            imageBytes,
            LocalDate.of(1990, 5, 16),
            Role.ADMIN
        ));
        users.add(new User(
            UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5"),
            "the_jane_smith",
            "the_jane.smith@example.com",
            "the_password2",
            imageBytes,
            LocalDate.of(1985, 9, 11),
            Role.GENERAL
        ));
        users.add(new User(
            UUID.fromString("018f39f8-ea26-7c8d-945b-85c7e9f7cb4c"),
            "the_alex_jones",
            "the_alex.jones@example.com",
            "the_password3",
            imageBytes,
            LocalDate.of(1988, 12, 10),
            Role.ADMIN
        ));

        // When
        Executable executable = () -> {
            userRepository.save(users);
            var optionalUser = userRepository.findById(userId);
        };

        // Then
        assertThrows(EntityUpdateException.class, executable);
    }

    @Test
    void delete_whenUserExists_ThenReturnsTrue() {
        // Given
        UUID id = UUID.fromString("018f39f8-8697-7a1a-93ab-5d42d7b42dd5");
        boolean expected = true;

        // When
        boolean actual = userRepository.delete(id);

        // Then
        assertEquals(expected, actual, "True if the user was deleted");
    }

    @Test
    void delete_whenUserDoesNotExist_thenReturnsFalse() {
        // Given
        UUID id = UUID.randomUUID();
        boolean expected = false;

        // When
        boolean actual = userRepository.delete(id);

        // Then
        assertEquals(expected, actual, "False if the user with this id does not exist");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connectionManager.closePool();
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
