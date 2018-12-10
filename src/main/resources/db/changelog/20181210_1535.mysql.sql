--liquibase formatted sql

--changeset pmuchowski:1544452543690-1
UPDATE `user_permission` SET `display_name` = 'Create/Edit Incharge' WHERE `id` = 'a0abf37d-f22d-44ff-9c25-76a12d0722fd';

--changeset pmuchowski:1544452543690-2
UPDATE `user_permission` SET `display_name` = 'Manage Modules' WHERE `id` = '2eb6d006-52a0-49db-b5ff-a8beb9464cf2';

--changeset pmuchowski:1544452543690-3
UPDATE `user_permission` SET `display_name` = 'Manage Facilities' WHERE `id` = '0f5f40f2-be3d-4509-af61-fa591e504295';

--changeset pmuchowski:1544452543690-4
UPDATE `user_permission` SET `display_name` = 'Manage Own Facilities' WHERE `id` = '39773f50-d881-4fc9-bde3-ae6965cbb18c';

--changeset pmuchowski:1544452543690-5
UPDATE `user_permission` SET `display_name` = 'Create Facilities' WHERE `id` = '39afbbe4-5854-418b-8ce7-4621980bcdfd';

--changeset pmuchowski:1544452543690-6
UPDATE `user_permission` SET `display_name` = 'Display Facilities' WHERE `id` = '6ab183c6-b091-48b1-98f9-869af54dcd1d';

--changeset pmuchowski:1544452543690-7
UPDATE `user_permission` SET `display_name` = 'Assign Modules' WHERE `id` = '8b42ec54-6675-4fd0-901c-d30ad5f41cf7';

