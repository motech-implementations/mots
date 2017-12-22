--liquibase formatted sql

--changeset pmuchowski:2
UPDATE client SET authorized_grant_types = 'password,refresh_token' WHERE client_id = 'trusted-client';