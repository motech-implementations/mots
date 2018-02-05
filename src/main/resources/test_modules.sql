INSERT INTO `call_flow_element` (`id`, `list_order`, `name`, `type`) VALUES('03f8f5a7-045e-4810-ac4e-30d2beda82ae', 0, 'start module 1', 'QUESTION');
INSERT INTO `call_flow_element` (`id`, `list_order`, `name`, `type`) VALUES('03ff01a2-b413-4b19-b5e2-e3dcc652583b', 0, 'start module 2', 'QUESTION');
INSERT INTO `call_flow_element` (`id`, `list_order`, `name`, `type`) VALUES('04ea4726-2aef-459a-8a34-8ccd12613396', 0, 'start module 3', 'QUESTION');

INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('03f8f5a7-045e-4810-ac4e-30d2beda82ae');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('03ff01a2-b413-4b19-b5e2-e3dcc652583b');
INSERT INTO `multiple_choice_question` (`call_flow_element_id`) VALUES ('04ea4726-2aef-459a-8a34-8ccd12613396');

INSERT INTO `module` (`id`,`create_date`,`update_date`,`ivr_id`,`ivr_name`,`description`,`module_number`,`name`,`status`,`version`,`previous_version_id`,`start_module_question_id`,`name_code`,`ivr_group`) VALUES ('2dcaa2da-edc4-42b0-a505-167d327aaf06','2017-11-14 16:07:22.745000','2017-11-14 16:07:22.745000','20229','module1',NULL,1,'Module 1','RELEASED',1,NULL,'03f8f5a7-045e-4810-ac4e-30d2beda82ae','module-1_v1','298829');
INSERT INTO `module` (`id`,`create_date`,`update_date`,`ivr_id`,`ivr_name`,`description`,`module_number`,`name`,`status`,`version`,`previous_version_id`,`start_module_question_id`,`name_code`,`ivr_group`) VALUES ('3ccbb0b8-0e5a-4e97-9bb2-8b934e12777d','2017-11-14 16:07:22.770000','2017-11-14 16:07:22.770000','20230','module2',NULL,2,'Module 2','RELEASED',1,NULL,'03ff01a2-b413-4b19-b5e2-e3dcc652583b','module-2_v1','298830');
INSERT INTO `module` (`id`,`create_date`,`update_date`,`ivr_id`,`ivr_name`,`description`,`module_number`,`name`,`status`,`version`,`previous_version_id`,`start_module_question_id`,`name_code`,`ivr_group`) VALUES ('766eb0c6-da6b-43e5-8b6b-eff89e58223f','2017-11-14 16:07:22.777000','2017-11-14 16:07:22.777000','20231','module3',NULL,3,'Module 3','RELEASED',1,NULL,'04ea4726-2aef-459a-8a34-8ccd12613396','module-3_v1','298831');
