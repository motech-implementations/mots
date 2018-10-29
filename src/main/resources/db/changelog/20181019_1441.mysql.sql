--liquibase formatted sql

--changeset pmuchowski:1539952921216-1
CREATE TABLE user_permission (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, display_name VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, readonly BIT DEFAULT 0 NOT NULL);

--changeset pmuchowski:1539952921216-2
ALTER TABLE user_role_permissions ADD permission_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1539952921216-3
ALTER TABLE user_role ADD readonly BIT DEFAULT 0 NOT NULL;

--changeset pmuchowski:1539952921216-4
ALTER TABLE user_role_permissions ADD role_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1539952921216-5
ALTER TABLE user_permission ADD PRIMARY KEY (id);


--changeset pmuchowski:1539952921216-6
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('1f45915b-2fb4-4f9e-be47-af2ac1bb5c81', 'ROLE_CHW_READ', 'Display CHWs', 1);

--changeset pmuchowski:1539952921216-7
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5', 'ROLE_CHW_WRITE', 'Create/Edit CHW', 1);

--changeset pmuchowski:1539952921216-8
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('323e0066-849d-43c3-b1c2-1bd42e205287', 'ROLE_INCHARGE_READ', 'Display Incharges', 1);

--changeset pmuchowski:1539952921216-9
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('a0abf37d-f22d-44ff-9c25-76a12d0722fd', 'ROLE_INCHARGE_WRITE', 'Create/Edit CHW', 1);

--changeset pmuchowski:1539952921216-10
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('2eb6d006-52a0-49db-b5ff-a8beb9464cf2', 'ROLE_MANAGE_MODULES', 'Assign Modules', 1);

--changeset pmuchowski:1539952921216-11
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('9206c12a-9ad8-4082-b1f3-cdda9b2c57df', 'ROLE_DISPLAY_MODULES', 'Display Modules', 1);

--changeset pmuchowski:1539952921216-12
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('0f5f40f2-be3d-4509-af61-fa591e504295', 'ROLE_MANAGE_FACILITIES', 'Manage Modules', 1);

--changeset pmuchowski:1539952921216-13
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('39773f50-d881-4fc9-bde3-ae6965cbb18c', 'ROLE_MANAGE_OWN_FACILITIES', 'Manage Facilities', 1);

--changeset pmuchowski:1539952921216-14
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('39afbbe4-5854-418b-8ce7-4621980bcdfd', 'ROLE_CREATE_FACILITIES', 'Manage Own Facilities', 1);

--changeset pmuchowski:1539952921216-15
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('6ab183c6-b091-48b1-98f9-869af54dcd1d', 'ROLE_DISPLAY_FACILITIES', 'Create Facilities', 1);

--changeset pmuchowski:1539952921216-16
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('8b42ec54-6675-4fd0-901c-d30ad5f41cf7', 'ROLE_ASSIGN_MODULES', 'Display Facilities', 1);

--changeset pmuchowski:1539952921216-17
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('038f5310-9d4a-4fbb-b474-44bb352d51fd', 'ROLE_MANAGE_USERS', 'Manage Users', 1);

--changeset pmuchowski:1539952921216-18
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('0830d85c-be57-4993-b39a-2358f3fee1f9', 'ROLE_MANAGE_INCHARGE_USERS', 'Manage Incharge Users', 1);

--changeset pmuchowski:1539952921216-19
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f', 'ROLE_DISPLAY_REPORTS', 'Display Reports', 1);

--changeset pmuchowski:1539952921216-20
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('1078f9ec-8b05-4db5-b1d1-78a622773983', 'ROLE_UPLOAD_CSV', 'Upload CSV', 1);


--changeset pmuchowski:1539952921216-21
UPDATE `user_role_permissions` SET `role_id` = `user_role_id`;

--changeset pmuchowski:1539952921216-22
UPDATE `user_role_permissions` JOIN `user_permission` ON `name` = CONCAT('ROLE_', `permissions`) SET `permission_id` = `id`;


--changeset pmuchowski:1539952921216-23
ALTER TABLE user_role_permissions ADD PRIMARY KEY (role_id, permission_id);

--changeset pmuchowski:1539952921216-24
ALTER TABLE user_role_permissions ADD CONSTRAINT FK4ol1yjwd29gjey9t05pi704qs FOREIGN KEY (permission_id) REFERENCES user_permission (id);

--changeset pmuchowski:1539952921216-25
ALTER TABLE user_role_permissions ADD CONSTRAINT FKjkt2u4oxtuxwh5ibl4tbpif2u FOREIGN KEY (role_id) REFERENCES user_role (id);

--changeset pmuchowski:1539952921216-26
ALTER TABLE user_role_permissions DROP FOREIGN KEY FKf463dg8s62qhel221m8exfcgx;

--changeset pmuchowski:1539952921216-27
ALTER TABLE user_role_permissions DROP COLUMN permissions;

--changeset pmuchowski:1539952921216-28
ALTER TABLE user_role_permissions DROP COLUMN user_role_id;

