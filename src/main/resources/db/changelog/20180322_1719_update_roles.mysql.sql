--liquibase formatted sql

--changeset user:1521735578164-1
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'CREATE_FACILITIES');

--changeset user:1521735578164-2
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'CREATE_FACILITIES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'MANAGE_OWN_FACILITIES');

