--liquibase formatted sql

--changeset pmuchowski:1523290490348-1
ALTER TABLE call_detail_record MODIFY call_id VARCHAR(255) NULL;

