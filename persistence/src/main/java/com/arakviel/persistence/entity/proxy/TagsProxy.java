package com.arakviel.persistence.entity.proxy;

import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.contract.PostRepository;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TagsProxy implements Tags {
    private final ApplicationContext applicationContext;

    public TagsProxy(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<Tag> get(UUID postId) {
        PostRepository postRepository = applicationContext.getBean(PostRepository.class);
        Tags tags = pId -> Collections.unmodifiableSet(postRepository.findAllTags(pId));
        return tags.get(postId);
    }
}
