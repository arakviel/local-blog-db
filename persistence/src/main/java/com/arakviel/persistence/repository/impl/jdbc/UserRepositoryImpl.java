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
import java.util.List;
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
    protected List<String> tableAttributes() {
        return List.of("username", "email", "password", "avatar", "birthday", "role");
    }

    @Override
    protected List<Object> tableValues(User user) {
        ArrayList<Object> values = new ArrayList<>();
        values.add(user.username());
        values.add(user.email());
        values.add(user.password());
        values.add(user.avatar());
        values.add(user.birthday());
        values.add(user.role());
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

        if (Objects.nonNull(userFilterDto.username())) {
            filters.put("username", userFilterDto.username());
        }
        if (Objects.nonNull(userFilterDto.email())) {
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
