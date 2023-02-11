create table user (
    id int auto_increment,
    password varchar(60) not null,
    email varchar(150) not null,
    bio text,
    avatar text,
    name varchar(20) not null,
    gender tinyint(1),
    deleted tinyint(1),
    created_at timestamp not null ,
    birthday timestamp,
    primary key (id)
)
create table follow(
    id int auto_increment,
    user_id int not null,
    follower_id int not null,
    status int,
    primary key (id)
)
create table noitification(
    id int auto_increment,
    to_user_id int not null,
    from_user_id int not null ,
    content text,
    type tinyint(1),
    seen tinyint(1),
    primary key (id)
)
create table noitify_contain(
    id int auto_increment,
    noitify_id int not null ,
    cmt_id int not null ,
    post_id int not null ,
    primary key (id)
)
create table comment(
    id int auto_increment,
    post_id int not null,
    user_id int not null ,
    created_at timestamp not null ,
    content text,
    primary key (id)
)
create table post(
    id int auto_increment,
    status tinyint(1) not null ,
    title varchar(100),
    user_id int not null,
    content text,
    created_at timestamp not null,
    book varchar(100) not null,
    primary key (id)
)
create table file(
    id int auto_increment,
    size int not null,
    path text not null ,
    primary key (id)
)
create table contains(
    id int auto_increment,
    post_id int not null ,
    file_id int not null ,
    image_id int not null ,
    primary key (id)
)
create table reaction(
    id int auto_increment,
    user_id int not null ,
    post_id int not null ,
    primary key (id)
)