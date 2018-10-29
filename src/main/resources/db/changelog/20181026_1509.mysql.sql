--liquibase formatted sql

--changeset pmuchowski:1540559356020-1
ALTER TABLE choice ADD `type` VARCHAR(255) DEFAULT 'INCORRECT' NOT NULL;

--changeset pmuchowski:1540559356020-2
UPDATE choice SET `type` = 'CORRECT' WHERE is_correct IS TRUE;

--changeset pmuchowski:1540559356020-3
ALTER TABLE choice DROP COLUMN is_correct;

--changeset pmuchowski:1540559356020-4
ALTER TABLE choice ALTER COLUMN `type` DROP DEFAULT;


