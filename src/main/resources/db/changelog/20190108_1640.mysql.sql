--liquibase formatted sql

--changeset user:1546962049347-1
ALTER TABLE in_charge ADD user_id VARCHAR(255) NULL;

--changeset user:1546962049347-2
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEUSER_ID_COL UNIQUE (user_id);

--changeset user:1546962049347-3
ALTER TABLE in_charge ADD CONSTRAINT FKam9aggyyufhw0paq2twubw2ty FOREIGN KEY (user_id) REFERENCES user (id);
