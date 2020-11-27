--liquibase formatted sql

--changeset user:1606472638313-6
ALTER TABLE facility MODIFY type VARCHAR(255) NULL;
