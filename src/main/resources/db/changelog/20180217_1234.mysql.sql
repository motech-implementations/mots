--liquibase formatted sql

--changeset chris:1518867295312-1
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'DISPLAY_MODULES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'DISPLAY_FACILITIES');

INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'DISPLAY_MODULES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'DISPLAY_FACILITIES');

INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'DISPLAY_MODULES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'DISPLAY_FACILITIES');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'INCHARGE_READ');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('1392c925-c7d8-43a9-9ee6-fb93022d8d20', 'CHW_READ');



