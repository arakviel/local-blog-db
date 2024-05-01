package com.arakviel.persistence.repository.contract;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.Repository;
import java.util.Optional;

public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
