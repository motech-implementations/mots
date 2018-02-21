--liquibase formatted sql

--changeset user:1519062056166-1
CREATE TABLE user_log (id VARCHAR(255) NOT NULL, login_date datetime NULL, logout_date datetime NULL, user_id VARCHAR(255) NOT NULL);

--changeset user:1519062056166-2
ALTER TABLE user_log ADD PRIMARY KEY (id);

--changeset user:1519062056166-3
ALTER TABLE user_log ADD CONSTRAINT FKmd6gmr2tvduf9qvif1nchhqfm FOREIGN KEY (user_id) REFERENCES user (id);
