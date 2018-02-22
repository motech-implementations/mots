--liquibase formatted sql

--changeset chris:1519228917015-1
CREATE TABLE district_assignment_log (id VARCHAR(255) NOT NULL, create_date datetime NULL, update_date datetime NULL, end_date date NOT NULL, start_date date NOT NULL, owner_id VARCHAR(255) NOT NULL, district_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL);

--changeset chris:1519228917015-2
ALTER TABLE district_assignment_log ADD PRIMARY KEY (id);

--changeset chris:1519228917015-3
ALTER TABLE district_assignment_log ADD CONSTRAINT FK35xsd0me3xm0km60i9ex4gbwv FOREIGN KEY (district_id) REFERENCES district (id);

--changeset chris:1519228917015-4
ALTER TABLE district_assignment_log ADD CONSTRAINT FKkg6ujxv9xt5m5p5g5840p5x8a FOREIGN KEY (module_id) REFERENCES module (id);

--changeset chris:1519228917015-5
ALTER TABLE district_assignment_log ADD CONSTRAINT FKrn57woe2hf202uok5hrqw4tak FOREIGN KEY (owner_id) REFERENCES user (id);
UPDATE course SET status = "RELEASED" where id='d5c4a806-f817-47e3-b73d-8803a7e76ddd';
