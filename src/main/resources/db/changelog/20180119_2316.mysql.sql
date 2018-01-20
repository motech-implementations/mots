--liquibase formatted sql

--changeset chris:1516400209551-1
CREATE TABLE users_roles (user_id VARCHAR(255) NOT NULL, role_id VARCHAR(255) NOT NULL);

--changeset chris:1516400209551-2
ALTER TABLE users_roles ADD PRIMARY KEY (user_id, role_id);

--changeset chris:1516400209551-3
ALTER TABLE users_roles ADD CONSTRAINT FKgd3iendaoyh04b95ykqise6qh FOREIGN KEY (user_id) REFERENCES user (id);

--changeset chris:1516400209551-4
ALTER TABLE users_roles ADD CONSTRAINT FKmknhyioq8hh8seoxe1fy6qo86 FOREIGN KEY (role_id) REFERENCES user_role (id);

--changeset chris:1516400209551-5
ALTER TABLE user_role DROP FOREIGN KEY FK859n2jvi8ivhui0rl0esws6o;

--changeset chris:1516400209551-6
ALTER TABLE user_role DROP COLUMN user_id;
