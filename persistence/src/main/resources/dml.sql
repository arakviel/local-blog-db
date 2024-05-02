
INSERT INTO users(id, username, email, password, avatar, birthday, role)
VALUES ('018f39f8-6850-75d3-b6b1-be865de4d061', 'john_doe', 'john.doe@example.com', 'password1',
        'avatar1.jpg', '1990-05-15 00:00:00', 'ADMIN'),
       ('018f39f8-8697-7a1a-93ab-5d42d7b42dd5', 'jane_smith', 'jane.smith@example.com', 'password2',
        'avatar2.jpg', '1985-09-21 00:00:00', 'GENERAL'),
       ('018f39f8-ea26-7c8d-945b-85c7e9f7cb4c', 'alex_jones', 'alex.jones@example.com', 'password3',
        'avatar3.jpg', '1988-12-10 00:00:00', 'GENERAL'),
       ('018f39f9-05de-704c-bdc4-9fb5e1432e19', 'emily_brown', 'emily.brown@example.com',
        'password4', 'avatar4.jpg', '1992-03-28 00:00:00', 'GENERAL'),
       ('018f39f9-1826-7cb9-9775-feee72794e6a', 'mike_wilson', 'mike.wilson@example.com',
        'password5', 'avatar5.jpg', '1983-07-02 00:00:00', 'GENERAL');


INSERT INTO tags (id, name, slug)
VALUES ('018f39f9-27de-706f-b91e-8b624f161234', 'Technology', 'technology'),
       ('018f39f9-379c-7553-b79c-6a7e6ba168b6', 'Science', 'science'),
       ('018f39f9-49ef-71f0-896a-817fe005d919', 'Art', 'art'),
       ('018f39f9-60b0-735f-83aa-46e0c7eade4e', 'Food', 'food'),
       ('018f39f9-72bb-7c08-b2ff-a6a56043bb1d', 'Travel', 'travel'),
       ('018f39f9-8446-7a12-9295-e8da9646033b', 'Music', 'music'),
       ('018f39f9-9f12-7835-8e5c-34c22d66f636', 'Fashion', 'fashion'),
       ('018f39f9-afd7-72dc-8349-9611d0d24fb1', 'Health', 'health'),
       ('018f39f9-c35a-7597-8418-75c724d0fc0c', 'Sports', 'sports'),
       ('018f39f9-d4e9-75c7-962c-a492dbcd546a', 'Nature', 'nature');

INSERT INTO posts (id, slug, title, description, body, image, is_published, created_at, updated_at,
                   user_id)
VALUES ('018f39fa-dfb7-791d-be2e-a4b2f8c415be', 'post-1', 'First Post', 'Description of first post',
        'Body of first post', 'image1.jpg', true, '2024-05-02 10:00:00', '2024-05-02 10:00:00',
        '018f39f8-6850-75d3-b6b1-be865de4d061'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', 'post-2', 'Second Post',
        'Description of second post',
        'Body of second post', 'image2.jpg', true, '2024-05-02 11:00:00', '2024-05-02 11:00:00',
        '018f39f8-6850-75d3-b6b1-be865de4d061'),
       ('018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', 'post-3', 'Third Post', 'Description of third post',
        'Body of third post', 'image3.jpg', false, '2024-05-02 12:00:00', '2024-05-02 12:00:00',
        '018f39f8-ea26-7c8d-945b-85c7e9f7cb4c'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', 'post-4', 'Fourth Post',
        'Description of fourth post',
        'Body of fourth post', 'image4.jpg', false, '2024-05-02 13:00:00', '2024-05-02 13:00:00',
        '018f39f9-1826-7cb9-9775-feee72794e6a');

INSERT INTO comments (id, body, user_id, post_id, created_at)
VALUES ('018f39ff-0e7a-7011-9d40-beca20844e49', 'Great post!', '018f39f9-1826-7cb9-9775-feee72794e6a', '018f39fb-64eb-7088-b12e-e0a01112c43e', '2024-05-02 14:00:00'),
       ('018f3a01-8305-7c2c-9309-bdc992e8c6e8', 'Interesting topic.', '018f39f8-6850-75d3-b6b1-be865de4d061', '018f39fb-64eb-7088-b12e-e0a01112c43e', '2024-05-02 15:00:00'),
       ('018f3a01-a266-74f9-9359-13615ae4baf3', 'Looking forward to more.', '018f39f8-6850-75d3-b6b1-be865de4d061', '018f39fb-64eb-7088-b12e-e0a01112c43e', '2024-05-02 16:00:00'),
       ('018f3a01-e5d3-7652-aee6-69bb6a86852a', 'I have a question.', '018f39f8-ea26-7c8d-945b-85c7e9f7cb4c', '018f39fb-36f7-78f7-98a9-17a60ccfdd58', '2024-05-02 18:00:00'),
       ('018f3a01-ffee-7c4b-85d1-5e0dc53dc805', 'Can you elaborate?', '018f39f8-ea26-7c8d-945b-85c7e9f7cb4c', '018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '2024-05-02 19:00:00'),
       ('018f3a02-1883-7a48-9bba-cfcdd91b4e36', 'This is awesome!', '018f39f9-05de-704c-bdc4-9fb5e1432e19', '018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '2024-05-02 20:00:00'),
       ('018f3a02-358c-7fcf-9fcc-6354d45cb621', 'I disagree.', '018f39f9-1826-7cb9-9775-feee72794e6a', '018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '2024-05-02 21:00:00'),
       ('018f3a02-50e3-7ffd-af2c-0721dc864282', 'Fascinating.', '018f39f9-05de-704c-bdc4-9fb5e1432e19', '018f39fa-dfb7-791d-be2e-a4b2f8c415be', '2024-05-02 22:00:00'),
       ('018f3a02-6a49-77c7-8a8a-586ffd3f61aa', 'I learned something new.', '018f39f9-1826-7cb9-9775-feee72794e6a', '018f39fa-dfb7-791d-be2e-a4b2f8c415be', '2024-05-02 23:00:00');

INSERT INTO post_tag(post_id, tag_id)
VALUES ('018f39fa-dfb7-791d-be2e-a4b2f8c415be', '018f39f9-27de-706f-b91e-8b624f161234'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-379c-7553-b79c-6a7e6ba168b6'),
       ('018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '018f39f9-49ef-71f0-896a-817fe005d919'),
       ('018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '018f39f9-60b0-735f-83aa-46e0c7eade4e'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-72bb-7c08-b2ff-a6a56043bb1d'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-8446-7a12-9295-e8da9646033b'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-9f12-7835-8e5c-34c22d66f636'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-afd7-72dc-8349-9611d0d24fb1'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-c35a-7597-8418-75c724d0fc0c'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-d4e9-75c7-962c-a492dbcd546a'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-27de-706f-b91e-8b624f161234'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-49ef-71f0-896a-817fe005d919'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-60b0-735f-83aa-46e0c7eade4e'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-72bb-7c08-b2ff-a6a56043bb1d'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-8446-7a12-9295-e8da9646033b'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-9f12-7835-8e5c-34c22d66f636'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-afd7-72dc-8349-9611d0d24fb1'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-c35a-7597-8418-75c724d0fc0c'),
       ('018f39fb-36f7-78f7-98a9-17a60ccfdd58', '018f39f9-d4e9-75c7-962c-a492dbcd546a'),
       ('018f39fb-4f4c-7787-a84c-c3ff9b2a5da0', '018f39f9-27de-706f-b91e-8b624f161234'),
       ('018f39fb-64eb-7088-b12e-e0a01112c43e', '018f39f9-379c-7553-b79c-6a7e6ba168b6');