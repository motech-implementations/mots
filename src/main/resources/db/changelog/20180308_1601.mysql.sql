--liquibase formatted sql

--changeset pmuchowski:1520521289534-2
ALTER TABLE ivr_config DROP COLUMN voto_main_tree_id;

