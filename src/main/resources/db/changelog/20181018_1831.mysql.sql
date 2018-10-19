--liquibase formatted sql

--changeset user:1539880272796-1
ALTER TABLE district_assignment_log ADD chiefdom_id VARCHAR(255) NULL;

--changeset user:1539880272796-2
ALTER TABLE district_assignment_log ADD CONSTRAINT FK5us6dg91qr56xhgmm5einv3ge FOREIGN KEY (chiefdom_id) REFERENCES chiefdom (id);
