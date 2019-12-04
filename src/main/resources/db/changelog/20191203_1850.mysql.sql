--liquibase formatted sql

--changeset pmuchowski:1575395419091-1
ALTER TABLE facility ADD incharge_full_name VARCHAR(255) NULL;

--changeset pmuchowski:1575395419091-2
ALTER TABLE registration_token ADD name VARCHAR(255) NULL;

--changeset pmuchowski:1575395419091-3
ALTER TABLE in_charge DROP FOREIGN KEY FKam9aggyyufhw0paq2twubw2ty;

--changeset pmuchowski:1575395419091-4
ALTER TABLE registration_token DROP FOREIGN KEY FKkt5vllj4i3ueris6lr6m4nlgt;

--changeset pmuchowski:1575395419091-5
ALTER TABLE in_charge DROP FOREIGN KEY FKsuaql5lw6h05a5kvejb0v5vj6;

--changeset pmuchowski:1575395419091-6
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEEMAIL_COL;

--changeset pmuchowski:1575395419091-7
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;

--changeset pmuchowski:1575395419091-8
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEPHONE_NUMBER_COL;

--changeset pmuchowski:1575395419091-9
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEUSER_ID_COL;

--changeset pmuchowski:1575395419091-10
ALTER TABLE registration_token DROP KEY UC_REGISTRATION_TOKENINCHARGE_ID_COL;

--changeset pmuchowski:1575395419091-11
DROP TABLE in_charge;

--changeset pmuchowski:1575395419091-12
ALTER TABLE registration_token DROP COLUMN incharge_id;

