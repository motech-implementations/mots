--liquibase formatted sql

--changeset pmuchowski:1511024378665-1
ALTER TABLE module ADD ivr_group VARCHAR(255) NULL;

