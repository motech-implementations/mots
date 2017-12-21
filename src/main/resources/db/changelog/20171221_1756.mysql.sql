--liquibase formatted sql

--changeset chris:1513875397081-1
ALTER TABLE community ADD CONSTRAINT UKfdsdymj57dhc2piikog8nw3nv UNIQUE (name, facility_id);

--changeset chris:1513875397081-2
ALTER TABLE facility ADD CONSTRAINT UKmwbp2gvjn52l7u1tn3ke0n0sf UNIQUE (name, chiefdom_id);

--changeset chris:1513875397081-3
ALTER TABLE chiefdom ADD CONSTRAINT UKpm93uvku1n4hfsdxpr8mg4j40 UNIQUE (name, district_id);

--changeset chris:1513875397081-4
ALTER TABLE chiefdom DROP KEY UK_t2149oqguejxnd84goin33pmy;

--changeset chris:1513875397081-5
ALTER TABLE facility MODIFY chiefdom_id VARCHAR(255) NOT NULL;

--changeset chris:1513875397081-6
ALTER TABLE chiefdom MODIFY district_id VARCHAR(255) NOT NULL;

--changeset chris:1513875397081-7
ALTER TABLE community MODIFY facility_id VARCHAR(255) NOT NULL;

--changeset chris:1513875397081-8
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEFACILITY_ID_COL UNIQUE (facility_id);

