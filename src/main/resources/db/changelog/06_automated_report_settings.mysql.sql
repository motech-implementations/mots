--liquibase formatted sql

--changeset kdondziak:1605000841282-6
CREATE TABLE automated_report_settings (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, emails LONGTEXT NULL, enabled BIT DEFAULT 0 NOT NULL, interval_in_seconds INT NOT NULL, job_name VARCHAR(255) NOT NULL, start_date datetime NULL, CONSTRAINT automated_report_settingsPK PRIMARY KEY (id));

ALTER TABLE automated_report_settings ADD CONSTRAINT UKcgvum9vwgcqb0sr2pv7lxrxks UNIQUE (job_name);

INSERT INTO automated_report_settings (id, created_date, updated_date, job_name, enabled, interval_in_seconds) VALUES
('2f822f94-836e-4ab8-978a-0d710e141160', '2020-10-02 10:09:12.000', '2020-10-02 10:09:12.000', 'Automated report', 0, 86400); # 86400 = 1 day

INSERT INTO user_permission (id,created_date,updated_date,display_name,name,readonly) VALUES
('cb706673-349b-4c76-82e7-bfa6303693eb',NULL,NULL,'Manage automated reports','AUTOMATED_REPORT_AUTHORITY',1);

INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('cb706673-349b-4c76-82e7-bfa6303693eb','d892e3c6-25eb-4291-abfc-b379aa502bc3');