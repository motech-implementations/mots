--liquibase formatted sql

--changeset user:1518621785211-1
ALTER TABLE jasper_templates DROP COLUMN data;

--changeset user:1518621785211-2
ALTER TABLE jasper_templates ADD data MEDIUMBLOB NULL;

--changeset user:1518621785211-3
ALTER TABLE jasper_templates ALTER visible DROP DEFAULT;
