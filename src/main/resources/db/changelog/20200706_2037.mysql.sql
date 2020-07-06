--liquibase formatted sql

--changeset pmuchowski:1594060656980-1
UPDATE `user` SET password = '{bcrypt}$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G' WHERE id = '51f6bdc1-4932-4bc3-9589-368646ef7ad3';

--changeset pmuchowski:1594060656980-2
UPDATE client SET client_secret = '{noop}secret' WHERE client_id = 'trusted-client';

