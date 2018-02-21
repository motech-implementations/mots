--liquibase formatted sql

--changeset pmuchowski:1518878781659-1
ALTER TABLE ivr_config ADD voto_main_tree_id VARCHAR(255) NOT NULL;

