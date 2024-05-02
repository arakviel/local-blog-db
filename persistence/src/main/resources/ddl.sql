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
CREATE TABLE users(
    id          UUID PRIMARY KEY,
    username    VARCHAR(32)     NOT NULL UNIQUE,
    email       VARCHAR(128)    NOT NULL UNIQUE,
    password    VARCHAR(72)     NOT NULL,
    avatar      VARCHAR(128)    NULL,
    birthday    TIMESTAMP       NULL,
    role        VARCHAR(12)     NOT NULL,
    CONSTRAINT users_username_key UNIQUE(username),
    CONSTRAINT users_email_key UNIQUE(email),
    CONSTRAINT users_username_min_length_check CHECK(LENGTH(username) > 5)
);

-- Create tags table
CREATE TABLE tags(
    id UUID PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    slug VARCHAR(128) NOT NULL,
    CONSTRAINT tags_name_key UNIQUE(name)
);

-- Create posts table
CREATE TABLE posts(
    id              UUID PRIMARY KEY,
    slug            VARCHAR(128) NOT NULL,
    title           VARCHAR(128) NOT NULL,
    description     VARCHAR(312) NOT NULL,
    body            TEXT         NOT NULL,
    image           VARCHAR(128) NULL,
    is_published    boolean      NOT NULL DEFAULT false,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL,
    user_id         UUID         NOT NULL,
    CONSTRAINT posts_user_id_users_id_fkey  FOREIGN KEY(user_id) REFERENCES users(id)
                                            ON DELETE CASCADE
                                            ON UPDATE CASCADE,
    CONSTRAINT posts_title_key UNIQUE(title),
    CONSTRAINT posts_title_min_length_check CHECK(LENGTH(title) > 5)
);

-- Create comments table
CREATE TABLE comments(
    id          UUID PRIMARY KEY,
    body        VARCHAR(1028)   NOT NULL,
    user_id     UUID            NOT NULL,
    post_id     UUID            NOT NULL,
    created_at  TIMESTAMP       NOT NULL,
    CONSTRAINT comments_user_id_users_id_fkey FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT comments_post_id_posts_id_fkey FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT comments_body_min_length_check CHECK(LENGTH(body) > 10)
);

-- Create likes table
CREATE TABLE likes(
    id          UUID PRIMARY KEY,
    post_id     UUID            NOT NULL,
    user_id     UUID            NOT NULL,
    body        VARCHAR(1028)   NOT NULL,
    created_at  TIMESTAMP       NOT NULL,
    updated_at  TIMESTAMP       NOT NULL,
    CONSTRAINT likes_user_id_users_id_fkey FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT likes_post_id_posts_id_fkey FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Create post_tag junction table
CREATE TABLE post_tag(
    post_id UUID NOT NULL,
    tag_id  UUID NOT NULL,
    PRIMARY KEY(post_id, tag_id),
    CONSTRAINT fk_post_tag_posts FOREIGN KEY(post_id) REFERENCES posts(id),
    CONSTRAINT fk_post_tag_tags FOREIGN KEY(tag_id) REFERENCES tags(id)
);