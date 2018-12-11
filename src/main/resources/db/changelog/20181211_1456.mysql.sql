--liquibase formatted sql

--changeset pmuchowski:1544536613162-1
UPDATE `user_permission` SET `display_name` = 'Manage Locations' WHERE `id` = '0f5f40f2-be3d-4509-af61-fa591e504295';

--changeset pmuchowski:1544536613162-2
UPDATE `user_permission` SET `display_name` = 'Manage Own Locations' WHERE `id` = '39773f50-d881-4fc9-bde3-ae6965cbb18c';

--changeset pmuchowski:1544536613162-3
UPDATE `user_permission` SET `display_name` = 'Create Locations' WHERE `id` = '39afbbe4-5854-418b-8ce7-4621980bcdfd';

--changeset pmuchowski:1544536613162-4
UPDATE `user_permission` SET `display_name` = 'Display Locations' WHERE `id` = '6ab183c6-b091-48b1-98f9-869af54dcd1d';

