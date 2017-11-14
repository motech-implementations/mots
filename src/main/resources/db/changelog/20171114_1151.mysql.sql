--liquibase formatted sql

--changeset pmuchowski:1510656667721-1
ALTER TABLE module ADD name_code VARCHAR(255) NOT NULL;

--changeset pmuchowski:1510656667721-2
ALTER TABLE module ADD CONSTRAINT UC_MODULENAME_CODE_COL UNIQUE (name_code);

