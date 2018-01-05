--liquibase formatted sql

--changeset pmuchowski:1514058409544-1
ALTER TABLE community_health_worker MODIFY community_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1514058409544-2
ALTER TABLE community_health_worker MODIFY first_name VARCHAR(255) NOT NULL;

--changeset pmuchowski:1514058409544-3
ALTER TABLE community_health_worker MODIFY gender VARCHAR(255) NOT NULL;

--changeset pmuchowski:1514058409544-4
ALTER TABLE community_health_worker MODIFY preferred_language VARCHAR(255) NOT NULL;

--changeset pmuchowski:1514058409544-5
ALTER TABLE community_health_worker MODIFY second_name VARCHAR(255) NOT NULL;

