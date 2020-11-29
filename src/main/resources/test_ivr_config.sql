INSERT INTO `ivr_config` (`id`, `created_date`, `updated_date`, `name`, `base_url`, `project_id`, `channel`, `send_module_assignment_message`, `module_assigned_message_id`, `call_id_field`, `chw_phone_field`, `call_duration_field`, `call_status_reason_field`, `call_status_field`)
VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '2020-11-27 16:07:22.745000', '2020-11-27 16:07:22.745000', 'Verboice', 'http://mots.mohs.gov.sl:8085', '1', 'mohs_3cx', 1, '4', 'CallSid', 'From', 'CallDuration', 'CallStatusReason', 'CallStatus');

INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'English', 'ENGLISH');

INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'in-progress', 'IN_PROGRESS');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'completed', 'FINISHED_COMPLETE');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'failed', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'no-answer', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', 'ringing', 'INITIATED');
