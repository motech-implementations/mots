--liquibase formatted sql

--changeset pmuchowski:1519834923042-1
CREATE TABLE course_module (id VARCHAR(255) NOT NULL, list_order INT NOT NULL, course_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1519834923042-2
ALTER TABLE course_module ADD PRIMARY KEY (id);

--changeset pmuchowski:1519834923042-3
ALTER TABLE course_module ADD CONSTRAINT FKkge7sg0xxyo0sxgfelpavhjdj FOREIGN KEY (course_id) REFERENCES course (id);

--changeset pmuchowski:1519834923042-4
ALTER TABLE course_module ADD CONSTRAINT FKnxpmkao1aniqf059wcl785y67 FOREIGN KEY (module_id) REFERENCES module (id);

--changeset pmuchowski:1519834923042-5
INSERT INTO course_module (id, course_id, module_id, list_order) SELECT module.id, module.course_id, module.id, module.module_number - 1 FROM module;

--changeset pmuchowski:1519834923042-6
ALTER TABLE module DROP FOREIGN KEY FKfq09oddpwjoxcirvkh9vnfnsg;

--changeset pmuchowski:1519834923042-7
ALTER TABLE module DROP COLUMN course_id;

--changeset pmuchowski:1519836179832-8
ALTER TABLE module DROP COLUMN module_number;

