package com.arakviel.persistence.context.impl;

import com.arakviel.persistence.context.GenericUnitOfWork;
import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.repository.contract.PostRepository;
import org.springframework.stereotype.Component;

@Component
public class PostContext extends GenericUnitOfWork<Post> {

    private final PostRepository repository;

    protected PostContext(PostRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
