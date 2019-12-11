--liquibase formatted sql

--changeset pmuchowski:1576079885578-1
ALTER TABLE community_health_worker ADD district_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1576079885578-2
ALTER TABLE community_health_worker ADD facility_id VARCHAR(255) NULL;

--changeset pmuchowski:1576079885578-3
ALTER TABLE community_health_worker ADD sector_id VARCHAR(255) NULL;

--changeset pmuchowski:1576079885578-4
ALTER TABLE community_health_worker ADD CONSTRAINT FKf2v6hefppoj0x6twlhjv48xko FOREIGN KEY (district_id) REFERENCES district (id);

--changeset pmuchowski:1576079885578-5
ALTER TABLE community_health_worker ADD CONSTRAINT FKl87uscintaj1cdkos7ha5a56e FOREIGN KEY (facility_id) REFERENCES facility (id);

--changeset pmuchowski:1576079885578-6
ALTER TABLE community_health_worker ADD CONSTRAINT FKtcmmw6l72djiab42udt9xnhly FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset pmuchowski:1576079885578-10
ALTER TABLE community_health_worker MODIFY village_id VARCHAR(255) NULL;

