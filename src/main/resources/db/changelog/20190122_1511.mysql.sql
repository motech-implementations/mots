--liquibase formatted sql

--changeset user:1548166300430-1
ALTER TABLE registration_token ADD issue_date datetime NOT NULL DEFAULT NOW();
