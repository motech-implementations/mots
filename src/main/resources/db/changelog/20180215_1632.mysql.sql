--liquibase formatted sql

--changeset dserkowski:1518708776096-1
CREATE TABLE ivr_config (id VARCHAR(255) NOT NULL, create_date datetime NULL, update_date datetime NULL, base_url VARCHAR(255) NOT NULL, default_users_group_id VARCHAR(255) NOT NULL, detect_voicemail_action BIT NOT NULL, module_assigned_message_id VARCHAR(255) NOT NULL, retry_attempts_long INT NOT NULL, retry_attempts_short INT NOT NULL, retry_delay_long INT NOT NULL, retry_delay_short INT NOT NULL, send_sms_if_voice_fails BIT NOT NULL);

--changeset dserkowski:1518708776096-2
CREATE TABLE ivr_config_languages (ivr_config_id VARCHAR(255) NOT NULL, ivr_language_id VARCHAR(255) NOT NULL, language VARCHAR(255) NOT NULL);

--changeset dserkowski:1518708776096-3
ALTER TABLE ivr_config_languages ADD PRIMARY KEY (ivr_config_id, language);

--changeset dserkowski:1518708776096-4
ALTER TABLE ivr_config ADD PRIMARY KEY (id);

--changeset dserkowski:1518708776096-5
ALTER TABLE ivr_config_languages ADD CONSTRAINT FKpf7yd3nqoj629q8gbu95o6yrg FOREIGN KEY (ivr_config_id) REFERENCES ivr_config (id);

