package com.arakviel.persistence.repository.contract;

import com.arakviel.persistence.entity.Post;
import com.arakviel.persistence.entity.Tag;
import com.arakviel.persistence.repository.Repository;
import java.util.Set;
import java.util.UUID;

public interface TagRepository extends Repository<Tag>, ManyToMany {

    Set<Post> getPosts(UUID tagId);
}
