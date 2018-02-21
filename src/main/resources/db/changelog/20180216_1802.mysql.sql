--liquibase formatted sql

--changeset pmuchowski:1518800578597-1
CREATE TABLE ivr_config_call_status_map (ivr_config_id VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, ivr_status VARCHAR(255) NOT NULL);

--changeset pmuchowski:1518800578597-2
ALTER TABLE ivr_config ADD call_id_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518800578597-3
ALTER TABLE ivr_config ADD call_log_id_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518800578597-4
ALTER TABLE ivr_config ADD call_status_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518800578597-5
ALTER TABLE ivr_config ADD chw_ivr_id_field VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518800578597-6
ALTER TABLE ivr_config ADD name VARCHAR(255) NOT NULL;

--changeset pmuchowski:1518800578597-7
ALTER TABLE ivr_config_call_status_map ADD PRIMARY KEY (ivr_config_id, ivr_status);

--changeset pmuchowski:1518800578597-8
ALTER TABLE ivr_config ADD CONSTRAINT UC_IVR_CONFIGNAME_COL UNIQUE (name);

--changeset pmuchowski:1518800578597-9
ALTER TABLE ivr_config_call_status_map ADD CONSTRAINT FKiv9m5fsgmlmmkny1tkd29ani6 FOREIGN KEY (ivr_config_id) REFERENCES ivr_config (id);

--changeset pmuchowski:1518800578597-10
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEFACILITY_ID_COL UNIQUE (facility_id);

