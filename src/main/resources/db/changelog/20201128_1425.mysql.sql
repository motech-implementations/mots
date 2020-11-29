--liquibase formatted sql

--changeset pmuchowski:1606703195522-5
ALTER TABLE call_detail_record ADD call_duration VARCHAR(255) NULL;

--changeset pmuchowski:1606703195522-6
ALTER TABLE ivr_config ADD call_duration_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-7
ALTER TABLE call_detail_record ADD call_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-8
ALTER TABLE ivr_config ADD call_id_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-9
ALTER TABLE call_detail_record ADD call_status_reason VARCHAR(255) NULL;

--changeset pmuchowski:1606703195522-10
ALTER TABLE ivr_config ADD call_status_reason_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-11
ALTER TABLE ivr_config ADD channel VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-12
ALTER TABLE call_detail_record ADD chw_phone VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-13
ALTER TABLE ivr_config ADD chw_phone_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-14
ALTER TABLE ivr_config ADD project_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1606703195522-15
ALTER TABLE call_detail_record DROP COLUMN call_log_id;

--changeset pmuchowski:1606703195522-16
ALTER TABLE ivr_config DROP COLUMN call_log_id_field;

--changeset pmuchowski:1606703195522-17
ALTER TABLE call_detail_record DROP COLUMN chw_ivr_id;

--changeset pmuchowski:1606703195522-18
ALTER TABLE ivr_config DROP COLUMN chw_ivr_id_field;

--changeset pmuchowski:1606703195522-19
ALTER TABLE ivr_config DROP COLUMN default_users_group_id;

--changeset pmuchowski:1606703195522-20
ALTER TABLE ivr_config DROP COLUMN detect_voicemail_action;

--changeset pmuchowski:1606703195522-21
ALTER TABLE call_detail_record DROP COLUMN incoming_call_id;

--changeset pmuchowski:1606703195522-22
ALTER TABLE ivr_config DROP COLUMN incoming_call_id_field;

--changeset pmuchowski:1606703195522-23
ALTER TABLE ivr_config DROP COLUMN main_menu_tree_id;

--changeset pmuchowski:1606703195522-24
ALTER TABLE call_detail_record DROP COLUMN outgoing_call_id;

--changeset pmuchowski:1606703195522-25
ALTER TABLE ivr_config DROP COLUMN outgoing_call_id_field;

--changeset pmuchowski:1606703195522-26
ALTER TABLE ivr_config DROP COLUMN retry_attempts_long;

--changeset pmuchowski:1606703195522-27
ALTER TABLE ivr_config DROP COLUMN retry_attempts_short;

--changeset pmuchowski:1606703195522-28
ALTER TABLE ivr_config DROP COLUMN retry_delay_long;

--changeset pmuchowski:1606703195522-29
ALTER TABLE ivr_config DROP COLUMN retry_delay_short;

--changeset pmuchowski:1606703195522-30
ALTER TABLE ivr_config DROP COLUMN send_sms_if_voice_fails;

--changeset pmuchowski:1606703195522-31
ALTER TABLE ivr_config ADD send_module_assignment_message BIT NOT NULL;

--changeset pmuchowski:1606703195522-1
ALTER TABLE community_health_worker MODIFY preferred_language VARCHAR(255) NULL;

