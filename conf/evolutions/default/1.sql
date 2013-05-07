# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table rooms (
  id                        bigint auto_increment not null,
  theme                     varchar(255) not null,
  status                    integer not null,
  constraint ck_rooms_status check (status in (0,1,2,3)),
  constraint pk_rooms primary key (id))
;

create table messages (
  id                        bigint auto_increment not null,
  message                   varchar(255) not null,
  from_email                varchar(255),
  send                      datetime,
  constraint pk_messages primary key (id))
;

create table users (
  email                     varchar(255) not null,
  name                      varchar(255) not null,
  password                  varchar(255) not null,
  role                      integer not null,
  constraint ck_users_role check (role in (0,1)),
  constraint pk_users primary key (email))
;


create table user_room (
  user_email                     varchar(255) not null,
  room_id                        bigint not null,
  constraint pk_user_room primary key (user_email, room_id))
;
alter table messages add constraint fk_messages_from_1 foreign key (from_email) references users (email) on delete restrict on update restrict;
create index ix_messages_from_1 on messages (from_email);



alter table user_room add constraint fk_user_room_users_01 foreign key (user_email) references users (email) on delete restrict on update restrict;

alter table user_room add constraint fk_user_room_rooms_02 foreign key (room_id) references rooms (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table rooms;

drop table messages;

drop table users;

drop table user_room;

SET FOREIGN_KEY_CHECKS=1;

