--liquibase formatted sql

--changeset pmuchowski:1575654579770-1
CREATE TABLE sector (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, owner_id VARCHAR(255) NOT NULL, district_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1575654579770-2
ALTER TABLE district_assignment_log ADD sector_id VARCHAR(255) NULL;

--changeset pmuchowski:1575654579770-3
ALTER TABLE facility ADD sector_id VARCHAR(255) NOT NULL;

--changeset pmuchowski:1575654579770-4
ALTER TABLE sector ADD PRIMARY KEY (id);

--changeset pmuchowski:1575654579770-5
ALTER TABLE facility ADD CONSTRAINT UK3mecgv5hgteuykpjkjjqs8non UNIQUE (name, sector_id);

--changeset pmuchowski:1575654579770-6
ALTER TABLE sector ADD CONSTRAINT UK4cq62q6fbyvpfu2renaxcyq4e UNIQUE (name, district_id);

--changeset pmuchowski:1575654579770-7
ALTER TABLE facility ADD CONSTRAINT FK3mhmpg9vjhkthrqx17wi7jtbr FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset pmuchowski:1575654579770-8
ALTER TABLE sector ADD CONSTRAINT FK9wfuya7l83obc1cnkh6w5yxek FOREIGN KEY (district_id) REFERENCES district (id);

--changeset pmuchowski:1575654579770-9
ALTER TABLE sector ADD CONSTRAINT FKm906rij5n5tmb6s35orduwpji FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset pmuchowski:1575654579770-10
ALTER TABLE district_assignment_log ADD CONSTRAINT FKs3aj1g2mit4gkswjs6lajbjxy FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset pmuchowski:1575654579770-11
ALTER TABLE chiefdom DROP FOREIGN KEY FK1xnj1ersgdj0mi8dskao9oag0;

--changeset pmuchowski:1575654579770-12
ALTER TABLE district_assignment_log DROP FOREIGN KEY FK5us6dg91qr56xhgmm5einv3ge;

--changeset pmuchowski:1575654579770-13
ALTER TABLE chiefdom DROP FOREIGN KEY FK9us7j26axacucgvpnli9p2s4p;

--changeset pmuchowski:1575654579770-14
ALTER TABLE facility DROP FOREIGN KEY FKhs1oq1jxfysj9c3iqu5w91b17;

--changeset pmuchowski:1575654579770-15
ALTER TABLE facility DROP KEY UKmwbp2gvjn52l7u1tn3ke0n0sf;

--changeset pmuchowski:1575654579770-16
ALTER TABLE chiefdom DROP KEY UKpm93uvku1n4hfsdxpr8mg4j40;

--changeset pmuchowski:1575654579770-17
DROP TABLE chiefdom;

--changeset pmuchowski:1575654579770-18
ALTER TABLE district_assignment_log DROP COLUMN chiefdom_id;

--changeset pmuchowski:1575654579770-19
ALTER TABLE facility DROP COLUMN chiefdom_id;

