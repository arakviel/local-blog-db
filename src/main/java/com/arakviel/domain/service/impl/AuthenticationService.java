package com.arakviel.domain.service.impl;

import com.arakviel.domain.exception.AuthenticationException;
import com.arakviel.domain.exception.UserAlreadyAuthenticatedException;
import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.password4j.Password;

public class AuthenticationService {

    private final UserRepository userRepository;
    private User user;

    AuthenticationService(PersistenceContext persistenceContext) {
        this.userRepository = persistenceContext.users.repository;
    }

    public boolean authenticate(String username, String password) {
        // Перевіряємо, чи вже існує аутентифікований користувач
        if (user != null) {
            throw new UserAlreadyAuthenticatedException(
                STR."Ви вже авторизувалися як: \{user.username()}");
        }

        User foundedUser = userRepository.findByUsername(username)
            .orElseThrow(AuthenticationException::new);

        if (!Password.check(password, foundedUser.password()).withBcrypt()) {
            return false;
        }

        user = foundedUser;
        return true;
    }

    public boolean isAuthenticated() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void logout() {
        if (user == null) {
            throw new UserAlreadyAuthenticatedException("Ви ще не автентифікавані.");
        }
        user = null;
    }

}
