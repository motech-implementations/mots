--liquibase formatted sql

--changeset pmuchowski:1511019333393-1
CREATE TABLE module_assignment (assigned_modules_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1511019333393-2
ALTER TABLE module_assignment ADD PRIMARY KEY (assigned_modules_id, module_id);

--changeset pmuchowski:1511019333393-3
ALTER TABLE module_assignment ADD CONSTRAINT FKfpn0o6k6bemdlf42sgg2vstyw FOREIGN KEY (module_id) REFERENCES module (id);

--changeset pmuchowski:1511019333393-4
ALTER TABLE module_assignment ADD CONSTRAINT FKly23tqnk0vla1amxwm1r9rlky FOREIGN KEY (assigned_modules_id) REFERENCES assigned_modules (id);

--changeset pmuchowski:1511019333393-5
ALTER TABLE module_progress DROP FOREIGN KEY FKb3od5pjcv9xhiqxipdo4pytwi;

--changeset pmuchowski:1511019333393-6
ALTER TABLE module_progress DROP COLUMN assigned_modules_id;

