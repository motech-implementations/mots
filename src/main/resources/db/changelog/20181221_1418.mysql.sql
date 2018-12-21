--liquibase formatted sql

--changeset pmuchowski:1545398308048-1
UPDATE `user_permission` SET `name` = 'ROLE_UPLOAD_CHW_OR_INCHARGE_CSV', `display_name` = 'Upload CHW or Incharge CSV' WHERE `id` = '1078f9ec-8b05-4db5-b1d1-78a622773983';

--changeset pmuchowski:1545398308048-2
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('86a7adf7-8287-4233-972b-05662444bfa7', 'ROLE_UPLOAD_LOCATION_CSV', 'Upload Location CSV', 1);

--changeset pmuchowski:1545398308048-3
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '86a7adf7-8287-4233-972b-05662444bfa7');

