--liquibase formatted sql

--changeset pmuchowski:1575474181395-1
ALTER TABLE community_health_worker ADD family_name VARCHAR(255) NOT NULL;

--changeset pmuchowski:1575474181395-2
ALTER TABLE community_health_worker DROP COLUMN education_level;

--changeset pmuchowski:1575474181395-3
ALTER TABLE community_health_worker DROP COLUMN has_peer_supervisor;

--changeset pmuchowski:1575474181395-4
ALTER TABLE community_health_worker DROP COLUMN literacy;

--changeset pmuchowski:1575474181395-5
ALTER TABLE community_health_worker DROP COLUMN other_name;

--changeset pmuchowski:1575474181395-6
ALTER TABLE community_health_worker DROP COLUMN second_name;

--changeset pmuchowski:1575474181395-7
ALTER TABLE community_health_worker DROP COLUMN year_of_birth;

--changeset pmuchowski:1575474181395-8
ALTER TABLE community_health_worker MODIFY gender VARCHAR(255) NULL;

