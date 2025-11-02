DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    ultimAcces timestamp NULL DEFAULT NULL,
    dataCreated timestamp NULL DEFAULT NULL,
    dataUpdated timestamp NULL DEFAULT NULL
);