CREATE SEQUENCE seq_user_id;

CREATE TABLE "user"
(
    user_id  BIGINT PRIMARY KEY NOT NULL,
    email    TEXT UNIQUE        NOT NULL,
    password TEXT               NOT NULL
);
