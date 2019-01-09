--liquibase formatted sql

--changeset user:1546865541521-1
CREATE TABLE registration_token (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, email VARCHAR(255) NOT NULL, token VARCHAR(255) NOT NULL, incharge_id VARCHAR(255) NULL);

--changeset user:1546865541521-2
CREATE TABLE registration_token_roles (registration_token_id VARCHAR(255) NOT NULL, role_id VARCHAR(255) NOT NULL);

--changeset user:1546865541521-3
ALTER TABLE registration_token_roles ADD PRIMARY KEY (registration_token_id, role_id);

--changeset user:1546865541521-4
ALTER TABLE registration_token ADD PRIMARY KEY (id);

--changeset user:1546865541521-5
ALTER TABLE registration_token ADD CONSTRAINT UC_REGISTRATION_TOKENINCHARGE_ID_COL UNIQUE (incharge_id);

--changeset user:1546865541521-6
ALTER TABLE registration_token_roles ADD CONSTRAINT FK7yiurjebksujsmmy7ip09weau FOREIGN KEY (role_id) REFERENCES user_role (id);

--changeset user:1546865541521-7
ALTER TABLE registration_token_roles ADD CONSTRAINT FKblkn301kco905945b2ut2plrv FOREIGN KEY (registration_token_id) REFERENCES registration_token (id);

--changeset user:1546865541521-8
ALTER TABLE registration_token ADD CONSTRAINT FKkt5vllj4i3ueris6lr6m4nlgt FOREIGN KEY (incharge_id) REFERENCES in_charge (id);
