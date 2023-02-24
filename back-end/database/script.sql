create table comment
(
    id         int auto_increment
        primary key,
    post_id    int       not null,
    user_id    int       not null,
    created_at timestamp not null,
    content    text      null
);

create table follow
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    others_id int not null,
    status    int null comment '0: user -> others,  1: friend'
);

create table notification
(
    id           int auto_increment
        primary key,
    to_user_id   int           not null,
    from_user_id int           not null,
    type         tinyint(1)    null comment '0: post, 1: comment, 2:follow, 3: accept,',
    seen         tinyint(1)    null,
    link_id      int default 0 null
);

create table post
(
    id         int auto_increment
        primary key,
    status     tinyint(1)   not null comment '0: private, 1: friend, 2: public',
    title      varchar(100) null,
    user_id    int          not null,
    content    text         null,
    created_at timestamp    not null,
    image      text         not null,
    path       text         null
);

create table reaction
(
    id      int auto_increment
        primary key,
    user_id int not null,
    post_id int not null
);

create table user
(
    id         int auto_increment
        primary key,
    password   varchar(60)          not null,
    email      varchar(150)         not null,
    bio        text                 null,
    avatar     text                 null,
    name       varchar(20)          not null,
    gender     tinyint(1)           null,
    deleted    tinyint(1) default 0 null,
    created_at timestamp            not null,
    birthday   date                 null,
    interest   text                 null
);


