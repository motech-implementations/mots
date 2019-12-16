--liquibase formatted sql

--changeset pmuchowski:1576508377213-1
ALTER TABLE facility DROP KEY UC_FACILITYFACILITY_ID_COL;

--changeset pmuchowski:1576508377213-2
ALTER TABLE facility DROP COLUMN facility_id;

--changeset pmuchowski:1576508377213-3
ALTER TABLE facility DROP COLUMN type;

