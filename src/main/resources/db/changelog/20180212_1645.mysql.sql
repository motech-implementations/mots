--liquibase formatted sql

--changeset user:1518450347973-1
CREATE TABLE jasper_template_parameter_options (id VARCHAR(255) NOT NULL, options VARCHAR(255) NULL);

--changeset user:1518450347973-2
CREATE TABLE jasper_templates (id VARCHAR(255) NOT NULL, create_date datetime NULL, update_date datetime NULL, data TINYBLOB NULL, description VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NULL);

--changeset user:1518450347973-3
CREATE TABLE template_parameters (id VARCHAR(255) NOT NULL, create_date datetime NULL, update_date datetime NULL, data_type VARCHAR(255) NULL, default_value VARCHAR(255) NULL, description VARCHAR(255) NULL, display_name VARCHAR(255) NULL, display_property VARCHAR(255) NULL, name VARCHAR(255) NULL, required BIT NOT NULL, select_body VARCHAR(255) NULL, select_expression VARCHAR(255) NULL, select_method VARCHAR(255) NULL, select_property VARCHAR(255) NULL, template_id VARCHAR(255) NOT NULL);

--changeset user:1518450347973-4
ALTER TABLE jasper_templates ADD PRIMARY KEY (id);

--changeset user:1518450347973-5
ALTER TABLE template_parameters ADD PRIMARY KEY (id);

--changeset user:1518450347973-6
ALTER TABLE jasper_templates ADD CONSTRAINT UC_JASPER_TEMPLATESNAME_COL UNIQUE (name);

--changeset user:1518450347973-7
ALTER TABLE jasper_template_parameter_options ADD CONSTRAINT FKf31nhehpvovubnlfm674wg4cd FOREIGN KEY (id) REFERENCES template_parameters (id);

--changeset user:1518450347973-8
ALTER TABLE template_parameters ADD CONSTRAINT FKlyvjfikotvvjaxqgm1g8ylq2v FOREIGN KEY (template_id) REFERENCES jasper_templates (id);

--changeset user:1518450347973-9
ALTER TABLE in_charge DROP KEY UC_IN_CHARGEFACILITY_ID_COL;
ALTER TABLE in_charge ADD CONSTRAINT UC_IN_CHARGEFACILITY_ID_COL UNIQUE (facility_id);

