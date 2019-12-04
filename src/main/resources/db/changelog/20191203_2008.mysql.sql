--liquibase formatted sql

--changeset pmuchowski:1575400104585-1
ALTER TABLE registration_token_roles DROP FOREIGN KEY FK7yiurjebksujsmmy7ip09weau;

--changeset pmuchowski:1575400104585-2
ALTER TABLE registration_token_roles DROP FOREIGN KEY FKblkn301kco905945b2ut2plrv;

--changeset pmuchowski:1575400104585-3
DROP TABLE registration_token;

--changeset pmuchowski:1575400104585-4
DROP TABLE registration_token_roles;

