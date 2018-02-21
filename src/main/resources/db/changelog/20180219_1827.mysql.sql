--liquibase formatted sql

--changeset pmuchowski:1519061247749-1
ALTER TABLE choice ADD choice_id INT NOT NULL;

