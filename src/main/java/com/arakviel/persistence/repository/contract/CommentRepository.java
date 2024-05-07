package com.arakviel.persistence.repository.contract;

import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.filter.CommentFilterDto;
import com.arakviel.persistence.repository.Repository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface CommentRepository extends Repository<Comment> {

    Set<Comment> findAllByPostId(UUID postId);

    Set<Comment> findAllByUserId(UUID userId);

    Set<Comment> findAll(int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto);

    Set<Comment> findAllByPostId(UUID postId,
        int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto);

    Set<Comment> findAllByUserId(UUID userId,
        int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto);
}
