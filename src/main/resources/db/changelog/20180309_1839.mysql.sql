--liquibase formatted sql

--changeset pmuchowski:1520617157717-1
ALTER TABLE module_progress ADD course_module_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1520617157717-2
ALTER TABLE course_module ADD created_date datetime NULL;

--changeset pmuchowski:1520617157717-3
ALTER TABLE course_module ADD ivr_id VARCHAR(255) NULL;

--changeset pmuchowski:1520617157717-4
ALTER TABLE course_module ADD ivr_name VARCHAR(255) NULL;

--changeset pmuchowski:1520617157717-5
ALTER TABLE course_module ADD updated_date datetime NULL;

--changeset pmuchowski:1520617157717-6
UPDATE course_module JOIN module ON module.id = course_module.module_id SET course_module.ivr_id = module.ivr_id, course_module.ivr_name = module.ivr_name;

--changeset pmuchowski:1520617157717-7
UPDATE module_progress JOIN course_module ON course_module.module_id = module_progress.module_id JOIN course ON course_module.course_id = course.id SET module_progress.course_module_id = course_module.id WHERE course.status = "RELEASED" OR course.status = "PREVIOUS_VERSION";

--changeset pmuchowski:1520617157717-8
ALTER TABLE module_progress ADD CONSTRAINT FKg9ofqigvb0r2mbpdhgiijvu31 FOREIGN KEY (course_module_id) REFERENCES course_module (id);

--changeset pmuchowski:1520617157717-9
ALTER TABLE module_progress DROP FOREIGN KEY FKrklx0sebigmraipwpbjrw14i9;

--changeset pmuchowski:1520617157717-10
ALTER TABLE module DROP COLUMN ivr_id;

--changeset pmuchowski:1520617157717-11
ALTER TABLE module DROP COLUMN ivr_name;

--changeset pmuchowski:1520617157717-12
ALTER TABLE module_progress DROP COLUMN module_id;

