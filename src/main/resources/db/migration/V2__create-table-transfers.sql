create table transfers(
    id varchar(255) primary key not null,
    payer_id varchar(255) not null,
    payee_id varchar(255) not null,
    amount decimal(10,2) not null,
    payed_at datetime not null
)