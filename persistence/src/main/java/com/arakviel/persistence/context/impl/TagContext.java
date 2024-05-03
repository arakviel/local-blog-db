package com.arakviel.persistence.context.impl;

import com.arakviel.persistence.context.GenericUnitOfWork;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.contract.TagRepository;
import org.springframework.stereotype.Component;

@Component
public class TagContext extends GenericUnitOfWork<Tag> {

    private final TagRepository repository;

    protected TagContext(TagRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
