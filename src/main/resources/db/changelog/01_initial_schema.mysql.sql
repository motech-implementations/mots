--liquibase formatted sql

--changeset kdondziak:1600078835283-1
CREATE TABLE assigned_course (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, end_date datetime NULL, start_date datetime NOT NULL, course_id VARCHAR(255) NULL, health_worker_id VARCHAR(255) NULL, CONSTRAINT assigned_coursePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-2
CREATE TABLE assigned_modules (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, health_worker_id VARCHAR(255) NULL, CONSTRAINT assigned_modulesPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-3
CREATE TABLE call_detail_record (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, call_log_id VARCHAR(255) NOT NULL, call_status VARCHAR(255) NOT NULL, chw_ivr_id VARCHAR(255) NOT NULL, incoming_call_id VARCHAR(255) NULL, ivr_config_name VARCHAR(255) NOT NULL, outgoing_call_id VARCHAR(255) NULL, CONSTRAINT call_detail_recordPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-4
CREATE TABLE call_detail_record_data (cdr_id VARCHAR(255) NOT NULL, value VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, CONSTRAINT PK_CALL_DETAIL_RECORD_DATA PRIMARY KEY (cdr_id, name));

--changeset kdondziak:1600078835283-5
CREATE TABLE call_flow_element (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, content LONGTEXT NULL, list_order INT NOT NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, unit_id VARCHAR(255) NULL, CONSTRAINT call_flow_elementPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-6
CREATE TABLE call_flow_element_log (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, end_date datetime NULL, start_date datetime NULL, call_flow_element_id VARCHAR(255) NOT NULL, unit_progress_id VARCHAR(255) NOT NULL, CONSTRAINT call_flow_element_logPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-7
CREATE TABLE choice (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, choice_id INT NOT NULL, `description` LONGTEXT NULL, ivr_name VARCHAR(255) NULL, type VARCHAR(255) NOT NULL, question_id VARCHAR(255) NULL, CONSTRAINT choicePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-8
CREATE TABLE chw_group (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, CONSTRAINT chw_groupPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-9
CREATE TABLE client (client_id VARCHAR(255) NOT NULL, access_token_validity_seconds INT NULL, additional_information VARCHAR(255) NULL, authorities VARCHAR(255) NOT NULL, authorized_grant_types VARCHAR(255) NOT NULL, client_secret VARCHAR(255) NULL, refresh_token_validity_seconds INT NULL, registered_redirect_uris VARCHAR(255) NULL, resource_ids VARCHAR(255) NULL, scope VARCHAR(255) NULL, CONSTRAINT clientPK PRIMARY KEY (client_id));

--changeset kdondziak:1600078835283-10
CREATE TABLE community_health_worker (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, chw_id VARCHAR(255) NOT NULL, chw_name VARCHAR(255) NOT NULL, gender VARCHAR(255) NULL, ivr_id VARCHAR(255) NULL, phone_number VARCHAR(255) NULL, preferred_language VARCHAR(255) NOT NULL, selected BIT DEFAULT 0 NOT NULL, district_id VARCHAR(255) NOT NULL, facility_id VARCHAR(255) NULL, group_id VARCHAR(255) NULL, sector_id VARCHAR(255) NULL, village_id VARCHAR(255) NULL, CONSTRAINT community_health_workerPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-11
CREATE TABLE course (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, choose_module_question_ivr_id VARCHAR(255) NULL, `description` LONGTEXT NULL, menu_intro_message_ivr_id VARCHAR(255) NULL, name VARCHAR(255) NULL, no_modules_message_ivr_id VARCHAR(255) NULL, status VARCHAR(255) NOT NULL, version INT NOT NULL, previous_version_id VARCHAR(255) NULL, CONSTRAINT coursePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-12
CREATE TABLE course_module (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, list_order INT NOT NULL, start_module_question_ivr_id VARCHAR(255) NULL, course_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL, previous_version_id VARCHAR(255) NULL, CONSTRAINT course_modulePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-13
CREATE TABLE district (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, ivr_group_id VARCHAR(255) NULL, owner_id VARCHAR(255) NOT NULL, CONSTRAINT districtPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-14
CREATE TABLE district_assignment_log (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, owner_id VARCHAR(255) NOT NULL, district_id VARCHAR(255) NULL, facility_id VARCHAR(255) NULL, group_id VARCHAR(255) NULL, module_id VARCHAR(255) NOT NULL, sector_id VARCHAR(255) NULL, CONSTRAINT district_assignment_logPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-15
CREATE TABLE facility (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, incharge_email VARCHAR(255) NULL, incharge_full_name VARCHAR(255) NULL, incharge_phone VARCHAR(255) NULL, owner_id VARCHAR(255) NOT NULL, sector_id VARCHAR(255) NOT NULL, CONSTRAINT facilityPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-16
CREATE TABLE ivr_config (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, base_url VARCHAR(255) NOT NULL, call_log_id_field VARCHAR(255) NOT NULL, call_status_field VARCHAR(255) NOT NULL, chw_ivr_id_field VARCHAR(255) NOT NULL, default_users_group_id VARCHAR(255) NOT NULL, detect_voicemail_action BIT NOT NULL, incoming_call_id_field VARCHAR(255) NOT NULL, main_menu_tree_id VARCHAR(255) NULL, module_assigned_message_id VARCHAR(255) NOT NULL, name VARCHAR(255) NOT NULL, outgoing_call_id_field VARCHAR(255) NOT NULL, retry_attempts_long INT NOT NULL, retry_attempts_short INT NOT NULL, retry_delay_long INT NOT NULL, retry_delay_short INT NOT NULL, send_sms_if_voice_fails BIT NOT NULL, CONSTRAINT ivr_configPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-17
CREATE TABLE ivr_config_call_status_map (ivr_config_id VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, ivr_status VARCHAR(255) NOT NULL, CONSTRAINT PK_IVR_CONFIG_CALL_STATUS_MAP PRIMARY KEY (ivr_config_id, ivr_status));

--changeset kdondziak:1600078835283-18
CREATE TABLE ivr_config_languages (ivr_config_id VARCHAR(255) NOT NULL, ivr_language_id VARCHAR(255) NOT NULL, language VARCHAR(255) NOT NULL, CONSTRAINT PK_IVR_CONFIG_LANGUAGES PRIMARY KEY (ivr_config_id, language));

--changeset kdondziak:1600078835283-19
CREATE TABLE jasper_template_parameter_options (id VARCHAR(255) NOT NULL, options VARCHAR(255) NULL);

--changeset kdondziak:1600078835283-20
CREATE TABLE jasper_template_supported_formats (jasper_template_id VARCHAR(255) NOT NULL, supported_formats VARCHAR(255) NULL);

--changeset kdondziak:1600078835283-21
CREATE TABLE jasper_templates (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, data MEDIUMBLOB NULL, `description` VARCHAR(255) NULL, json_output LONGTEXT NULL, json_output_version BIGINT NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NULL, `visible` BIT NULL, CONSTRAINT jasper_templatesPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-22
CREATE TABLE message (call_flow_element_id VARCHAR(255) NOT NULL, CONSTRAINT messagePK PRIMARY KEY (call_flow_element_id));

--changeset kdondziak:1600078835283-23
CREATE TABLE message_log (call_flow_element_log_id VARCHAR(255) NOT NULL, CONSTRAINT message_logPK PRIMARY KEY (call_flow_element_log_id));

--changeset kdondziak:1600078835283-24
CREATE TABLE module (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, `description` LONGTEXT NULL, ivr_group VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, name_code VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, version INT NOT NULL, previous_version_id VARCHAR(255) NULL, CONSTRAINT modulePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-25
CREATE TABLE module_assignment (assigned_modules_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL, CONSTRAINT PK_MODULE_ASSIGNMENT PRIMARY KEY (assigned_modules_id, module_id));

--changeset kdondziak:1600078835283-26
CREATE TABLE module_progress (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, current_unit_number INT NOT NULL, end_date datetime NULL, interrupted BIT NOT NULL, start_date datetime NULL, status VARCHAR(255) NOT NULL, chw_id VARCHAR(255) NOT NULL, course_module_id VARCHAR(255) NOT NULL, CONSTRAINT module_progressPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-27
CREATE TABLE multiple_choice_question (question_type VARCHAR(255) NOT NULL, call_flow_element_id VARCHAR(255) NOT NULL, CONSTRAINT multiple_choice_questionPK PRIMARY KEY (call_flow_element_id));

--changeset kdondziak:1600078835283-28
CREATE TABLE multiple_choice_question_log (number_of_attempts INT NOT NULL, call_flow_element_log_id VARCHAR(255) NOT NULL, response_id VARCHAR(255) NULL, CONSTRAINT multiple_choice_question_logPK PRIMARY KEY (call_flow_element_log_id));

--changeset kdondziak:1600078835283-29
CREATE TABLE sector (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, owner_id VARCHAR(255) NOT NULL, district_id VARCHAR(255) NOT NULL, CONSTRAINT sectorPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-30
CREATE TABLE template_parameters (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, data_type VARCHAR(255) NULL, default_value VARCHAR(255) NULL, `description` VARCHAR(255) NULL, display_name VARCHAR(255) NULL, name VARCHAR(255) NULL, required BIT NOT NULL, template_id VARCHAR(255) NOT NULL, CONSTRAINT template_parametersPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-31
CREATE TABLE unit (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, allow_replay BIT NOT NULL, continuation_question_ivr_id VARCHAR(255) NULL, `description` LONGTEXT NULL, list_order INT NOT NULL, name VARCHAR(255) NOT NULL, module_id VARCHAR(255) NULL, CONSTRAINT unitPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-32
CREATE TABLE unit_progress (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, number_of_replays INT NOT NULL, status VARCHAR(255) NOT NULL, module_progress_id VARCHAR(255) NOT NULL, unit_id VARCHAR(255) NOT NULL, CONSTRAINT unit_progressPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-33
CREATE TABLE user (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, email VARCHAR(255) NULL, enabled BIT NOT NULL, name VARCHAR(255) NULL, password VARCHAR(255) NOT NULL, username VARCHAR(255) NOT NULL, CONSTRAINT userPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-34
CREATE TABLE user_log (id VARCHAR(255) NOT NULL, login_date datetime NULL, logout_date datetime NULL, user_id VARCHAR(255) NOT NULL, CONSTRAINT user_logPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-35
CREATE TABLE user_permission (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, display_name VARCHAR(255) NULL, name VARCHAR(255) NOT NULL, readonly BIT DEFAULT 0 NOT NULL, CONSTRAINT user_permissionPK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-36
CREATE TABLE user_role (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, readonly BIT DEFAULT 0 NOT NULL, CONSTRAINT user_rolePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-37
CREATE TABLE user_role_permissions (role_id VARCHAR(255) NOT NULL, permission_id VARCHAR(255) NOT NULL, CONSTRAINT PK_USER_ROLE_PERMISSIONS PRIMARY KEY (role_id, permission_id));

--changeset kdondziak:1600078835283-38
CREATE TABLE users_roles (user_id VARCHAR(255) NOT NULL, role_id VARCHAR(255) NOT NULL, CONSTRAINT PK_USERS_ROLES PRIMARY KEY (user_id, role_id));

--changeset kdondziak:1600078835283-39
CREATE TABLE village (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, name VARCHAR(255) NOT NULL, owner_id VARCHAR(255) NOT NULL, facility_id VARCHAR(255) NOT NULL, CONSTRAINT villagePK PRIMARY KEY (id));

--changeset kdondziak:1600078835283-40
ALTER TABLE chw_group ADD CONSTRAINT UC_CHW_GROUPNAME_COL UNIQUE (name);

--changeset kdondziak:1600078835283-41
ALTER TABLE community_health_worker ADD CONSTRAINT UC_COMMUNITY_HEALTH_WORKERCHW_ID_COL UNIQUE (chw_id);

--changeset kdondziak:1600078835283-42
ALTER TABLE community_health_worker ADD CONSTRAINT UC_COMMUNITY_HEALTH_WORKERIVR_ID_COL UNIQUE (ivr_id);

--changeset kdondziak:1600078835283-43
ALTER TABLE community_health_worker ADD CONSTRAINT UC_COMMUNITY_HEALTH_WORKERPHONE_NUMBER_COL UNIQUE (phone_number);

--changeset kdondziak:1600078835283-44
ALTER TABLE ivr_config ADD CONSTRAINT UC_IVR_CONFIGNAME_COL UNIQUE (name);

--changeset kdondziak:1600078835283-45
ALTER TABLE jasper_templates ADD CONSTRAINT UC_JASPER_TEMPLATESNAME_COL UNIQUE (name);

--changeset kdondziak:1600078835283-46
ALTER TABLE module ADD CONSTRAINT UC_MODULENAME_CODE_COL UNIQUE (name_code);

--changeset kdondziak:1600078835283-47
ALTER TABLE user ADD CONSTRAINT UC_USEREMAIL_COL UNIQUE (email);

--changeset kdondziak:1600078835283-48
ALTER TABLE user ADD CONSTRAINT UC_USERUSERNAME_COL UNIQUE (username);

--changeset kdondziak:1600078835283-49
ALTER TABLE user_role ADD CONSTRAINT UC_USER_ROLENAME_COL UNIQUE (name);

--changeset kdondziak:1600078835283-50
ALTER TABLE facility ADD CONSTRAINT UK3mecgv5hgteuykpjkjjqs8non UNIQUE (name, sector_id);

--changeset kdondziak:1600078835283-51
ALTER TABLE sector ADD CONSTRAINT UK4cq62q6fbyvpfu2renaxcyq4e UNIQUE (name, district_id);

--changeset kdondziak:1600078835283-52
ALTER TABLE village ADD CONSTRAINT UKkjs7irjjd5qmb89714k34m9i UNIQUE (name, facility_id);

--changeset kdondziak:1600078835283-53
ALTER TABLE district ADD CONSTRAINT UKkvkg405mr04cjytv1tuyh7ryr UNIQUE (name);

--changeset kdondziak:1600078835283-54
ALTER TABLE multiple_choice_question ADD CONSTRAINT FK16mx3unw8l3yxyrqgjfhw1arh FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id);

--changeset kdondziak:1600078835283-55
ALTER TABLE unit_progress ADD CONSTRAINT FK1sc0rfpkg18aue2hxh7st7cuj FOREIGN KEY (unit_id) REFERENCES unit (id);

--changeset kdondziak:1600078835283-56
ALTER TABLE district_assignment_log ADD CONSTRAINT FK35xsd0me3xm0km60i9ex4gbwv FOREIGN KEY (district_id) REFERENCES district (id);

--changeset kdondziak:1600078835283-57
ALTER TABLE village ADD CONSTRAINT FK3hiaj53dsf3827n64h9ipaftm FOREIGN KEY (facility_id) REFERENCES facility (id);

--changeset kdondziak:1600078835283-58
ALTER TABLE facility ADD CONSTRAINT FK3mhmpg9vjhkthrqx17wi7jtbr FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset kdondziak:1600078835283-59
ALTER TABLE user_role_permissions ADD CONSTRAINT FK4ol1yjwd29gjey9t05pi704qs FOREIGN KEY (permission_id) REFERENCES user_permission (id);

--changeset kdondziak:1600078835283-60
ALTER TABLE call_detail_record_data ADD CONSTRAINT FK5xvgp1vq6d7ym79he9qb54s91 FOREIGN KEY (cdr_id) REFERENCES call_detail_record (id);

--changeset kdondziak:1600078835283-61
ALTER TABLE district_assignment_log ADD CONSTRAINT FK74qra792bivntkq52wqycabhd FOREIGN KEY (group_id) REFERENCES chw_group (id);

--changeset kdondziak:1600078835283-62
ALTER TABLE call_flow_element_log ADD CONSTRAINT FK7uxgn0fe0tf4mcbf7rb7pcf1c FOREIGN KEY (unit_progress_id) REFERENCES unit_progress (id);

--changeset kdondziak:1600078835283-63
ALTER TABLE message_log ADD CONSTRAINT FK8mmo4p9rxt2jjioct3q3vcaio FOREIGN KEY (call_flow_element_log_id) REFERENCES call_flow_element_log (id);

--changeset kdondziak:1600078835283-64
ALTER TABLE district_assignment_log ADD CONSTRAINT FK9my0xx8dmq1h8g9q7c0s0gi7w FOREIGN KEY (facility_id) REFERENCES facility (id);

--changeset kdondziak:1600078835283-65
ALTER TABLE sector ADD CONSTRAINT FK9wfuya7l83obc1cnkh6w5yxek FOREIGN KEY (district_id) REFERENCES district (id);

--changeset kdondziak:1600078835283-66
ALTER TABLE message ADD CONSTRAINT FKa77h4bklwxw8i61ox9ge3qaa0 FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id);

--changeset kdondziak:1600078835283-67
ALTER TABLE module_progress ADD CONSTRAINT FKaj61x09ycr36qng40hwpww7lu FOREIGN KEY (chw_id) REFERENCES community_health_worker (id);

--changeset kdondziak:1600078835283-68
ALTER TABLE community_health_worker ADD CONSTRAINT FKb7ajufa5sqcprhp93rd1aimox FOREIGN KEY (group_id) REFERENCES chw_group (id);

--changeset kdondziak:1600078835283-69
ALTER TABLE unit ADD CONSTRAINT FKbs3cm3jk85mwf7d4ggxiheabe FOREIGN KEY (module_id) REFERENCES module (id);

--changeset kdondziak:1600078835283-70
ALTER TABLE call_flow_element ADD CONSTRAINT FKdgxmo7cqpg9wyt6bugpxphhb8 FOREIGN KEY (unit_id) REFERENCES unit (id);

--changeset kdondziak:1600078835283-71
ALTER TABLE module ADD CONSTRAINT FKeo3gitdl1ge6qb5yion24ut49 FOREIGN KEY (previous_version_id) REFERENCES module (id);

--changeset kdondziak:1600078835283-72
ALTER TABLE community_health_worker ADD CONSTRAINT FKf2v6hefppoj0x6twlhjv48xko FOREIGN KEY (district_id) REFERENCES district (id);

--changeset kdondziak:1600078835283-73
ALTER TABLE jasper_template_parameter_options ADD CONSTRAINT FKf31nhehpvovubnlfm674wg4cd FOREIGN KEY (id) REFERENCES template_parameters (id);

--changeset kdondziak:1600078835283-74
ALTER TABLE module_assignment ADD CONSTRAINT FKfpn0o6k6bemdlf42sgg2vstyw FOREIGN KEY (module_id) REFERENCES module (id);

--changeset kdondziak:1600078835283-75
ALTER TABLE module_progress ADD CONSTRAINT FKg9ofqigvb0r2mbpdhgiijvu31 FOREIGN KEY (course_module_id) REFERENCES course_module (id);

--changeset kdondziak:1600078835283-76
ALTER TABLE users_roles ADD CONSTRAINT FKgd3iendaoyh04b95ykqise6qh FOREIGN KEY (user_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-77
ALTER TABLE course ADD CONSTRAINT FKgdl2k38f9lgphdftfmi91kccr FOREIGN KEY (previous_version_id) REFERENCES course (id);

--changeset kdondziak:1600078835283-78
ALTER TABLE choice ADD CONSTRAINT FKgqxnuxtoxp900dqobs032x901 FOREIGN KEY (question_id) REFERENCES multiple_choice_question (call_flow_element_id);

--changeset kdondziak:1600078835283-79
ALTER TABLE multiple_choice_question_log ADD CONSTRAINT FKgx1boftsm9kfq5fq3v3ywtaq6 FOREIGN KEY (call_flow_element_log_id) REFERENCES call_flow_element_log (id);

--changeset kdondziak:1600078835283-80
ALTER TABLE ivr_config_call_status_map ADD CONSTRAINT FKiv9m5fsgmlmmkny1tkd29ani6 FOREIGN KEY (ivr_config_id) REFERENCES ivr_config (id);

--changeset kdondziak:1600078835283-81
ALTER TABLE assigned_modules ADD CONSTRAINT FKj3mostj9opdsslyhv4ir03w9c FOREIGN KEY (health_worker_id) REFERENCES community_health_worker (id);

--changeset kdondziak:1600078835283-82
ALTER TABLE facility ADD CONSTRAINT FKj4bbqvvhwk0fy0kqih94i361f FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-83
ALTER TABLE jasper_template_supported_formats ADD CONSTRAINT FKjdfliesfcst7d0jutngwynvwa FOREIGN KEY (jasper_template_id) REFERENCES jasper_templates (id);

--changeset kdondziak:1600078835283-84
ALTER TABLE user_role_permissions ADD CONSTRAINT FKjkt2u4oxtuxwh5ibl4tbpif2u FOREIGN KEY (role_id) REFERENCES user_role (id);

--changeset kdondziak:1600078835283-85
ALTER TABLE district_assignment_log ADD CONSTRAINT FKkg6ujxv9xt5m5p5g5840p5x8a FOREIGN KEY (module_id) REFERENCES module (id);

--changeset kdondziak:1600078835283-86
ALTER TABLE course_module ADD CONSTRAINT FKkge7sg0xxyo0sxgfelpavhjdj FOREIGN KEY (course_id) REFERENCES course (id);

--changeset kdondziak:1600078835283-87
ALTER TABLE assigned_course ADD CONSTRAINT FKkn7na6ymvu8rektni20nd2rvj FOREIGN KEY (course_id) REFERENCES course (id);

--changeset kdondziak:1600078835283-88
ALTER TABLE community_health_worker ADD CONSTRAINT FKl87uscintaj1cdkos7ha5a56e FOREIGN KEY (facility_id) REFERENCES facility (id);

--changeset kdondziak:1600078835283-89
ALTER TABLE unit_progress ADD CONSTRAINT FKlhbirf0m2itbvy53uijbx7eu9 FOREIGN KEY (module_progress_id) REFERENCES module_progress (id);

--changeset kdondziak:1600078835283-90
ALTER TABLE community_health_worker ADD CONSTRAINT FKlwqg9xaal72wvlk5rnwwjty4y FOREIGN KEY (village_id) REFERENCES village (id);

--changeset kdondziak:1600078835283-91
ALTER TABLE module_assignment ADD CONSTRAINT FKly23tqnk0vla1amxwm1r9rlky FOREIGN KEY (assigned_modules_id) REFERENCES assigned_modules (id);

--changeset kdondziak:1600078835283-92
ALTER TABLE template_parameters ADD CONSTRAINT FKlyvjfikotvvjaxqgm1g8ylq2v FOREIGN KEY (template_id) REFERENCES jasper_templates (id);

--changeset kdondziak:1600078835283-93
ALTER TABLE sector ADD CONSTRAINT FKm906rij5n5tmb6s35orduwpji FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-94
ALTER TABLE user_log ADD CONSTRAINT FKmd6gmr2tvduf9qvif1nchhqfm FOREIGN KEY (user_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-95
ALTER TABLE users_roles ADD CONSTRAINT FKmknhyioq8hh8seoxe1fy6qo86 FOREIGN KEY (role_id) REFERENCES user_role (id);

--changeset kdondziak:1600078835283-96
ALTER TABLE district ADD CONSTRAINT FKmqx0dvfgyjfvu55qo4n7e4rmb FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-97
ALTER TABLE course_module ADD CONSTRAINT FKnxpmkao1aniqf059wcl785y67 FOREIGN KEY (module_id) REFERENCES module (id);

--changeset kdondziak:1600078835283-98
ALTER TABLE village ADD CONSTRAINT FKocaecc1m4419r4fdlljj30dfv FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-99
ALTER TABLE ivr_config_languages ADD CONSTRAINT FKpf7yd3nqoj629q8gbu95o6yrg FOREIGN KEY (ivr_config_id) REFERENCES ivr_config (id);

--changeset kdondziak:1600078835283-100
ALTER TABLE course_module ADD CONSTRAINT FKqycbh2ogasb0x9ldt9kxet46g FOREIGN KEY (previous_version_id) REFERENCES course_module (id);

--changeset kdondziak:1600078835283-101
ALTER TABLE multiple_choice_question_log ADD CONSTRAINT FKraoecc8g2enm2nb8f4wjkcoq2 FOREIGN KEY (response_id) REFERENCES choice (id);

--changeset kdondziak:1600078835283-102
ALTER TABLE district_assignment_log ADD CONSTRAINT FKrn57woe2hf202uok5hrqw4tak FOREIGN KEY (owner_id) REFERENCES user (id);

--changeset kdondziak:1600078835283-103
ALTER TABLE assigned_course ADD CONSTRAINT FKs2lk77yqkmagt26m4hh5ww3ym FOREIGN KEY (health_worker_id) REFERENCES community_health_worker (id);

--changeset kdondziak:1600078835283-104
ALTER TABLE district_assignment_log ADD CONSTRAINT FKs3aj1g2mit4gkswjs6lajbjxy FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset kdondziak:1600078835283-105
ALTER TABLE community_health_worker ADD CONSTRAINT FKtcmmw6l72djiab42udt9xnhly FOREIGN KEY (sector_id) REFERENCES sector (id);

--changeset kdondziak:1600078835283-106
ALTER TABLE call_flow_element_log ADD CONSTRAINT FKth2klq4eqfmp825vboeyblypn FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id);

