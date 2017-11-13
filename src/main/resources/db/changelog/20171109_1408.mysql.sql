--liquibase formatted sql

--changeset pmuchowski:1510232903873-1
ALTER TABLE community_health_worker ADD has_peer_supervisor BIT NULL;

--changeset pmuchowski:1510232903873-2
ALTER TABLE community_health_worker ADD supervisor VARCHAR(255) NULL;

--changeset pmuchowski:1510232903873-3
ALTER TABLE community_health_worker DROP COLUMN peer_supervisor;

