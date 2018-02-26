--liquibase formatted sql

--changeset pmuchowski:1519481915099-1
ALTER TABLE choice DROP COLUMN ivr_pressed_key;

