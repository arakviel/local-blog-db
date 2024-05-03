package com.arakviel.persistence.repository.impl.jdbc;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.GenericJdbcRepository;
import com.arakviel.persistence.repository.contract.TableNames;
import com.arakviel.persistence.repository.contract.UserRepository;
import com.arakviel.persistence.repository.mapper.impl.UserRowMapper;
import com.arakviel.persistence.util.ConnectionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
}
