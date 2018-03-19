--liquibase formatted sql

--changeset user:1521477496398-1
ALTER TABLE chiefdom ADD owner_id VARCHAR(255) NOT NULL;

--changeset user:1521477496398-2
ALTER TABLE community ADD owner_id VARCHAR(255) NOT NULL;

--changeset user:1521477496398-3
ALTER TABLE district ADD owner_id VARCHAR(255) NOT NULL;

--changeset user:1521477496398-4
ALTER TABLE facility ADD owner_id VARCHAR(255) NOT NULL;

--changeset user:1521477496398-5
ALTER TABLE community ADD CONSTRAINT FK89wcnl1eeatyb4intp7xqsgkc FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset user:1521477496398-6
ALTER TABLE chiefdom ADD CONSTRAINT FK9us7j26axacucgvpnli9p2s4p FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset user:1521477496398-7
ALTER TABLE facility ADD CONSTRAINT FKj4bbqvvhwk0fy0kqih94i361f FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset user:1521477496398-8
ALTER TABLE district ADD CONSTRAINT FKmqx0dvfgyjfvu55qo4n7e4rmb FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset user:1521477496398-9
ALTER TABLE community_health_worker ALTER selected SET DEFAULT 0;

--changeset user:1521477496398-10
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEFACILITY_ID_COL UNIQUE (facility_id);

