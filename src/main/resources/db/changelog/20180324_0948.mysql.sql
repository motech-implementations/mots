--liquibase formatted sql

--changeset user:1521881316032-1
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('a894d04f-a653-4745-bf11-f76e3cfb228b', 'MANAGE_INCHARGE_USERS');
