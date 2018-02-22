--liquibase formatted sql

--changeset user:1519295578093-2
ALTER TABLE assigned_course CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-3
ALTER TABLE assigned_modules CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-4
ALTER TABLE call_detail_record CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-5
ALTER TABLE call_flow_element CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-6
ALTER TABLE chiefdom CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-7
ALTER TABLE choice CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-8
ALTER TABLE community CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-9
ALTER TABLE community_health_worker CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-10
ALTER TABLE course CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-11
ALTER TABLE district CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-12
ALTER TABLE facility CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-13
ALTER TABLE in_charge CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-14
ALTER TABLE ivr_config CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-15
ALTER TABLE jasper_templates CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-16
ALTER TABLE module CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-17
ALTER TABLE module_progress CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-18
ALTER TABLE question_response CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-19
ALTER TABLE template_parameters CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-20
ALTER TABLE unit CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-21
ALTER TABLE unit_progress CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-22
ALTER TABLE user CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-23
ALTER TABLE user_role CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-24
ALTER TABLE assigned_course CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-25
ALTER TABLE assigned_modules CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-26
ALTER TABLE call_detail_record CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-27
ALTER TABLE call_flow_element CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-28
ALTER TABLE chiefdom CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-29
ALTER TABLE choice CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-30
ALTER TABLE community CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-31
ALTER TABLE community_health_worker CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-32
ALTER TABLE course CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-33
ALTER TABLE district CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-34
ALTER TABLE facility CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-35
ALTER TABLE in_charge CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-36
ALTER TABLE ivr_config CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-37
ALTER TABLE jasper_templates CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-38
ALTER TABLE module CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-39
ALTER TABLE module_progress CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-40
ALTER TABLE question_response CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-41
ALTER TABLE template_parameters CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-42
ALTER TABLE unit CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-43
ALTER TABLE unit_progress CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-44
ALTER TABLE user CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-45
ALTER TABLE user_role CHANGE update_date updated_date datetime NULL;

--changeset user:1519295578093-46
ALTER TABLE district_assignment_log CHANGE create_date created_date datetime NULL;

--changeset user:1519295578093-47
ALTER TABLE district_assignment_log CHANGE update_date updated_date datetime NULL;
