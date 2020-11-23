--liquibase formatted sql

--changeset kdondziak:1606133616829-5
ALTER TABLE automated_report_settings ADD period VARCHAR(255) DEFAULT 'MONTHLY' NOT NULL;

ALTER TABLE automated_report_settings DROP COLUMN interval_in_seconds;

ALTER TABLE automated_report_settings ADD template_id VARCHAR(255) NOT NULL;

UPDATE automated_report_settings set template_id = '07623ae5-bb3d-4a15-9601-89b06ca38694' WHERE id = '2f822f94-836e-4ab8-978a-0d710e141160';

INSERT INTO template_parameters (id, created_date, updated_date, data_type, default_value, description, display_name, name, required, template_id)
VALUES('07623ae5-bb3d-4a15-9601-89b06ca38691', NULL, NULL, 'Image', NULL, NULL, 'Logo', 'logo', 0, '07623ae5-bb3d-4a15-9601-89b06ca38694');
