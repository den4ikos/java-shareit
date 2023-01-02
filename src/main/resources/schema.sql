drop table if exists users, requests, items, bookings, comments;
create table if not exists users
(
    id              bigint not null primary key,
    name            varchar(255) not null,
    email           varchar(255) not null
);

create table if not exists requests
(
    id              bigint not null generated by default as identity primary key,
    description     text not null,
    requestor_id    bigint
);

create table if not exists items
(
    id              bigint not null generated by default as identity primary key,
    name            varchar(255) not null,
    description     text not null,
    is_available    boolean not null,
    owner_id        bigint,
    request_id      bigint
);

create table if not exists bookings
(
    id              bigint not null generated by default as identity primary key,
    date_start      timestamp without time zone not null,
    date_end        timestamp without time zone not null,
    status          varchar not null,
    item_id         bigint,
    booker_id       bigint
);

create table if not exists comments
(
    id              bigint not null generated by default as identity primary key,
    comment         text not null,
    item_id         bigint,
    author_id       bigint,
    created         timestamp without time zone not null
);

alter table users drop constraint if exists users_email_unique;
alter table users add constraint users_email_unique unique (email);

alter table requests drop constraint if exists fk_requests_requestor_id;
alter table requests add constraint fk_requests_requestor_id foreign key(requestor_id) references requests (id) on delete cascade;

alter table items drop constraint if exists fk_items_owner_id;
alter table items add constraint fk_items_owner_id foreign key(owner_id) references users (id) on delete set null;

alter table items drop constraint if exists fk_items_request_id;
alter table items add constraint fk_items_request_id foreign key(request_id) references requests (id) on delete set null;

alter table bookings drop constraint if exists fk_bookings_item_id;
alter table bookings add constraint fk_bookings_item_id foreign key(item_id) references items (id) on delete cascade;

alter table bookings drop constraint if exists fk_bookings_booker_id;
alter table bookings add constraint fk_bookings_booker_id foreign key(booker_id) references users (id) on delete cascade;

alter table comments drop constraint if exists fk_comments_item_id;
alter table comments add constraint fk_comments_item_id foreign key(item_id) references items (id) on delete cascade;

alter table comments drop constraint if exists fk_comments_author_id;
alter table comments add constraint fk_comments_author_id foreign key(author_id) references users (id) on delete cascade;
