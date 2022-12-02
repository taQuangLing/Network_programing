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