INSERT INTO `ivr_config` (`id`, `create_date`, `update_date`, `name`, `base_url`, `default_users_group_id`, `detect_voicemail_action`, `module_assigned_message_id`, `retry_attempts_long`, `retry_attempts_short`, `retry_delay_long`, `retry_delay_short`, `send_sms_if_voice_fails`, `call_id_field`, `chw_ivr_id_field`, `call_log_id_field`, `call_status_field`, `voto_main_tree_id`)
VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '2018-02-15 16:07:22.745000', '2018-02-15 16:07:22.745000', 'Voto', 'https://go.votomobile.org/api/v1', '298868', true, '2228099', 1, 3, 60, 15, true, 'outgoing_call_id', 'subscriber_id', 'delivery_log_id', 'delivery_status', '22047');

INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '206945', 'ENGLISH');
INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '207107', 'KRIO');
INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '207108', 'LIMBA');
INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '207109', 'SUSU');
INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '207110', 'TEMNE');
INSERT INTO `mots`.`ivr_config_languages` (`ivr_config_id`, `ivr_language_id`, `language`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '207111', 'MENDE');

INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '1', 'INITIATED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '3', 'IN_PROGRESS');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '6', 'FINISHED_COMPLETE');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '7', 'FINISHED_INCOMPLETE');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '8', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '9', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '10', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '13', 'FAILED');
INSERT INTO `ivr_config_call_status_map` (`ivr_config_id`, `ivr_status`, `status`) VALUES ('70638cc4-4c33-4899-8480-a4ef1607273b', '14', 'FAILED');
