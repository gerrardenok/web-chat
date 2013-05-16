# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table messages (
  id                        bigint auto_increment not null,
  message                   varchar(255) not null,
  room_id                   bigint,
  from_email                varchar(255),
  send                      datetime not null,
  constraint pk_messages primary key (id))
;

create table rooms (
  id                        bigint auto_increment not null,
  theme                     varchar(255) not null,
  status                    integer not null,
  constraint ck_rooms_status check (status in (0,1,2,3)),
  constraint pk_rooms primary key (id))
;

create table users (
  email                     varchar(255) not null,
  name                      varchar(255) not null,
  password                  varchar(255) not null,
  role                      integer not null,
  constraint ck_users_role check (role in (0,1)),
  constraint pk_users primary key (email))
;

create table user_room_relationship (
  id                        bigint auto_increment not null,
  user_email                varchar(255),
  room_id                   bigint,
  role                      integer,
  constraint ck_user_room_relationship_role check (role in (0,1)),
  constraint pk_user_room_relationship primary key (id))
;

alter table messages add constraint fk_messages_room_1 foreign key (room_id) references rooms (id) on delete restrict on update restrict;
create index ix_messages_room_1 on messages (room_id);
alter table messages add constraint fk_messages_from_2 foreign key (from_email) references users (email) on delete restrict on update restrict;
create index ix_messages_from_2 on messages (from_email);
alter table user_room_relationship add constraint fk_user_room_relationship_user_3 foreign key (user_email) references users (email) on delete restrict on update restrict;
create index ix_user_room_relationship_user_3 on user_room_relationship (user_email);
alter table user_room_relationship add constraint fk_user_room_relationship_room_4 foreign key (room_id) references rooms (id) on delete restrict on update restrict;
create index ix_user_room_relationship_room_4 on user_room_relationship (room_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table messages;

drop table rooms;

drop table users;

drop table user_room_relationship;

SET FOREIGN_KEY_CHECKS=1;

