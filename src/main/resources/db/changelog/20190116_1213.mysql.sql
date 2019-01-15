--liquibase formatted sql

--changeset user:1547637201600-1
UPDATE user SET email = NULL WHERE email = '';

--changeset user:1547637201600-2
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEEMAIL_COL UNIQUE (email);

--changeset user:1547637201600-3
ALTER TABLE user ADD CONSTRAINT UC_USEREMAIL_COL UNIQUE (email);
