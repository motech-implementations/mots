--liquibase formatted sql

--changeset pmuchowski:1519696393484-1
ALTER TABLE module_progress ADD end_date datetime NULL;

--changeset pmuchowski:1519696393484-2
ALTER TABLE module_progress ADD start_date datetime NULL;

