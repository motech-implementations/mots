--liquibase formatted sql

--changeset pmuchowski:1522087566313-1
ALTER TABLE community_health_worker ADD working BIT NULL;

