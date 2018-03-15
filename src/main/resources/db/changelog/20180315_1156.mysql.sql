--liquibase formatted sql

--changeset pmuchowski:1521111378981-1
ALTER TABLE course_module ADD previous_version_id VARCHAR(255) NULL;

--changeset pmuchowski:1521111378981-2
ALTER TABLE course_module ADD CONSTRAINT FKqycbh2ogasb0x9ldt9kxet46g FOREIGN KEY (previous_version_id) REFERENCES course_module (id);

