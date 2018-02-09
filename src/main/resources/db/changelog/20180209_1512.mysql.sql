--liquibase formatted sql

--changeset pmuchowski:1518185558045-1
ALTER TABLE module ADD course_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518185558045-2
ALTER TABLE module ADD CONSTRAINT FKfq09oddpwjoxcirvkh9vnfnsg FOREIGN KEY (course_id) REFERENCES course (id);

--changeset pmuchowski:1518185558045-3
ALTER TABLE course_module DROP FOREIGN KEY FKkge7sg0xxyo0sxgfelpavhjdj;

--changeset pmuchowski:1518185558045-4
ALTER TABLE course_module DROP FOREIGN KEY FKnxpmkao1aniqf059wcl785y67;

--changeset pmuchowski:1518185558045-5
DROP TABLE course_module;
