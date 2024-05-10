package com.arakviel.domain.service.impl;

import com.arakviel.domain.dto.UserStoreDto;
import com.arakviel.domain.dto.UserUpdateDto;
import com.arakviel.domain.exception.AccessDeniedException;
import com.arakviel.domain.exception.ValidationException;
import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.context.impl.UserContext;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.User.Role;
import com.arakviel.persistence.entity.filter.UserFilterDto;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.UserRepository;
import jakarta.validation.Validator;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserContext userContext;
    private final UserRepository userRepository;
    private final AuthorizeService authorizeService;
    private final FileService fileService;
    private final Validator validator;
    private Path defaultAvatar;

    public UserService(PersistenceContext persistenceContext, AuthorizeService authorizeService,
        FileService fileService, Validator validator) {
        this.userContext = persistenceContext.users;
        this.userRepository = persistenceContext.users.repository;
        this.authorizeService = authorizeService;
        this.fileService = fileService;
        this.validator = validator;
        // вказуємо дефолтний аватар
        defaultAvatar = fileService.getPathFromResource("default-avatar.png");
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача"));
    }

    public Set<User> findAll() {
        return new TreeSet<>(userRepository.findAll());
    }

    public Set<User> findAll(int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        UserFilterDto userFilterDto) {
        return new TreeSet<>(userRepository.findAll(
            offset,
            limit,
            sortColumn,
            ascending,
            userFilterDto));
    }

    public long count() {
        return userRepository.count();
    }

    public User create(UserStoreDto userStoreDto) {
        var violations = validator.validate(userStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("збереженні користувача", violations);
        }

        User user = new User(
            null,
            userStoreDto.username(),
            userStoreDto.email(),
            Password.hash(userStoreDto.password()).withBcrypt().getResult(),
            fileService.getBytes(userStoreDto.avatar()),
            userStoreDto.birthday(),
            Objects.nonNull(userStoreDto.role()) ? userStoreDto.role() : Role.GENERAL
        );

        userContext.registerNew(user);
        userContext.commit();
        return userContext.getEntity();
    }

    public User update(UserUpdateDto userUpdateDto) {
        var violations = validator.validate(userUpdateDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("оновленні користувача", violations);
        }

        User oldUser = findById(userUpdateDto.id());

        if (!authorizeService.canUpdate(oldUser)) {
            throw AccessDeniedException.notAuthorOrBannedUser("оновлювати користувача");
        }
        User user = new User(
            userUpdateDto.id(),
            userUpdateDto.username(),
            userUpdateDto.email(),
            Objects.nonNull(userUpdateDto.password()) ?
                Password.hash(userUpdateDto.password()).withBcrypt().getResult() : null,
            !userUpdateDto.avatar().equals(defaultAvatar) ?
                fileService.getBytes(userUpdateDto.avatar()) : fileService.getBytes(defaultAvatar),
            userUpdateDto.birthday(),
            userUpdateDto.role()
        );

        userContext.registerModified(user);
        userContext.commit();
        return userContext.getEntity();
    }
}
