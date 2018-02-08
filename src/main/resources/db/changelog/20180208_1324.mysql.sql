--liquibase formatted sql

--changeset chris:1518092681236-1
ALTER TABLE facility ADD facility_id VARCHAR(255) NOT NULL;

--changeset chris:1518092681236-2
ALTER TABLE facility ADD CONSTRAINT UC_FACILITYFACILITY_ID_COL UNIQUE (facility_id);

--changeset chris:1518092681236-3
ALTER TABLE facility MODIFY type VARCHAR(255) NOT NULL;
