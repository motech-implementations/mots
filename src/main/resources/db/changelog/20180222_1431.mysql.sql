--liquibase formatted sql

--changeset pmuchowski:1519378275766-1
ALTER TABLE unit_progress MODIFY current_call_flow_element_number INT NOT NULL;

--changeset pmuchowski:1519378275766-2
ALTER TABLE module_progress MODIFY current_unit_number INT NOT NULL;

--changeset pmuchowski:1519378275766-3
ALTER TABLE question_response MODIFY response_id VARCHAR(255) NULL;

