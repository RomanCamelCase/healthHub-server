create table authority_tokens (
    id int primary key generated always as identity,
    token varchar(64) not null unique,
    user_id int not null references users(id),
    expires_at timestamp with time zone not null,
    authority varchar(64) not null
);