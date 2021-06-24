create table simple (
  id bigserial not null,
  counter int8 not null,
  created_by text,
  created_date timestamp,
  modified_by text,
  modified_date timestamp,
  name text not null,
  version int4 not null,
  primary key (id)
)