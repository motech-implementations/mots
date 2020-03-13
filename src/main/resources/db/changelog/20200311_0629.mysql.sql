--liquibase formatted sql

--changeset pmuchowski:1583904572015-1
ALTER TABLE ivr_config ADD main_menu_tree_id VARCHAR(255) NULL;

