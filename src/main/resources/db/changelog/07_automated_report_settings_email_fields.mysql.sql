--liquibase formatted sql

--changeset kdondziak:1605022978131-5
ALTER TABLE automated_report_settings ADD message_body LONGTEXT NULL;

ALTER TABLE automated_report_settings ADD message_subject VARCHAR(255) NOT NULL;

