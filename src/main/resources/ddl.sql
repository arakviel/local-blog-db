--DROP DATABASE IF EXISTS blog;
--CREATE DATABASE blog;
-- Drop tables in correct order considering foreign key dependencies
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users
    (
        id uuid
            PRIMARY KEY,
        username VARCHAR(32) NOT NULL
            UNIQUE,
        email VARCHAR(128) NOT NULL
            UNIQUE,
        password VARCHAR(72) NOT NULL,
        avatar bytea NULL,
        birthday TIMESTAMP NULL,
        role VARCHAR(12) NOT NULL,
        CONSTRAINT users_username_key
            UNIQUE (username),
        CONSTRAINT users_email_key
            UNIQUE (email),
        CONSTRAINT users_username_min_length_check
            CHECK (LENGTH(username) > 5)
    );

-- Create tags table
CREATE TABLE tags
    (
        id uuid
            PRIMARY KEY,
        name VARCHAR(128) NOT NULL,
        slug VARCHAR(128) NOT NULL,
        CONSTRAINT tags_name_key
            UNIQUE (name)
    );

-- Create posts table
CREATE TABLE posts
    (
        id uuid
            PRIMARY KEY,
        slug VARCHAR(128) NOT NULL,
        title VARCHAR(128) NOT NULL,
        description VARCHAR(312) NOT NULL,
        body TEXT NOT NULL,
        image bytea NULL,
        is_published boolean NOT NULL DEFAULT FALSE,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL,
        user_id uuid NOT NULL,
        CONSTRAINT posts_user_id_users_id_fkey
            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT posts_title_key
            UNIQUE (title),
        CONSTRAINT posts_title_min_length_check
            CHECK (LENGTH(title) > 5)
    );

-- Create comments table
CREATE TABLE comments
    (
        id uuid
            PRIMARY KEY,
        body VARCHAR(1028) NOT NULL,
        user_id uuid NOT NULL,
        post_id uuid NOT NULL,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL,
        CONSTRAINT comments_user_id_users_id_fkey
            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT comments_post_id_posts_id_fkey
            FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT comments_body_min_length_check
            CHECK (LENGTH(body) > 10)
    );

-- Create likes table
CREATE TABLE likes
    (
        id uuid
            PRIMARY KEY,
        user_id uuid NOT NULL,
        post_id uuid NOT NULL,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL,
        CONSTRAINT likes_user_id_users_id_fkey
            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT likes_post_id_posts_id_fkey
            FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE ON UPDATE CASCADE
    );

-- Create post_tag junction table
CREATE TABLE post_tag
    (
        post_id uuid NOT NULL,
        tag_id uuid NOT NULL,
        PRIMARY KEY (post_id, tag_id),
        CONSTRAINT fk_post_tag_posts
            FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT fk_post_tag_tags
            FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE ON UPDATE CASCADE
    );