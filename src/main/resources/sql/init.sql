CREATE TABLE books (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    name varchar(128) not null,
    author varchar(128) not null,
    publisher varchar(128) not null,
    register tinyint(1) not null,
    created_at datetime null,
    modified_at datetime null
);