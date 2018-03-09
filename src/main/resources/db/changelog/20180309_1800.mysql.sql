--liquibase formatted sql

--changeset chris:1520614865990-1
ALTER TABLE community_health_worker ADD year_of_birth INT NULL;

--changeset chris:1520614865990-2
ALTER TABLE community_health_worker DROP COLUMN date_of_birth;
