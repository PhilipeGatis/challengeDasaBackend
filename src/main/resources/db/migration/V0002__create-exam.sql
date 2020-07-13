create table if not exists exam (
   id uuid primary key default uuid_generate_v4(),
   status int4 default 0,
   name varchar(255) not null,
   type varchar(255) not null,
   created_at timestamp with time zone default current_timestamp not null,
   updated_at timestamp with time zone default current_timestamp not null
);
