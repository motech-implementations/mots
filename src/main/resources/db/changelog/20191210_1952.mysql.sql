--liquibase formatted sql

--changeset pmuchowski:1576018355248-1
ALTER TABLE facility ADD incharge_email VARCHAR(255) NULL;

--changeset pmuchowski:1576018355248-2
ALTER TABLE facility ADD incharge_phone VARCHAR(255) NULL;

