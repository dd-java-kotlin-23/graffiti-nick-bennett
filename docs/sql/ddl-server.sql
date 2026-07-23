create sequence brush_stroke_seq start with 1 increment by 50;
create sequence canvas_seq start with 1 increment by 50;
create sequence user_profile_seq start with 1 increment by 50;
create table brush_stroke_point
(
    point_order     integer not null check ((point_order >= 0)),
    x               float(24),
    y               float(24),
    brush_stroke_id bigint  not null,
    primary key (point_order, brush_stroke_id)
);
create table brush_stroke
(
    color           integer                     not null,
    width           integer                     not null,
    added           timestamp(6) with time zone not null,
    brush_stroke_id bigint                      not null,
    canvas_id       bigint                      not null,
    contributor_id  bigint                      not null,
    primary key (brush_stroke_id)
);
create table canvas
(
    background_color integer default -1          not null,
    height           integer                     not null,
    width            integer                     not null,
    canvas_id        bigint                      not null,
    created          timestamp(6) with time zone not null,
    owner_id         bigint                      not null,
    external_key     uuid                        not null unique,
    primary key (canvas_id)
);
create table user_profile
(
    created      timestamp(6) with time zone not null,
    user_id      bigint                      not null,
    external_key uuid                        not null unique,
    display_name varchar(255)                not null unique,
    oauth_key    varchar(255)                not null unique,
    primary key (user_id)
);
alter table if exists brush_stroke_point
    add constraint FK2nd0l5820k0ko28gcfv10getn foreign key (brush_stroke_id) references brush_stroke;
alter table if exists brush_stroke
    add constraint FKj6s95s5hdvrg2u53gpve961l0 foreign key (canvas_id) references canvas;
alter table if exists brush_stroke
    add constraint FK9amxe3sn9liguxsbcbilggah7 foreign key (contributor_id) references user_profile;
alter table if exists canvas
    add constraint FK8p8wbr1ye6fso2noqrmbfb9xh foreign key (owner_id) references user_profile;
