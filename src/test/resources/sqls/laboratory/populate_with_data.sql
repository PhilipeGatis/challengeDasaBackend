CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into
    laboratory (id, name, address, status, created_at, updated_at) values (uuid_generate_v4(), 'Lab1', 'Street1', 0, now(), now());
insert into
    laboratory (id, name, address, status, created_at, updated_at) values (uuid_generate_v4(), 'Lab2', 'Street2', 0, now(), now());
insert into
    laboratory (id, name, address, status, created_at, updated_at) values (uuid_generate_v4(), 'Lab2', 'Street2', 1, now(), now());
