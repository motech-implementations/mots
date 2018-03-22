--liquibase formatted sql

--changeset pmuchowski:1521709411714-1
ALTER TABLE in_charge ADD selected BIT DEFAULT 0 NOT NULL;

