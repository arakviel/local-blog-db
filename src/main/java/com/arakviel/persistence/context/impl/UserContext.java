package com.arakviel.persistence.context.impl;

import com.arakviel.persistence.context.GenericUnitOfWork;
import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.repository.contract.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserContext extends GenericUnitOfWork<User> {

    public final UserRepository repository;

    protected UserContext(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
