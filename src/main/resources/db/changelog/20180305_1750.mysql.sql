--liquibase formatted sql

--changeset pmuchowski:1520268609090-1
ALTER TABLE course MODIFY name VARCHAR(255) NULL;

