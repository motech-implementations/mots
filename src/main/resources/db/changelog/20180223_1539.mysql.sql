--liquibase formatted sql

--changeset user:1519396788567-1
ALTER TABLE unit_progress DROP FOREIGN KEY FKk04dsxve9pxghlvxawa8molh1;

--changeset user:1519396788567-2
ALTER TABLE question_response DROP FOREIGN KEY FKotqxl5s4ubc87rh77fhb5g25p;

--changeset user:1519396788567-3
ALTER TABLE unit_progress CHANGE module_id module_progress_id VARCHAR(255) NOT NULL;

--changeset user:1519396788567-4
ALTER TABLE question_response CHANGE unit_id unit_progress_id VARCHAR(255) NOT NULL;

--changeset user:1519396788567-5
ALTER TABLE question_response ADD CONSTRAINT FKcn5kg8v65bk53it5wykkncrmw FOREIGN KEY (unit_progress_id) REFERENCES unit_progress (id);

--changeset user:1519396788567-6
ALTER TABLE unit_progress ADD CONSTRAINT FKlhbirf0m2itbvy53uijbx7eu9 FOREIGN KEY (module_progress_id) REFERENCES module_progress (id);
