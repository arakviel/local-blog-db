DROP DATABASE IF EXISTS blog;
--CREATE DATABASE blog;
BEGIN;

-- Drop tables in correct order considering foreign key dependencies
DROP TABLE IF EXISTS blog.post_tag;
DROP TABLE IF EXISTS blog.comments;
DROP TABLE IF EXISTS blog.posts;
DROP TABLE IF EXISTS blog.tags;
DROP TABLE IF EXISTS blog.users;

-- Create users table
CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    login VARCHAR(32) NOT NULL,
    password VARCHAR(64) NOT NULL,
    photo VARCHAR(24) NULL,
    CONSTRAINT users_login_key UNIQUE(login),
    CONSTRAINT users_login_min_length_check CHECK(LENGTH(login) > 5)
);

-- Create tags table
CREATE TABLE tags(
    id SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    CONSTRAINT tags_name_key UNIQUE(name)
);

-- Create posts table
CREATE TABLE posts(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(248) NOT NULL,
    body TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT posts_user_id_users_id_fkey FOREIGN KEY(user_id) REFERENCES users(id) ON UPDATE CASCADE,
    CONSTRAINT posts_title_key UNIQUE(title),
    CONSTRAINT posts_title_min_length_check CHECK(LENGTH(title) > 5)
);

-- Create comments table
CREATE TABLE comments(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    post_id INT NOT NULL,
    body VARCHAR(1028) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT comments_user_id_users_id_fkey FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT comments_post_id_posts_id_fkey FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT comments_body_min_length_check CHECK(LENGTH(body) > 10)
);

-- Create post_tag junction table
CREATE TABLE post_tag(
    post_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY(post_id, tag_id),
    CONSTRAINT fk_post_tag_posts FOREIGN KEY(post_id) REFERENCES posts(id),
    CONSTRAINT fk_post_tag_tags FOREIGN KEY(tag_id) REFERENCES tags(id)
);

COMMIT;