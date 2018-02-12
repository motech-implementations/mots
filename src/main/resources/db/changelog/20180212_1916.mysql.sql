--liquibase formatted sql

--changeset chris:1518459393910-1
CREATE TABLE call_detail_record (id VARCHAR(255) NOT NULL, create_date datetime NULL, update_date datetime NULL);

--changeset chris:1518459393910-2
CREATE TABLE call_detail_record_data (cdr_id VARCHAR(255) NOT NULL, value VARCHAR(255) NULL, name VARCHAR(255) NOT NULL);

--changeset chris:1518459393910-3
ALTER TABLE call_detail_record_data ADD PRIMARY KEY (cdr_id, name);

--changeset chris:1518459393910-4
ALTER TABLE call_detail_record ADD PRIMARY KEY (id);

--changeset chris:1518459393910-5
ALTER TABLE call_detail_record_data ADD CONSTRAINT FK5xvgp1vq6d7ym79he9qb54s91 FOREIGN KEY (cdr_id) REFERENCES call_detail_record (id);
