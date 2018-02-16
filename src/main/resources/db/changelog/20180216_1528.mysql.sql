--liquibase formatted sql

--changeset pmuchowski:1518791293418-1
ALTER TABLE call_detail_record ADD call_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-2
ALTER TABLE call_detail_record ADD call_log_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-3
ALTER TABLE call_detail_record ADD call_status VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-4
ALTER TABLE module_progress ADD chw_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-5
ALTER TABLE call_detail_record ADD chw_ivr_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-6
ALTER TABLE unit_progress ADD current_call_flow_element_number INT NULL;

--changeset pmuchowski:1518791293418-7
ALTER TABLE module_progress ADD current_unit_number INT NULL;

--changeset pmuchowski:1518791293418-8
ALTER TABLE module_progress ADD interrupted BIT NOT NULL;

--changeset pmuchowski:1518791293418-9
ALTER TABLE call_detail_record ADD ivr_config_name VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-10
ALTER TABLE module_progress ADD CONSTRAINT FKaj61x09ycr36qng40hwpww7lu FOREIGN KEY (chw_id) REFERENCES community_health_worker (id);

--changeset pmuchowski:1518791293418-11
ALTER TABLE module_progress DROP FOREIGN KEY FK8enh3u2aqu6ull7y1voefavj9;

--changeset pmuchowski:1518791293418-12
ALTER TABLE unit_progress DROP FOREIGN KEY FKk5y5mkg4jpy4mfgw2amllmc9j;

--changeset pmuchowski:1518791293418-13
ALTER TABLE unit_progress DROP COLUMN current_call_flow_id;

--changeset pmuchowski:1518791293418-14
ALTER TABLE module_progress DROP COLUMN current_unit_id;

--changeset pmuchowski:1518791293418-15
ALTER TABLE module_progress MODIFY module_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-16
ALTER TABLE question_response MODIFY question_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-17
ALTER TABLE question_response MODIFY response_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518791293418-18
ALTER TABLE unit_progress MODIFY unit_id VARCHAR(255) NOT NULL;

