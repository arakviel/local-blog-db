package com.arakviel.persistence.context.impl;

import com.arakviel.persistence.context.GenericUnitOfWork;
import com.arakviel.persistence.entity.Like;
import com.arakviel.persistence.repository.contract.LikeRepository;
import org.springframework.stereotype.Component;

@Component
public class LikeContext extends GenericUnitOfWork<Like> {

    private final LikeRepository repository;

    protected LikeContext(LikeRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
