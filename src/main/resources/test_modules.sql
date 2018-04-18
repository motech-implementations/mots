INSERT INTO `course` (`id`, `name`, `status`, `version`,`ivr_id`) VALUES('51cc333d-5135-40b5-864a-0281ec59f0ef', 'course 1', 'RELEASED', 1, '24757');


INSERT INTO `course_module` (`id`, `list_order`, `course_id`, `module_id`,`ivr_id`,`ivr_name`) VALUES ('e9f603be-daac-471b-9737-391442037a7f', 0, '51cc333d-5135-40b5-864a-0281ec59f0ef', '6115e9c1-079e-4720-9005-17bb3a6051b0', '10555155', 'module1');
INSERT INTO `course_module` (`id`, `list_order`, `course_id`, `module_id`,`ivr_id`,`ivr_name`) VALUES ('21182e0d-856d-4baa-8869-f9733bfedeaf', 1, '51cc333d-5135-40b5-864a-0281ec59f0ef', '65aa8422-46ac-4476-8863-f1ccb4a66d07', '10555156', 'module2');


INSERT INTO `module` (`id`,`created_date`,`updated_date`,`description`,`name`,`status`,`version`,`previous_version_id`,`start_module_question_id`,`name_code`,`ivr_group`) VALUES ('6115e9c1-079e-4720-9005-17bb3a6051b0', '2018-04-12 11:12:56', '2018-04-18 11:41:07', NULL, 'Earth', 'RELEASED', 2, NULL, NULL, 'module-1_v2', '302610');
INSERT INTO `module` (`id`,`created_date`,`updated_date`,`description`,`name`,`status`,`version`,`previous_version_id`,`start_module_question_id`,`name_code`,`ivr_group`) VALUES ('65aa8422-46ac-4476-8863-f1ccb4a66d07', '2018-04-17 11:37:17', '2018-04-18 11:41:30', NULL, 'Sierra Leone', 'RELEASED', 2, NULL, NULL, 'module-2_v2', '302611');


INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('a0b9abc3-d279-407c-8511-6eabb0253641', '2018-04-12 11:12:56', '2018-04-17 16:10:21', '10554199', NULL, 0, NULL, 0, 'unit 1', NULL, '6115e9c1-079e-4720-9005-17bb3a6051b0');
INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('ce0e21c7-1240-48f2-95f0-ff68d7af1dfb', '2018-04-12 11:12:56', '2018-04-17 16:10:21', '10554197', NULL, 1, NULL, 1, 'unit 2', NULL, '6115e9c1-079e-4720-9005-17bb3a6051b0');
INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0', '2018-04-12 11:12:56', '2018-04-17 16:10:21', '10554204', NULL, 1, NULL, 2, 'unit 3', NULL, '6115e9c1-079e-4720-9005-17bb3a6051b0');

INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('fcf04098-7a59-4e09-86c8-87fa3098e3b3', '2018-04-17 11:37:17', '2018-04-17 16:10:21', '10554836', NULL, 0, NULL, 0, 'unit 1', NULL, '65aa8422-46ac-4476-8863-f1ccb4a66d07');
INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('3d19da26-4509-4033-af53-e5d8757375a3', '2018-04-17 11:37:17', '2018-04-17 16:10:21', '10554834', NULL, 1, NULL, 1, 'unit 2', NULL, '65aa8422-46ac-4476-8863-f1ccb4a66d07');
INSERT INTO `unit` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name`,`allow_replay`,`description`,`list_order`,`name`,`continuation_question_id`,`module_id`) VALUES ('92f33d3c-4dcd-4e95-a34f-9d6d650474aa', '2018-04-17 11:37:17', '2018-04-17 16:10:21', '10554831', NULL, 1, NULL, 2, 'unit 3', NULL, '65aa8422-46ac-4476-8863-f1ccb4a66d07');


INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('7ba6fd90-6938-4526-8c4f-f8a98d18ee49', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553516', NULL, NULL, 0, 'intro', 'MESSAGE', 'a0b9abc3-d279-407c-8511-6eabb0253641');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('3d2b0c27-d91b-4a0d-9076-e63bdbf17bdb', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553517', NULL, NULL, 1, 'q1', 'QUESTION', 'a0b9abc3-d279-407c-8511-6eabb0253641');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('f67831b9-0d17-46d8-8540-cd0e45a47aeb', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553518', NULL, NULL, 2, 'q2', 'QUESTION', 'a0b9abc3-d279-407c-8511-6eabb0253641');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('13b9bf37-61ba-4a7f-a0fd-e83daf736d95', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553519', NULL, NULL, 3, 'q3', 'QUESTION', 'a0b9abc3-d279-407c-8511-6eabb0253641');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('4f07acb5-ea01-4088-90f4-89cdbe8615d5', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553523', NULL, NULL, 4, 'q4', 'QUESTION', 'a0b9abc3-d279-407c-8511-6eabb0253641');

INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('d50da68f-6d57-4ae7-85dc-7a3271f647c8', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553604', NULL, NULL, 0, 'intro', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('440c1f23-05eb-4acb-839c-eb40ff3189df', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553605', NULL, NULL, 1, 'm1', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('f6e53eed-e617-4837-9f7c-055875ef12ce', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553606', NULL, NULL, 2, 'm2', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('65724b31-d836-437f-af6d-f2e5d00b82d1', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553607', NULL, NULL, 3, 'm3', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('c8e5eba7-799e-4bb3-bb1e-c711366d79ef', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553608', NULL, NULL, 4, 'q1', 'QUESTION', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('92d35664-a80f-4194-85b4-7b9e2f6dc7af', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553609', NULL, NULL, 5, 'q1 response', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('56ab3639-1437-4b39-bd55-141109f42765', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553610', NULL, NULL, 6, 'q2', 'QUESTION', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('731e67f1-b2b7-4215-84bc-3306402486e5', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10553611', NULL, NULL, 7, 'q2 response', 'MESSAGE', 'ce0e21c7-1240-48f2-95f0-ff68d7af1dfb');

INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('28abff3e-7911-44d2-adfa-48dbf8368e0d', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554721', NULL, NULL, 0, 'intro', 'MESSAGE', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('ab53ee47-211a-4103-b0f9-72addbe0adbe', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554722', NULL, NULL, 1, 'm1', 'MESSAGE', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('6a461e60-885f-40db-adbf-eb345a6ec67a', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554728', NULL, NULL, 2, 'm2', 'MESSAGE', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('04165151-b7fd-4237-81ad-b2e2e00b46b5', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554723', NULL, NULL, 3, 'q1', 'QUESTION', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('c590b5ec-b4ac-475a-9689-931219238a38', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554724', NULL, NULL, 4, 'q1 response', 'MESSAGE', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('03d1b341-fba4-4a86-a46e-150cc39e389b', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554729', NULL, NULL, 5, 'q2', 'QUESTION', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('0bc6d18f-d356-4652-bfd1-294f941b8c3d', '2018-04-12 11:12:56', '2018-04-17 16:35:45', '10554730', NULL, NULL, 6, 'q2 response', 'MESSAGE', '6ac812c8-adfe-4db2-a5a4-c5d8fbd7ccc0');

INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('b4f247da-aa1d-49c5-add0-f91e203d1c58', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554490', NULL, NULL, 0, 'intro', 'MESSAGE', 'fcf04098-7a59-4e09-86c8-87fa3098e3b3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('ef02e47b-dada-4474-a74e-72bed831a429', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554491', NULL, NULL, 1, 'q1', 'QUESTION', 'fcf04098-7a59-4e09-86c8-87fa3098e3b3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('d366402c-2129-4409-a88f-a26969823581', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554492', NULL, NULL, 2, 'q2', 'QUESTION', 'fcf04098-7a59-4e09-86c8-87fa3098e3b3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('e3684dbf-c2bf-44f8-8746-d2493dd206fb', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554493', NULL, NULL, 3, 'q3', 'QUESTION', 'fcf04098-7a59-4e09-86c8-87fa3098e3b3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('5d876465-bcf9-4395-a7f5-36e73db3a674', '2018-04-17 15:55:00', '2018-04-17 16:43:56', '10554497', NULL, NULL, 4, 'q4', 'QUESTION', 'fcf04098-7a59-4e09-86c8-87fa3098e3b3');

INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('3c7a58d8-3be8-4175-8a13-e45177d79630', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554567', NULL, NULL, 0, 'intro', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('bc8cff7f-47b5-426a-8a84-2f6c39e61427', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554568', NULL, NULL, 1, 'm1', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('dfb3cc9b-6073-44d1-b61e-929abb52f67a', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554569', NULL, NULL, 2, 'm2', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('e509399c-f439-4b8f-b8b1-2d22c59ccaa6', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554575', NULL, NULL, 3, 'm3', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('6325a0a5-7d04-421a-9bc6-91e1a0f96592', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554570', NULL, NULL, 4, 'q1', 'QUESTION', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('c5dc9a93-fa56-4ac8-aa21-03639ae13d98', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554571', NULL, NULL, 5, 'q1 response', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('e17759b4-8f91-440f-8e5f-77a8daef928c', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554576', NULL, NULL, 6, 'q2', 'QUESTION', '3d19da26-4509-4033-af53-e5d8757375a3');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('6065eccf-c857-4b9c-839f-f0ac548f15a4', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554577', NULL, NULL, 7, 'q2 response', 'MESSAGE', '3d19da26-4509-4033-af53-e5d8757375a3');

INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('05a54c15-743b-4f0b-a94f-de89a1056d02', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554721', NULL, NULL, 0, 'intro', 'MESSAGE', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('79725757-bef6-40a3-bbe4-72c58caa7d77', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554722', NULL, NULL, 1, 'm1', 'MESSAGE', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('7c113dc4-0d27-4993-8023-f8bdebc9007c', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554728', NULL, NULL, 2, 'm2', 'MESSAGE', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('970b0c66-b1e7-45d4-8586-7ff2c83964f0', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554723', NULL, NULL, 3, 'q1', 'QUESTION', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('8d719f11-c389-4f34-b5f2-1bafc2eb4992', '2018-04-17 11:37:17', '2018-04-17 16:43:56', '10554724', NULL, NULL, 4, 'q1 response', 'MESSAGE', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('a233ef30-cc59-4f04-a035-892da44126c1', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554729', NULL, NULL, 5, 'q2', 'QUESTION', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');
INSERT INTO `call_flow_element` (`id`,`created_date`,`updated_date`,`ivr_id`,`ivr_name` ,`content`,`list_order`,`name`,`type`,`unit_id`) VALUES ('d22387e7-0ec3-4811-945c-1d6afce4028c', '2018-04-17 16:02:58', '2018-04-17 16:43:56', '10554730', NULL, NULL, 6, 'q2 response', 'MESSAGE', '92f33d3c-4dcd-4e95-a34f-9d6d650474aa');


INSERT INTO `message` (`call_flow_element_id`) VALUES ('7ba6fd90-6938-4526-8c4f-f8a98d18ee49');

INSERT INTO `message` (`call_flow_element_id`) VALUES ('d50da68f-6d57-4ae7-85dc-7a3271f647c8');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('440c1f23-05eb-4acb-839c-eb40ff3189df');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('f6e53eed-e617-4837-9f7c-055875ef12ce');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('65724b31-d836-437f-af6d-f2e5d00b82d1');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('92d35664-a80f-4194-85b4-7b9e2f6dc7af');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('731e67f1-b2b7-4215-84bc-3306402486e5');

INSERT INTO `message` (`call_flow_element_id`) VALUES ('28abff3e-7911-44d2-adfa-48dbf8368e0d');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('ab53ee47-211a-4103-b0f9-72addbe0adbe');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('6a461e60-885f-40db-adbf-eb345a6ec67a');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('c590b5ec-b4ac-475a-9689-931219238a38');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('0bc6d18f-d356-4652-bfd1-294f941b8c3d');

INSERT INTO `message` (`call_flow_element_id`) VALUES ('b4f247da-aa1d-49c5-add0-f91e203d1c58');

INSERT INTO `message` (`call_flow_element_id`) VALUES ('3c7a58d8-3be8-4175-8a13-e45177d79630');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('bc8cff7f-47b5-426a-8a84-2f6c39e61427');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('dfb3cc9b-6073-44d1-b61e-929abb52f67a');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('e509399c-f439-4b8f-b8b1-2d22c59ccaa6');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('c5dc9a93-fa56-4ac8-aa21-03639ae13d98');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('6065eccf-c857-4b9c-839f-f0ac548f15a4');

INSERT INTO `message` (`call_flow_element_id`) VALUES ('05a54c15-743b-4f0b-a94f-de89a1056d02');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('79725757-bef6-40a3-bbe4-72c58caa7d77');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('7c113dc4-0d27-4993-8023-f8bdebc9007c');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('8d719f11-c389-4f34-b5f2-1bafc2eb4992');
INSERT INTO `message` (`call_flow_element_id`) VALUES ('d22387e7-0ec3-4811-945c-1d6afce4028c');


INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('3d2b0c27-d91b-4a0d-9076-e63bdbf17bdb');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('f67831b9-0d17-46d8-8540-cd0e45a47aeb');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('13b9bf37-61ba-4a7f-a0fd-e83daf736d95');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('4f07acb5-ea01-4088-90f4-89cdbe8615d5');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('c8e5eba7-799e-4bb3-bb1e-c711366d79ef');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('56ab3639-1437-4b39-bd55-141109f42765');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('04165151-b7fd-4237-81ad-b2e2e00b46b5');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('03d1b341-fba4-4a86-a46e-150cc39e389b');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('ef02e47b-dada-4474-a74e-72bed831a429');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('d366402c-2129-4409-a88f-a26969823581');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('e3684dbf-c2bf-44f8-8746-d2493dd206fb');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('5d876465-bcf9-4395-a7f5-36e73db3a674');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('6325a0a5-7d04-421a-9bc6-91e1a0f96592');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('e17759b4-8f91-440f-8e5f-77a8daef928c');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('970b0c66-b1e7-45d4-8586-7ff2c83964f0');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('a233ef30-cc59-4f04-a035-892da44126c1');


INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('541db2a5-8c6b-456f-bdd5-73b8366a62f8', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '3d2b0c27-d91b-4a0d-9076-e63bdbf17bdb', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('517b422d-d6df-48ec-b9e8-31d87480e103', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '3d2b0c27-d91b-4a0d-9076-e63bdbf17bdb', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('b8904987-6134-40e8-ac20-85ccdd3ddf04', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'f67831b9-0d17-46d8-8540-cd0e45a47aeb', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('2f231916-8900-47a5-9878-2c00d3d1c014', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'f67831b9-0d17-46d8-8540-cd0e45a47aeb', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('75fd7293-04e1-4b83-9ef1-0910015dc8d7', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'f67831b9-0d17-46d8-8540-cd0e45a47aeb', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('f929923f-0d17-4ea7-b1b1-d6b616c07121', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '13b9bf37-61ba-4a7f-a0fd-e83daf736d95', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('23cd0e02-d708-4eb7-a7f5-0d3f33eadf92', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '13b9bf37-61ba-4a7f-a0fd-e83daf736d95', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('9153cd1e-e139-4b1f-a730-78be9658648b', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '13b9bf37-61ba-4a7f-a0fd-e83daf736d95', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('f8b2aeb5-e23a-4d03-9253-f6706e75a7ba', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '4f07acb5-ea01-4088-90f4-89cdbe8615d5', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('0371829c-0ec6-4235-8d25-f85eecab22d3', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '4f07acb5-ea01-4088-90f4-89cdbe8615d5', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('e09d6bb0-3075-4baf-a1bb-90b823a6c98b', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'c8e5eba7-799e-4bb3-bb1e-c711366d79ef', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('0c2d0c8e-3072-4698-a338-4ed96a9fc3c6', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'c8e5eba7-799e-4bb3-bb1e-c711366d79ef', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('c54a647e-b1e9-44f1-a819-b7b1f2adfd2e', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '56ab3639-1437-4b39-bd55-141109f42765', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('bd23cec8-0e3b-4538-9c92-4b805e161e60', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '56ab3639-1437-4b39-bd55-141109f42765', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('41daf19d-2eff-4885-912f-f656087e582c', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '56ab3639-1437-4b39-bd55-141109f42765', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('d3c1eec1-2c66-4663-b5c4-2a332e51b2d9', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '04165151-b7fd-4237-81ad-b2e2e00b46b5', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('4b94e6b7-038f-4384-9c21-fcee7c2d3313', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '04165151-b7fd-4237-81ad-b2e2e00b46b5', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('1ee98a35-1b03-4e98-8d21-0702315f132a', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '03d1b341-fba4-4a86-a46e-150cc39e389b', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('274027a1-c023-4e78-9668-dc2fd13fa0a2', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '03d1b341-fba4-4a86-a46e-150cc39e389b', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('e1bcd21d-3e8a-41c6-b822-852c22c0c126', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '03d1b341-fba4-4a86-a46e-150cc39e389b', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('3d64c9f3-ca11-461e-815c-c76390a6c103', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'ef02e47b-dada-4474-a74e-72bed831a429', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('2581a37c-f8b7-4bad-8f17-97c5c2ffd567', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'ef02e47b-dada-4474-a74e-72bed831a429', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('1c35f9be-aa83-4e13-8baa-7d3d6d87bf07', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'd366402c-2129-4409-a88f-a26969823581', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('197abb59-f401-4243-975f-dda2a87af010', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'd366402c-2129-4409-a88f-a26969823581', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('d8890fb9-214b-415e-aaac-5b50b2647e77', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'd366402c-2129-4409-a88f-a26969823581', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('2351d981-477c-4453-9662-fe8b66551ae6', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'e3684dbf-c2bf-44f8-8746-d2493dd206fb', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('5ca11daa-5d43-4c24-a7a8-fc2f5b3bb9fd', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'e3684dbf-c2bf-44f8-8746-d2493dd206fb', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('a99a05c8-1865-4ab6-9436-73aed8458532', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '5d876465-bcf9-4395-a7f5-36e73db3a674', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('cf646c43-b2fc-4e98-8ff4-34409169baef', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '5d876465-bcf9-4395-a7f5-36e73db3a674', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('5d2f1326-08ae-4ea2-9ad9-92694b0c3c12', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '6325a0a5-7d04-421a-9bc6-91e1a0f96592', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('f85d2186-ab3e-44a4-af64-d9a85fbd7bdf', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '6325a0a5-7d04-421a-9bc6-91e1a0f96592', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('cfaae0de-bfb5-4839-8b7d-0762caa13901', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'e17759b4-8f91-440f-8e5f-77a8daef928c', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('723f4eba-32cf-4029-94b6-734b92abbf7d', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'e17759b4-8f91-440f-8e5f-77a8daef928c', 2);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('2ef7b909-f97c-4cab-9756-133d2e3a3972', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'e17759b4-8f91-440f-8e5f-77a8daef928c', 3);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('35ce4cec-5989-4914-9b2e-bcbec34ee3fc', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, '970b0c66-b1e7-45d4-8586-7ff2c83964f0', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('8e4df39c-3a42-4979-b6d1-a6d993169ed9', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, '970b0c66-b1e7-45d4-8586-7ff2c83964f0', 2);

INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('e5338dec-5890-48d4-9d57-6ea57925d69f', NULL, '2018-04-17 16:43:56', NULL, 1, NULL, 'a233ef30-cc59-4f04-a035-892da44126c1', 1);
INSERT INTO `choice` (`id`,`created_date`,`updated_date`,`description`,`is_correct`,`ivr_name`,`question_id`,`choice_id`) VALUES ('310c3c06-5828-4f40-834c-a8bcc03d1d90', NULL, '2018-04-17 16:43:56', NULL, 0, NULL, 'a233ef30-cc59-4f04-a035-892da44126c1', 2);
