--liquibase formatted sql

--changeset pmuchowski:1550061695981-1
CREATE TABLE chw_group (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL);

--changeset pmuchowski:1550061695981-2
ALTER TABLE community_health_worker ADD group_id VARCHAR(255) NULL;

--changeset pmuchowski:1550061695981-3
ALTER TABLE chw_group ADD PRIMARY KEY (id);

--changeset pmuchowski:1550061695981-4
ALTER TABLE chw_group ADD CONSTRAINT UC_CHW_GROUPNAME_COL UNIQUE (name);

--changeset pmuchowski:1550061695981-5
ALTER TABLE community_health_worker DROP COLUMN working;

--changeset pmuchowski:1550061695981-6
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('3f273730-a2c3-4208-adaf-a8839b78655e', 'ROLE_GROUP_READ', 'Display Groups', 1);
INSERT INTO `user_permission` (`id`, `name`, `display_name`, `readonly`) VALUES ('adc5e376-5993-4699-9d09-5a415b9329fe', 'ROLE_GROUP_WRITE', 'Create/Edit Group', 1);

--changeset pmuchowski:1550061695981-7
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', '3f273730-a2c3-4208-adaf-a8839b78655e');
INSERT INTO `user_role_permissions` (`role_id`, `permission_id`) VALUES ('d892e3c6-25eb-4291-abfc-b379aa502bc3', 'adc5e376-5993-4699-9d09-5a415b9329fe');

--changeset pmuchowski:1550061695981-8
ALTER TABLE community_health_worker ADD CONSTRAINT FKb7ajufa5sqcprhp93rd1aimox FOREIGN KEY (group_id) REFERENCES chw_group (id);

