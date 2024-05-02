package com.arakviel.persistence.entity.proxy;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.repository.contract.TagRepository;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PostsProxy implements Posts {

    private final ApplicationContext applicationContext;

    public PostsProxy(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<Post> get(UUID tagId) {
        var tagRepository = applicationContext.getBean(TagRepository.class);
        Posts posts = tId -> Collections.unmodifiableSet(tagRepository.findAllPosts(tId));
        return posts.get(tagId);
    }
}
