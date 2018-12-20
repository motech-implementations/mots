--liquibase formatted sql

--changeset pmuchowski:1545275731887-1
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('0bb95c4e-9e1a-40cd-8fa0-9879c419a507', 'ROLE_ADMIN', 'Admin', 1);

--changeset pmuchowski:1545275731887-2
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '0bb95c4e-9e1a-40cd-8fa0-9879c419a507');

