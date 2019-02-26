--liquibase formatted sql

--changeset pmuchowski:1550934223141-1
ALTER TABLE district_assignment_log ADD group_id VARCHAR(255) NULL;

--changeset pmuchowski:1550934223141-2
ALTER TABLE district_assignment_log ADD CONSTRAINT FK74qra792bivntkq52wqycabhd FOREIGN KEY (group_id) REFERENCES chw_group (id);

--changeset pmuchowski:1550934223141-3
ALTER TABLE district_assignment_log MODIFY district_id VARCHAR(255) NULL;

