CREATE TABLE books (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    name varchar(128) not null,
    author varchar(128) not null,
    publisher varchar(128) not null,
    register tinyint(1) not null,
    created_at datetime null,
    modified_at datetime null
);

CREATE TABLE products (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    product_type VARCHAR(64) not null,
    quantity bigint not null default 0,
    price bigint not null,
    book_id bigint null,
    created_at datetime null,
    modified_at datetime null
);