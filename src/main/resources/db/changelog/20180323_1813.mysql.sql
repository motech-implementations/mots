--liquibase formatted sql

--changeset pmuchowski:1521825220680-1
ALTER TABLE in_charge MODIFY phone_number VARCHAR(255) NULL;

