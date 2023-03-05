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
    quantity bigint not null default 0,
    price bigint not null,
    product_type VARCHAR(64) not null,
    product_status VARCHAR(64) not null default 'OUT_OF_STOCK',
    book_id bigint null,
    created_at datetime null,
    modified_at datetime null
);