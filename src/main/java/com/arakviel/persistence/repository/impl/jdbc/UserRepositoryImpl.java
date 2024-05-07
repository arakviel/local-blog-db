package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.filter.UserFilterDto;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.impl.UserRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends GenericJdbcRepository<User> implements UserRepository {

    public UserRepositoryImpl(ConnectionManager connectionManager, UserRowMapper userRowMapper) {
        super(connectionManager, userRowMapper, TableNames.USERS.getName());
    }

    @Override
    protected Map<String, Object> tableValues(User user) {
        Map<String, Object> values = new LinkedHashMap<>();

        if (!user.username().isBlank()) {
            values.put("username", user.username());
        }
        if (!user.email().isBlank()) {
            values.put("email", user.email());
        }
        if (!user.password().isBlank()) {
            values.put("password", user.password());
        }
        if (Objects.nonNull(user.avatar())) {
            values.put("avatar", user.avatar());
        }
        if (Objects.nonNull(user.birthday())) {
            values.put("birthday", user.birthday());
        }
        if (Objects.nonNull(user.role())) {
            values.put("role", user.role());
        }
        return values;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findBy("username", username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findBy("email", email);
    }

    @Override
    public Set<User> findAll(
        int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        UserFilterDto userFilterDto) {

        HashMap<String, Object> filters = new HashMap<>();

        if (!userFilterDto.username().isBlank()) {
            filters.put("username", userFilterDto.username());
        }
        if (!userFilterDto.email().isBlank()) {
            filters.put("email", userFilterDto.email());
        }
        if (Objects.nonNull(userFilterDto.birthday())) {
            filters.put("birthday", userFilterDto.birthday());
        }
        if (Objects.nonNull(userFilterDto.role())) {
            filters.put("role", userFilterDto.role());
        }

        return findAll(offset, limit, sortColumn, ascending, filters);
    }
}
