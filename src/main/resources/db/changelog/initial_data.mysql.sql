--liquibase formatted sql

--changeset pmuchowski:1
INSERT INTO client (client_id, client_secret, authorities, authorized_grant_types,  resource_ids, scope) VALUES ('trusted-client', 'secret', 'TRUSTED_CLIENT', 'password', 'mots', 'read,write');
INSERT INTO `user` (id, enabled, password, username, email) VALUES ('51f6bdc1-4932-4bc3-9589-368646ef7ad3', 1, '$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G', 'admin', 'test@mots.org');
INSERT INTO user_role (id, name, user_id) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'admin', '51f6bdc1-4932-4bc3-9589-368646ef7ad3');
INSERT INTO user_role_permissions (user_role_id, permissions) VALUES ('da5d8474-637d-446d-a221-80de009a6f6c', 'ADMIN');
