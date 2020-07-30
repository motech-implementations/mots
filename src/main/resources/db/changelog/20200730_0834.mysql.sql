--liquibase formatted sql

--changeset pmuchowski:1596090914090-4
ALTER TABLE district ADD ivr_group_id VARCHAR(255) NULL;

