package com.arakviel.persistence.context.impl;

import com.arakviel.persistence.context.GenericUnitOfWork;
import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.repository.contract.CommentRepository;
import org.springframework.stereotype.Component;

@Component
public class CommentContext extends GenericUnitOfWork<Comment> {

    private final CommentRepository repository;

    public CommentContext(CommentRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
