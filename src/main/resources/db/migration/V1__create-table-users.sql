create table users (
    id varchar(255) primary key not null,
    full_name varchar(255) not null,
    document varchar(14) not null,
    email varchar(120) not null,
    role varchar(12) not null,
    balance decimal(10,2) not null
)