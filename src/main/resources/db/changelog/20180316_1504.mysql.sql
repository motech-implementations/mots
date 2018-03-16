--liquibase formatted sql

--changeset chris:1521209134284-1
ALTER TABLE community_health_worker ADD selected BIT DEFAULT 0 NOT NULL;

--changeset chris:1521209134284-2
ALTER TABLE community_health_worker MODIFY phone_number VARCHAR(255) NULL;
