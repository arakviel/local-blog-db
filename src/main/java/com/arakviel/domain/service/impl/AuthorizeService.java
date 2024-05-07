package com.arakviel.domain.service.impl;

import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.UserRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeService {

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public AuthorizeService(PersistenceContext persistenceContext,
        AuthenticationService authenticationService) {
        this.userRepository = persistenceContext.users.repository;
        this.authenticationService = authenticationService;
    }

    public boolean canCreate(UUID userId, DtoTypes dtoTypes) {
        User user = getUser(userId);

        return switch (dtoTypes) {
            case COMMENT, LIKE -> !user.role().equals(Role.BANNED);
            case TAG, POST -> !user.role().equals(Role.ADMIN);
            case USER -> true;
        };
    }

    public boolean canUpdate(Comment comment, UUID userId) {
        User user = getUser(userId);
        return comment.userId() == userId && !user.role().equals(Role.BANNED);
    }

    public boolean canDelete(Comment comment, UUID userId) {
        User user = getUser(userId);
        return comment.userId() == userId && !user.role().equals(Role.BANNED);
    }

    // оновлювати можуть дані лише власник акаунту (аутентифікований користувач) або адміністратори
    public boolean canUpdate(User user) {
        if (user.role() == Role.ADMIN) {
            return true;
        } else {
            return user.id() == authenticationService.getUser().id()
                   && !user.role().equals(Role.BANNED);
        }
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Користувача не знайдено"));
    }


    public enum DtoTypes {
        COMMENT,
        LIKE,
        TAG,
        POST,
        USER
    }
}
