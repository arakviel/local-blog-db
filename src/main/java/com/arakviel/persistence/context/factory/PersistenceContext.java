package com.arakviel.persistence.context.factory;

import com.arakviel.persistence.context.impl.CommentContext;
import com.arakviel.persistence.context.impl.LikeContext;
import com.arakviel.persistence.context.impl.PostContext;
import com.arakviel.persistence.context.impl.TagContext;
import com.arakviel.persistence.context.impl.UserContext;
import org.springframework.stereotype.Component;

@Component
public class PersistenceContext {

    public final CommentContext comments;
    public final LikeContext likes;
    public final PostContext posts;
    public final TagContext tags;
    public final UserContext users;

    public PersistenceContext(
            CommentContext commentContext,
            LikeContext likeContext,
            PostContext postContext,
            TagContext tagContext,
            UserContext userContext) {
        this.comments = commentContext;
        this.likes = likeContext;
        this.posts = postContext;
        this.tags = tagContext;
        this.users = userContext;
    }
}
