CREATE DATABASE shortlink;
USE shortlink;
CREATE TABLE short_urls (
    id BIGINT NOT NULL,
    code VARCHAR(10) NOT NULL,
    origin_url VARCHAR(2048) NOT NULL,
    expire_at DATETIME DEFAULT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
);