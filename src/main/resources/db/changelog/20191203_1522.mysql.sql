--liquibase formatted sql

--changeset pmuchowski:1575382955679-1
DELETE FROM `user_role_permissions` WHERE permission_id = '0830d85c-be57-4993-b39a-2358f3fee1f9';
DELETE FROM `user_permission` WHERE id = '0830d85c-be57-4993-b39a-2358f3fee1f9';

--changeset pmuchowski:1575382955679-2
DELETE FROM `user_role_permissions` WHERE permission_id = '323e0066-849d-43c3-b1c2-1bd42e205287';
DELETE FROM `user_permission` WHERE id = '323e0066-849d-43c3-b1c2-1bd42e205287';

--changeset pmuchowski:1575382955679-3
DELETE FROM `user_role_permissions` WHERE permission_id = 'a0abf37d-f22d-44ff-9c25-76a12d0722fd';
DELETE FROM `user_permission` WHERE id = 'a0abf37d-f22d-44ff-9c25-76a12d0722fd';

--changeset pmuchowski:1575382955679-4
DELETE FROM `user_role_permissions` WHERE role_id = '1392c925-c7d8-43a9-9ee6-fb93022d8d20';
DELETE FROM `user_role` WHERE id = '1392c925-c7d8-43a9-9ee6-fb93022d8d20';

--changeset pmuchowski:1575382955679-5
UPDATE `user_permission` SET `name` = 'ROLE_UPLOAD_CHW_CSV', `display_name` = 'Upload CHW CSV' WHERE `id` = '1078f9ec-8b05-4db5-b1d1-78a622773983';

--changeset pmuchowski:1575382955679-6
DELETE FROM `template_parameters` WHERE template_id = '27e5a4e4-9b6b-4a9d-8a5c-7f378dba2635';
DELETE FROM `jasper_templates` WHERE id = '27e5a4e4-9b6b-4a9d-8a5c-7f378dba2635';

--changeset pmuchowski:1575382955679-7
DELETE FROM `template_parameters` WHERE template_id = '4b757b57-78e8-4072-b032-4af142be8c5e';
DELETE FROM `jasper_templates` WHERE id = '4b757b57-78e8-4072-b032-4af142be8c5e';

