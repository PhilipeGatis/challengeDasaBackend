create table if not exists laboratory_exam (
   exam_id uuid not null,
   laboratory_id uuid not null
);

alter table if exists laboratory_exam add constraint laboratory_id_exam_id foreign key (laboratory_id) references laboratory;
alter table if exists laboratory_exam add constraint exam_id_laboratory_id foreign key (exam_id) references exam;
