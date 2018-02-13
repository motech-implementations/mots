--liquibase formatted sql

--changeset user:1518543186750-1
CREATE TABLE jasper_template_supported_formats (jasper_template_id VARCHAR(255) NOT NULL, supported_formats VARCHAR(255) NULL);

--changeset user:1518543186750-2
ALTER TABLE jasper_templates ADD is_displayed BIT(1) DEFAULT 1 NULL;

--changeset user:1518543186750-3
ALTER TABLE jasper_template_supported_formats ADD CONSTRAINT FKjdfliesfcst7d0jutngwynvwa FOREIGN KEY (jasper_template_id) REFERENCES jasper_templates (id);
