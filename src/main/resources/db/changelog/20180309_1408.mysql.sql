--liquibase formatted sql

--changeset chris:1520600902652-1
ALTER TABLE community_health_worker DROP COLUMN supervisor;
