--liquibase formatted sql

--changeset chris:1
UPDATE user_role SET `name` = 'Level 2' WHERE id = 'da5d8474-637d-446d-a221-80de009a6f6c';

--changeset chris:2
DELETE FROM user_role_permissions;

--changeset chris:3
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'MANAGE_USERS');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'MANAGE_MODULES');

INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'CHW_READ');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'CHW_WRITE');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'INCHARGE_READ');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'INCHARGE_WRITE');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'MANAGE_FACILITIES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'ASSIGN_MODULES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'DISPLAY_REPORTS');

--changeset chris:4
INSERT INTO user_role (id, name) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'Level 1');

--changeset chris:5
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'CHW_READ');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'CHW_WRITE');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'INCHARGE_READ');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'INCHARGE_WRITE');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'ASSIGN_MODULES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'DISPLAY_REPORTS');

--changeset chris:6
INSERT INTO user_role (id, name) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'Incharge');

--changeset chris:7
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'DISPLAY_REPORTS');

--changeset chris:8
INSERT INTO users_roles (user_id, role_id) VALUES ('51f6bdc1-4932-4bc3-9589-368646ef7ad3', 'da5d8474-637d-446d-a221-80de009a6f6c');
