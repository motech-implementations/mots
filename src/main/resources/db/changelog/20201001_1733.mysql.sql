--liquibase formatted sql

--changeset pmuchowski:1601994861454-1
ALTER TABLE community_health_worker MODIFY preferred_language VARCHAR(255) NULL;

