--liquibase formatted sql

--changeset pmuchowski:1522154307371-1
UPDATE community_health_worker SET literacy = 'CANNOT_READ_AND_WRITE' WHERE literacy = 'CANNOT_REAND_AND_WRITE';

