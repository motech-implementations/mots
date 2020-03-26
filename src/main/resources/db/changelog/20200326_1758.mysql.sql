--liquibase formatted sql

--changeset pmuchowski:1585256308749-1
ALTER TABLE call_detail_record ADD outgoing_call_id VARCHAR(255) NULL;

--changeset pmuchowski:1585256308749-2
ALTER TABLE call_detail_record CHANGE `call_id` `incoming_call_id` VARCHAR(255) NULL;

--changeset pmuchowski:1585256308749-3
ALTER TABLE ivr_config ADD outgoing_call_id_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1585256308749-4
ALTER TABLE ivr_config CHANGE `call_id_field` `incoming_call_id_field` VARCHAR(255) NOT NULL;

