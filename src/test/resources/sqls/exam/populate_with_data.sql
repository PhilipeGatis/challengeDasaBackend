CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
insert into
    exam (id, name, type, status, created_at, updated_at) values (uuid_generate_v4(), 'Exam1', 'IMAGEM', 0, now(), now());
insert into
    exam (id, name, type, status, created_at, updated_at) values (uuid_generate_v4(), 'Exam2', 'ANALISE_CLINICA', 0, now(), now());
insert into
    exam (id, name, type, status, created_at, updated_at) values (uuid_generate_v4(), 'Exam3', 'IMAGEM', 1, now(), now());
