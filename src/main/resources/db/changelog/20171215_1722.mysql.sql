--liquibase formatted sql

--changeset chris:1513354967003-1
ALTER TABLE in_charge ADD first_name VARCHAR(255) NOT NULL;

--changeset chris:1513354967003-2
ALTER TABLE in_charge ADD other_name VARCHAR(255) NULL;

--changeset chris:1513354967003-3
ALTER TABLE in_charge ADD second_name VARCHAR(255) NOT NULL;

--changeset chris:1513354967003-4
ALTER TABLE in_charge DROP COLUMN name;

--changeset chris:1513354967003-5
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEFACILITY_ID_COL UNIQUE (facility_id);

