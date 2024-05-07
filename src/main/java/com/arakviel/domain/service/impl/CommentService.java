package com.arakviel.domain.service.impl;


import com.arakviel.domain.dto.CommentStoreDto;
import com.arakviel.domain.dto.CommentUpdateDto;
import com.arakviel.domain.exception.AccessDeniedException;
import com.arakviel.domain.exception.ValidationException;
import com.arakviel.domain.service.impl.AuthorizeService.DtoTypes;
import com.arakviel.persistence.context.factory.PersistenceContext;
import com.arakviel.persistence.context.impl.CommentContext;
import com.arakviel.persistence.entity.Comment;
import com.arakviel.persistence.entity.filter.CommentFilterDto;
import com.arakviel.persistence.exception.EntityNotFoundException;
import com.arakviel.persistence.repository.contract.CommentRepository;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentContext commentContext;
    private final CommentRepository commentRepository;
    private final AuthorizeService authorizeService;
    private final Validator validator;

    public CommentService(PersistenceContext persistenceContext, AuthorizeService authorizeService,
        Validator validator) {
        this.commentContext = persistenceContext.comments;
        this.commentRepository = persistenceContext.comments.repository;
        this.authorizeService = authorizeService;
        this.validator = validator;
    }

    public Comment findById(UUID id) {
        return commentContext.repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Не вдалось знайти коментар"));
    }

    public Set<Comment> findAll() {
        return new TreeSet<>(commentRepository.findAll());
    }

    public Set<Comment> findAll(int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto) {
        return new TreeSet<>(commentRepository.findAll(
            offset,
            limit,
            sortColumn,
            ascending,
            commentFilterDto));
    }

    public Set<Comment> findAllByPostId(UUID postId) {
        return new TreeSet<>(commentRepository.findAllByPostId(postId));
    }

    public Set<Comment> findAllByUserId(UUID postId) {
        return new TreeSet<>(commentRepository.findAllByUserId(postId));
    }

    public Set<Comment> findAllByPostId(UUID postId,
        int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto) {
        return new TreeSet<>(commentRepository.findAllByPostId(
            postId,
            offset,
            limit,
            sortColumn,
            ascending,
            commentFilterDto));
    }

    public Set<Comment> findAllByUserId(UUID userId,
        int offset,
        int limit,
        String sortColumn,
        boolean ascending,
        CommentFilterDto commentFilterDto) {
        return new TreeSet<>(commentRepository.findAllByUserId(
            userId,
            offset,
            limit,
            sortColumn,
            ascending,
            commentFilterDto));
    }

    public long count() {
        return commentRepository.count();
    }

    public Comment create(CommentStoreDto commentStoreDto) {
        var violations = validator.validate(commentStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("збереженні коментаря", violations);
        } else if (!authorizeService.canCreate(commentStoreDto.userId(), DtoTypes.COMMENT)) {
            throw AccessDeniedException.bannedUser("додавати коментарі");
        }

        Comment comment = new Comment(
            null,
            commentStoreDto.body(),
            commentStoreDto.userId(),
            null,
            commentStoreDto.postId(),
            null,
            null,
            null
        );

        commentContext.registerNew(comment);
        commentContext.commit();
        return commentContext.getEntity();
    }

    public Comment update(CommentUpdateDto commentUpdateDto) {
        var violations = validator.validate(commentUpdateDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("оновленні коментаря", violations);
        }

        Comment oldComment = findById(commentUpdateDto.id());

        if (!authorizeService.canUpdate(oldComment, commentUpdateDto.userId())) {
            throw AccessDeniedException.notAuthorOrBannedUser("оновлювати коментарі");
        }

        Comment comment = new Comment(
            commentUpdateDto.id(),
            commentUpdateDto.body(),
            commentUpdateDto.userId(),
            null,
            commentUpdateDto.postId(),
            null,
            null,
            null
        );

        commentContext.registerModified(comment);
        commentContext.commit();
        return commentContext.getEntity();
    }

    public boolean delete(UUID id, UUID userId) {
        Comment comment = findById(id);
        if (!authorizeService.canDelete(comment, userId)) {
            throw AccessDeniedException.notAuthorOrBannedUser("видаленні коментарів");
        }

        return commentContext.repository.delete(comment.id());
    }
}
