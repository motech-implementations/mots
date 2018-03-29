--liquibase formatted sql

--changeset user:1522318915373-1
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'UPLOAD_CSV');

