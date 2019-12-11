--liquibase formatted sql

--changeset pmuchowski:1575954095210-1
CREATE TABLE village (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, owner_id VARCHAR(255) NOT NULL, facility_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1575954095210-2
ALTER TABLE community_health_worker ADD village_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1575954095210-3
ALTER TABLE village ADD PRIMARY KEY (id);

--changeset pmuchowski:1575954095210-4
ALTER TABLE village ADD CONSTRAINT UKkjs7irjjd5qmb89714k34m9i UNIQUE (name, facility_id);

--changeset pmuchowski:1575954095210-5
ALTER TABLE village ADD CONSTRAINT FK3hiaj53dsf3827n64h9ipaftm FOREIGN KEY (facility_id) REFERENCES facility (id);

--changeset pmuchowski:1575954095210-6
ALTER TABLE community_health_worker ADD CONSTRAINT FKlwqg9xaal72wvlk5rnwwjty4y FOREIGN KEY (village_id) REFERENCES village (id);

--changeset pmuchowski:1575954095210-7
ALTER TABLE village ADD CONSTRAINT FKocaecc1m4419r4fdlljj30dfv FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset pmuchowski:1575954095210-8
ALTER TABLE community DROP FOREIGN KEY FK4dntrbp4yli2wdio1i0ermjv8;

--changeset pmuchowski:1575954095210-9
ALTER TABLE community DROP FOREIGN KEY FK89wcnl1eeatyb4intp7xqsgkc;

--changeset pmuchowski:1575954095210-10
ALTER TABLE community_health_worker DROP FOREIGN KEY FKroi9371lxsrgufgva8c4n1scx;

--changeset pmuchowski:1575954095210-11
ALTER TABLE community DROP KEY UKfdsdymj57dhc2piikog8nw3nv;

--changeset pmuchowski:1575954095210-12
DROP TABLE community;

--changeset pmuchowski:1575954095210-13
ALTER TABLE community_health_worker DROP COLUMN community_id;

