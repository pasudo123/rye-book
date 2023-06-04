CREATE TABLE books (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    name varchar(128) not null,
    author varchar(128) not null,
    publisher varchar(128) not null,
    register tinyint(1) not null,
    created_at datetime null,
    modified_at datetime null
);

CREATE TABLE tickets (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    name varchar(128) not null,
    available_started_at datetime not null,
    available_ended_at datetime not null,
    remark varchar(256) null,
    register tinyint(1) not null,
    created_at datetime null,
    modified_at datetime null
);

CREATE TABLE products (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    quantity bigint not null default 0,
    price bigint not null,
    product_type VARCHAR(64) not null,
    product_status VARCHAR(64) not null default 'NEW',
    book_id bigint null,
    ticket_id bigint null,
    created_at datetime null,
    modified_at datetime null
);

CREATE TABLE booking (
    id bigint(20) PRIMARY KEY AUTO_INCREMENT,
    user_id varchar(128) not null,
    product_id bigint(20) not null,
    booking_status varchar(32) not null,
    created_at datetime null,
    modified_at datetime null
);
