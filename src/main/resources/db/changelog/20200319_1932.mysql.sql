--liquibase formatted sql

--changeset pmuchowski:1584642767631-1
ALTER TABLE unit_progress DROP COLUMN current_call_flow_element_number;

