--liquibase formatted sql

--changeset pmuchowski:1608105619904-1
INSERT INTO user_permission (id,created_date,updated_date,display_name,name,readonly) VALUES
('376555b2-fd60-4cd2-bcf9-d1224c6ce2f5',NULL,NULL,'Manage Roles','ROLE_MANAGE_ROLES',1);

--changeset pmuchowski:1608105619904-2
INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('376555b2-fd60-4cd2-bcf9-d1224c6ce2f5','d892e3c6-25eb-4291-abfc-b379aa502bc3');

