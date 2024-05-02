package com.arakviel.persistence;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.contract.PostRepository;
import com.arakviel.persistence.util.ConnectionManager;
import com.arakviel.persistence.util.DatabaseInitializer;
import java.util.Set;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
        var connectionManager = context.getBean(ConnectionManager.class);
        var databaseInitializer = context.getBean(DatabaseInitializer.class);

        try {
            databaseInitializer.init();
            var postRepository = context.getBean(PostRepository.class);
            Set<Post> posts = postRepository.findAll();
            posts.forEach(System.out::println);
            Post post = posts.stream().findFirst().orElseThrow();
            Set<Tag> tags = post.getTagsLazy();
            tags.forEach(System.out::println);
            Tag tag = tags.stream().findFirst().orElseThrow();

            Set<Post> postsLazy = tag.getPostsLazy();
            postsLazy.forEach(System.out::println);
        } finally {
            connectionManager.closePool();
        }
    }
}
