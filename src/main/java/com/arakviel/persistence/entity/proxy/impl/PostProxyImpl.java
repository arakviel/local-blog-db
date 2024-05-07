package com.arakviel.persistence.entity.proxy.impl;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.proxy.contract.PostProxy;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.PostRepository;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PostProxyImpl implements PostProxy {

    private final ApplicationContext applicationContext;

    public PostProxyImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Post get(UUID entityId) {
        PostProxy proxy = (postId) -> applicationContext.getBean(PostRepository.class)
            .findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти пост за id"));

        return proxy.get(entityId);
    }
}
