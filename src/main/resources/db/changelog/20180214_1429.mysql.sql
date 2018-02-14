--liquibase formatted sql

--changeset user:1518614946943-1
ALTER TABLE jasper_templates ADD visible BIT DEFAULT 1 NULL;

--changeset user:1518614946943-2
ALTER TABLE jasper_templates DROP COLUMN is_displayed;
