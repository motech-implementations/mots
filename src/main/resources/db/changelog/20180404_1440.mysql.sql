--liquibase formatted sql

--changeset user:1522318915375-1
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'UPLOAD_CSV');

