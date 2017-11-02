--liquibase formatted sql

--changeset pmuchowski:1509471087404-1
CREATE TABLE assigned_course (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, end_date datetime(6) NULL, start_date datetime(6) NOT NULL, course_id VARCHAR(255) NULL, health_worker_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-2
CREATE TABLE assigned_modules (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, health_worker_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-3
CREATE TABLE call_flow_element (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, content LONGTEXT NULL, list_order INT NOT NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, unit_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-4
CREATE TABLE chiefdom (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, name VARCHAR(255) NOT NULL, district_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-5
CREATE TABLE choice (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, description LONGTEXT NULL, is_correct BIT(1) NOT NULL, ivr_name VARCHAR(255) NULL, ivr_pressed_key INT NOT NULL, question_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-6
CREATE TABLE client (client_id VARCHAR(255) NOT NULL, access_token_validity_seconds INT NULL, additional_information VARCHAR(255) NULL, authorities VARCHAR(255) NOT NULL, authorized_grant_types VARCHAR(255) NOT NULL, client_secret VARCHAR(255) NULL, refresh_token_validity_seconds INT NULL, registered_redirect_uris VARCHAR(255) NULL, resource_ids VARCHAR(255) NULL, scope VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-7
CREATE TABLE community (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, name VARCHAR(255) NOT NULL, facility_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-8
CREATE TABLE community_health_worker (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, chw_id VARCHAR(255) NOT NULL, date_of_birth date NULL, education_level VARCHAR(255) NULL, first_name VARCHAR(255) NULL, gender VARCHAR(255) NULL, ivr_id VARCHAR(255) NULL, literacy VARCHAR(255) NULL, other_name VARCHAR(255) NULL, peer_supervisor BIT(1) NULL, phone_number VARCHAR(255) NOT NULL, preferred_language VARCHAR(255) NULL, second_name VARCHAR(255) NULL, community_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-9
CREATE TABLE course (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, description LONGTEXT NULL, name VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, version INT NOT NULL, no_modules_message_id VARCHAR(255) NULL, previous_version_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-10
CREATE TABLE course_module (course_id VARCHAR(255) NOT NULL, module_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1509471087404-11
CREATE TABLE district (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, name VARCHAR(255) NOT NULL);

--changeset pmuchowski:1509471087404-12
CREATE TABLE facility (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, name VARCHAR(255) NOT NULL, type VARCHAR(255) NULL, chiefdom_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-13
CREATE TABLE in_charge (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, email VARCHAR(255) NULL, name VARCHAR(255) NULL, phone_number VARCHAR(255) NULL, facility_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-14
CREATE TABLE message (call_flow_element_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1509471087404-15
CREATE TABLE module (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, description LONGTEXT NULL, module_number INT NOT NULL, name VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, version INT NOT NULL, previous_version_id VARCHAR(255) NULL, start_module_question_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-16
CREATE TABLE module_progress (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, status VARCHAR(255) NOT NULL, current_unit_id VARCHAR(255) NULL, module_id VARCHAR(255) NULL, assigned_modules_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-17
CREATE TABLE multiple_choice_question (call_flow_element_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1509471087404-18
CREATE TABLE question_response (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, number_of_attempts INT NOT NULL, response_id VARCHAR(255) NULL, question_id VARCHAR(255) NULL, unit_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-19
CREATE TABLE unit (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, ivr_id VARCHAR(255) NULL, ivr_name VARCHAR(255) NULL, allow_replay BIT(1) NOT NULL, description LONGTEXT NULL, list_order INT NOT NULL, name VARCHAR(255) NOT NULL, continuation_question_id VARCHAR(255) NULL, module_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-20
CREATE TABLE unit_progress (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, number_of_replays INT NOT NULL, status VARCHAR(255) NOT NULL, current_call_flow_id VARCHAR(255) NULL, unit_id VARCHAR(255) NULL, module_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-21
CREATE TABLE user (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, email VARCHAR(255) NULL, enabled BIT(1) NULL, name VARCHAR(255) NULL, password VARCHAR(255) NULL, username VARCHAR(255) NOT NULL);

--changeset pmuchowski:1509471087404-22
CREATE TABLE user_role (id VARCHAR(255) NOT NULL, create_date datetime(6) NULL, update_date datetime(6) NULL, name VARCHAR(255) NOT NULL, user_id VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-23
CREATE TABLE user_role_permissions (user_role_id VARCHAR(255) NOT NULL, permissions VARCHAR(255) NULL);

--changeset pmuchowski:1509471087404-24
ALTER TABLE assigned_course ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-25
ALTER TABLE assigned_modules ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-26
ALTER TABLE call_flow_element ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-27
ALTER TABLE chiefdom ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-28
ALTER TABLE choice ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-29
ALTER TABLE client ADD PRIMARY KEY (client_id);

--changeset pmuchowski:1509471087404-30
ALTER TABLE community ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-31
ALTER TABLE community_health_worker ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-32
ALTER TABLE course ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-33
ALTER TABLE district ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-34
ALTER TABLE facility ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-35
ALTER TABLE in_charge ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-36
ALTER TABLE message ADD PRIMARY KEY (call_flow_element_id);

--changeset pmuchowski:1509471087404-37
ALTER TABLE module ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-38
ALTER TABLE module_progress ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-39
ALTER TABLE multiple_choice_question ADD PRIMARY KEY (call_flow_element_id);

--changeset pmuchowski:1509471087404-40
ALTER TABLE question_response ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-41
ALTER TABLE unit ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-42
ALTER TABLE unit_progress ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-43
ALTER TABLE user ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-44
ALTER TABLE user_role ADD PRIMARY KEY (id);

--changeset pmuchowski:1509471087404-45
ALTER TABLE community_health_worker ADD CONSTRAINT UK_1jv7qikfrjk1q3t6mu0erdu3l UNIQUE (chw_id);

--changeset pmuchowski:1509471087404-46
ALTER TABLE community_health_worker ADD CONSTRAINT UK_djvtyj0jd11n4tf0ur8r55ttf UNIQUE (phone_number);

--changeset pmuchowski:1509471087404-47
ALTER TABLE community_health_worker ADD CONSTRAINT UK_iq4xqlohlon1ujatnoa4uyfa2 UNIQUE (ivr_id);

--changeset pmuchowski:1509471087404-48
ALTER TABLE district ADD CONSTRAINT UK_kvkg405mr04cjytv1tuyh7ryr UNIQUE (name);

--changeset pmuchowski:1509471087404-49
ALTER TABLE user_role ADD CONSTRAINT UK_lnth8w122wgy7grrjlu8hjmuu UNIQUE (name);

--changeset pmuchowski:1509471087404-50
ALTER TABLE user ADD CONSTRAINT UK_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);

--changeset pmuchowski:1509471087404-51
ALTER TABLE chiefdom ADD CONSTRAINT UK_t2149oqguejxnd84goin33pmy UNIQUE (name);

--changeset pmuchowski:1509471087404-52
CREATE INDEX FK1sc0rfpkg18aue2hxh7st7cuj ON unit_progress(unit_id);

--changeset pmuchowski:1509471087404-53
CREATE INDEX FK1xnj1ersgdj0mi8dskao9oag0 ON chiefdom(district_id);

--changeset pmuchowski:1509471087404-54
CREATE INDEX FK38jlskoqmxu97ma7qywfvud1t ON question_response(response_id);

--changeset pmuchowski:1509471087404-55
CREATE INDEX FK4dntrbp4yli2wdio1i0ermjv8 ON community(facility_id);

--changeset pmuchowski:1509471087404-56
CREATE INDEX FK859n2jvi8ivhui0rl0esws6o ON user_role(user_id);

--changeset pmuchowski:1509471087404-57
CREATE INDEX FK8enh3u2aqu6ull7y1voefavj9 ON module_progress(current_unit_id);

--changeset pmuchowski:1509471087404-58
CREATE INDEX FKb3od5pjcv9xhiqxipdo4pytwi ON module_progress(assigned_modules_id);

--changeset pmuchowski:1509471087404-59
CREATE INDEX FKbs3cm3jk85mwf7d4ggxiheabe ON unit(module_id);

--changeset pmuchowski:1509471087404-60
CREATE INDEX FKdgxmo7cqpg9wyt6bugpxphhb8 ON call_flow_element(unit_id);

--changeset pmuchowski:1509471087404-61
CREATE INDEX FKej0l2sdumgw7jwtk64stuh7gm ON module(start_module_question_id);

--changeset pmuchowski:1509471087404-62
CREATE INDEX FKeo3gitdl1ge6qb5yion24ut49 ON module(previous_version_id);

--changeset pmuchowski:1509471087404-63
CREATE INDEX FKf463dg8s62qhel221m8exfcgx ON user_role_permissions(user_role_id);

--changeset pmuchowski:1509471087404-64
CREATE INDEX FKgdl2k38f9lgphdftfmi91kccr ON course(previous_version_id);

--changeset pmuchowski:1509471087404-65
CREATE INDEX FKgqxnuxtoxp900dqobs032x901 ON choice(question_id);

--changeset pmuchowski:1509471087404-66
CREATE INDEX FKhfrr5otnbipe5kd6w5qx3t9t9 ON question_response(question_id);

--changeset pmuchowski:1509471087404-67
CREATE INDEX FKhs1oq1jxfysj9c3iqu5w91b17 ON facility(chiefdom_id);

--changeset pmuchowski:1509471087404-68
CREATE INDEX FKj3mostj9opdsslyhv4ir03w9c ON assigned_modules(health_worker_id);

--changeset pmuchowski:1509471087404-69
CREATE INDEX FKk04dsxve9pxghlvxawa8molh1 ON unit_progress(module_id);

--changeset pmuchowski:1509471087404-70
CREATE INDEX FKk5y5mkg4jpy4mfgw2amllmc9j ON unit_progress(current_call_flow_id);

--changeset pmuchowski:1509471087404-71
CREATE INDEX FKkge7sg0xxyo0sxgfelpavhjdj ON course_module(course_id);

--changeset pmuchowski:1509471087404-72
CREATE INDEX FKkn7na6ymvu8rektni20nd2rvj ON assigned_course(course_id);

--changeset pmuchowski:1509471087404-73
CREATE INDEX FKn3401kdg0uu7frmte7s569rw1 ON course(no_modules_message_id);

--changeset pmuchowski:1509471087404-74
CREATE INDEX FKnxpmkao1aniqf059wcl785y67 ON course_module(module_id);

--changeset pmuchowski:1509471087404-75
CREATE INDEX FKotqxl5s4ubc87rh77fhb5g25p ON question_response(unit_id);

--changeset pmuchowski:1509471087404-76
CREATE INDEX FKqte1bg2x6d5vu3g4qujmvhsod ON unit(continuation_question_id);

--changeset pmuchowski:1509471087404-77
CREATE INDEX FKrklx0sebigmraipwpbjrw14i9 ON module_progress(module_id);

--changeset pmuchowski:1509471087404-78
CREATE INDEX FKroi9371lxsrgufgva8c4n1scx ON community_health_worker(community_id);

--changeset pmuchowski:1509471087404-79
CREATE INDEX FKs2lk77yqkmagt26m4hh5ww3ym ON assigned_course(health_worker_id);

--changeset pmuchowski:1509471087404-80
CREATE INDEX FKsuaql5lw6h05a5kvejb0v5vj6 ON in_charge(facility_id);

--changeset pmuchowski:1509471087404-81
ALTER TABLE multiple_choice_question ADD CONSTRAINT FK16mx3unw8l3yxyrqgjfhw1arh FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-82
ALTER TABLE unit_progress ADD CONSTRAINT FK1sc0rfpkg18aue2hxh7st7cuj FOREIGN KEY (unit_id) REFERENCES unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-83
ALTER TABLE chiefdom ADD CONSTRAINT FK1xnj1ersgdj0mi8dskao9oag0 FOREIGN KEY (district_id) REFERENCES district (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-84
ALTER TABLE question_response ADD CONSTRAINT FK38jlskoqmxu97ma7qywfvud1t FOREIGN KEY (response_id) REFERENCES choice (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-85
ALTER TABLE community ADD CONSTRAINT FK4dntrbp4yli2wdio1i0ermjv8 FOREIGN KEY (facility_id) REFERENCES facility (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-86
ALTER TABLE user_role ADD CONSTRAINT FK859n2jvi8ivhui0rl0esws6o FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-87
ALTER TABLE module_progress ADD CONSTRAINT FK8enh3u2aqu6ull7y1voefavj9 FOREIGN KEY (current_unit_id) REFERENCES unit_progress (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-88
ALTER TABLE message ADD CONSTRAINT FKa77h4bklwxw8i61ox9ge3qaa0 FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-89
ALTER TABLE module_progress ADD CONSTRAINT FKb3od5pjcv9xhiqxipdo4pytwi FOREIGN KEY (assigned_modules_id) REFERENCES assigned_modules (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-90
ALTER TABLE unit ADD CONSTRAINT FKbs3cm3jk85mwf7d4ggxiheabe FOREIGN KEY (module_id) REFERENCES module (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-91
ALTER TABLE call_flow_element ADD CONSTRAINT FKdgxmo7cqpg9wyt6bugpxphhb8 FOREIGN KEY (unit_id) REFERENCES unit (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-92
ALTER TABLE module ADD CONSTRAINT FKej0l2sdumgw7jwtk64stuh7gm FOREIGN KEY (start_module_question_id) REFERENCES multiple_choice_question (call_flow_element_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-93
ALTER TABLE module ADD CONSTRAINT FKeo3gitdl1ge6qb5yion24ut49 FOREIGN KEY (previous_version_id) REFERENCES module (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-94
ALTER TABLE user_role_permissions ADD CONSTRAINT FKf463dg8s62qhel221m8exfcgx FOREIGN KEY (user_role_id) REFERENCES user_role (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-95
ALTER TABLE course ADD CONSTRAINT FKgdl2k38f9lgphdftfmi91kccr FOREIGN KEY (previous_version_id) REFERENCES course (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-96
ALTER TABLE choice ADD CONSTRAINT FKgqxnuxtoxp900dqobs032x901 FOREIGN KEY (question_id) REFERENCES multiple_choice_question (call_flow_element_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-97
ALTER TABLE question_response ADD CONSTRAINT FKhfrr5otnbipe5kd6w5qx3t9t9 FOREIGN KEY (question_id) REFERENCES multiple_choice_question (call_flow_element_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-98
ALTER TABLE facility ADD CONSTRAINT FKhs1oq1jxfysj9c3iqu5w91b17 FOREIGN KEY (chiefdom_id) REFERENCES chiefdom (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-99
ALTER TABLE assigned_modules ADD CONSTRAINT FKj3mostj9opdsslyhv4ir03w9c FOREIGN KEY (health_worker_id) REFERENCES community_health_worker (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-100
ALTER TABLE unit_progress ADD CONSTRAINT FKk04dsxve9pxghlvxawa8molh1 FOREIGN KEY (module_id) REFERENCES module_progress (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-101
ALTER TABLE unit_progress ADD CONSTRAINT FKk5y5mkg4jpy4mfgw2amllmc9j FOREIGN KEY (current_call_flow_id) REFERENCES call_flow_element (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-102
ALTER TABLE course_module ADD CONSTRAINT FKkge7sg0xxyo0sxgfelpavhjdj FOREIGN KEY (course_id) REFERENCES course (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-103
ALTER TABLE assigned_course ADD CONSTRAINT FKkn7na6ymvu8rektni20nd2rvj FOREIGN KEY (course_id) REFERENCES course (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-104
ALTER TABLE course ADD CONSTRAINT FKn3401kdg0uu7frmte7s569rw1 FOREIGN KEY (no_modules_message_id) REFERENCES message (call_flow_element_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-105
ALTER TABLE course_module ADD CONSTRAINT FKnxpmkao1aniqf059wcl785y67 FOREIGN KEY (module_id) REFERENCES module (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-106
ALTER TABLE question_response ADD CONSTRAINT FKotqxl5s4ubc87rh77fhb5g25p FOREIGN KEY (unit_id) REFERENCES unit_progress (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-107
ALTER TABLE unit ADD CONSTRAINT FKqte1bg2x6d5vu3g4qujmvhsod FOREIGN KEY (continuation_question_id) REFERENCES multiple_choice_question (call_flow_element_id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-108
ALTER TABLE module_progress ADD CONSTRAINT FKrklx0sebigmraipwpbjrw14i9 FOREIGN KEY (module_id) REFERENCES module (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-109
ALTER TABLE community_health_worker ADD CONSTRAINT FKroi9371lxsrgufgva8c4n1scx FOREIGN KEY (community_id) REFERENCES community (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-110
ALTER TABLE assigned_course ADD CONSTRAINT FKs2lk77yqkmagt26m4hh5ww3ym FOREIGN KEY (health_worker_id) REFERENCES community_health_worker (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

--changeset pmuchowski:1509471087404-111
ALTER TABLE in_charge ADD CONSTRAINT FKsuaql5lw6h05a5kvejb0v5vj6 FOREIGN KEY (facility_id) REFERENCES facility (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

