--liquibase formatted sql

--changeset chris:1551188261535-1
ALTER TABLE district_assignment_log ADD facility_id VARCHAR(255) NULL;

--changeset chris:1551188261535-2
ALTER TABLE district_assignment_log ADD CONSTRAINT FK9my0xx8dmq1h8g9q7c0s0gi7w FOREIGN KEY (facility_id) REFERENCES facility (id);
