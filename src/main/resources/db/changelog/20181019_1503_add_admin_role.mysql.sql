--liquibase formatted sql

--changeset pmuchowski:1539954222933-1
INSERT INTO `user_role` (`id`, `name`, `readonly`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', 'Admin', 1);

--changeset pmuchowski:1539954222933-2
UPDATE `users_roles` SET `role_id` = 'd892e3c6-25eb-4291-abfc-b379aa502bc3' WHERE `user_id` = '51f6bdc1-4932-4bc3-9589-368646ef7ad3';

--changeset pmuchowski:1539954222933-3
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '1f45915b-2fb4-4f9e-be47-af2ac1bb5c81');

--changeset pmuchowski:1539954222933-4
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5');

--changeset pmuchowski:1539954222933-5
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '323e0066-849d-43c3-b1c2-1bd42e205287');

--changeset pmuchowski:1539954222933-6
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', 'a0abf37d-f22d-44ff-9c25-76a12d0722fd');

--changeset pmuchowski:1539954222933-7
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '2eb6d006-52a0-49db-b5ff-a8beb9464cf2');

--changeset pmuchowski:1539954222933-8
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '9206c12a-9ad8-4082-b1f3-cdda9b2c57df');

--changeset pmuchowski:1539954222933-9
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '0f5f40f2-be3d-4509-af61-fa591e504295');

--changeset pmuchowski:1539954222933-10
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '39773f50-d881-4fc9-bde3-ae6965cbb18c');

--changeset pmuchowski:1539954222933-11
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '39afbbe4-5854-418b-8ce7-4621980bcdfd');

--changeset pmuchowski:1539954222933-12
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '6ab183c6-b091-48b1-98f9-869af54dcd1d');

--changeset pmuchowski:1539954222933-13
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '8b42ec54-6675-4fd0-901c-d30ad5f41cf7');

--changeset pmuchowski:1539954222933-14
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '038f5310-9d4a-4fbb-b474-44bb352d51fd');

--changeset pmuchowski:1539954222933-15
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '0830d85c-be57-4993-b39a-2358f3fee1f9');

--changeset pmuchowski:1539954222933-16
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f');

--changeset pmuchowski:1539954222933-17
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '1078f9ec-8b05-4db5-b1d1-78a622773983');

