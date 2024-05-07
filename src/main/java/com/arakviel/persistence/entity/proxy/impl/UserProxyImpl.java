package com.arakviel.persistence.entity.proxy.impl;

import com.arakviel.persistence.entity.User;
import com.arakviel.persistence.entity.proxy.contract.UserProxy;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.UserRepository;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserProxyImpl implements UserProxy {

    private final ApplicationContext applicationContext;

    public UserProxyImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public User get(UUID entityId) {
        UserProxy proxy = (userId) -> applicationContext.getBean(UserRepository.class)
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти користувача за id"));

        return proxy.get(entityId);
    }
}
