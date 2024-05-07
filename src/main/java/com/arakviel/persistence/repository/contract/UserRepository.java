package com.arakviel.persistence.repository.contract;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.filter.UserFilterDto;
import com.arakviel.persistence.repository.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends Repository<User> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Set<User> findAll(int offset, int limit, String sortColumn, boolean ascending, UserFilterDto userFilterDto);
}
