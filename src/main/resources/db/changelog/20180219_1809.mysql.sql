--liquibase formatted sql

--changeset dserkowski:1519060148654-1
ALTER TABLE user MODIFY enabled BIT(1) NOT NULL;

--changeset dserkowski:1519060148654-2
ALTER TABLE user MODIFY password VARCHAR(255) NOT NULL;

