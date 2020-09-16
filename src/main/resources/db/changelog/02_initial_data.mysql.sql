--liquibase formatted sql

--changeset kdondziak:1600079823224-1

INSERT INTO client (client_id,access_token_validity_seconds,additional_information,authorities,authorized_grant_types,client_secret,refresh_token_validity_seconds,registered_redirect_uris,resource_ids,`scope`) VALUES
('trusted-client',NULL,NULL,'TRUSTED_CLIENT','password,refresh_token','{noop}secret',NULL,NULL,'mots','read,write')
;


INSERT INTO user_permission (id,created_date,updated_date,display_name,name,readonly) VALUES
('038f5310-9d4a-4fbb-b474-44bb352d51fd',NULL,NULL,'Manage Users','ROLE_MANAGE_USERS',1)
                                                                                             ,('0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f',NULL,NULL,'Display Reports','ROLE_DISPLAY_REPORTS',1)
                                                                                             ,('0bb95c4e-9e1a-40cd-8fa0-9879c419a507',NULL,NULL,'Admin','ROLE_ADMIN',1)
                                                                                             ,('0f5f40f2-be3d-4509-af61-fa591e504295',NULL,NULL,'Manage Locations','ROLE_MANAGE_FACILITIES',1)
                                                                                             ,('1078f9ec-8b05-4db5-b1d1-78a622773983',NULL,NULL,'Upload CHW CSV','ROLE_UPLOAD_CHW_CSV',1)
                                                                                             ,('1f45915b-2fb4-4f9e-be47-af2ac1bb5c81',NULL,NULL,'Display CHWs','ROLE_CHW_READ',1)
                                                                                             ,('2eb6d006-52a0-49db-b5ff-a8beb9464cf2',NULL,NULL,'Manage Modules','ROLE_MANAGE_MODULES',1)
                                                                                             ,('39773f50-d881-4fc9-bde3-ae6965cbb18c',NULL,NULL,'Manage Own Locations','ROLE_MANAGE_OWN_FACILITIES',1)
                                                                                             ,('39afbbe4-5854-418b-8ce7-4621980bcdfd',NULL,NULL,'Create Locations','ROLE_CREATE_FACILITIES',1)
                                                                                             ,('3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5',NULL,NULL,'Create/Edit CHW','ROLE_CHW_WRITE',1)
;
INSERT INTO user_permission (id,created_date,updated_date,display_name,name,readonly) VALUES
('3f273730-a2c3-4208-adaf-a8839b78655e',NULL,NULL,'Display Groups','ROLE_GROUP_READ',1)
                                                                                             ,('6ab183c6-b091-48b1-98f9-869af54dcd1d',NULL,NULL,'Display Locations','ROLE_DISPLAY_FACILITIES',1)
                                                                                             ,('86a7adf7-8287-4233-972b-05662444bfa7',NULL,NULL,'Upload Location CSV','ROLE_UPLOAD_LOCATION_CSV',1)
                                                                                             ,('8b42ec54-6675-4fd0-901c-d30ad5f41cf7',NULL,NULL,'Assign Modules','ROLE_ASSIGN_MODULES',1)
                                                                                             ,('9206c12a-9ad8-4082-b1f3-cdda9b2c57df',NULL,NULL,'Display Modules','ROLE_DISPLAY_MODULES',1)
                                                                                             ,('adc5e376-5993-4699-9d09-5a415b9329fe',NULL,NULL,'Create/Edit Group','ROLE_GROUP_WRITE',1)
;

INSERT INTO user_role (id,created_date,updated_date,name,readonly) VALUES
('a894d04f-a653-4745-bf11-f76e3cfb228b',NULL,NULL,'Level 1',0)
                                                                          ,('d892e3c6-25eb-4291-abfc-b379aa502bc3',NULL,NULL,'Admin',1)
                                                                          ,('da5d8474-637d-446d-a221-80de009a6f6c',NULL,NULL,'Level 2',0)
;


INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('038f5310-9d4a-4fbb-b474-44bb352d51fd','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('038f5310-9d4a-4fbb-b474-44bb352d51fd','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('0b1b6dc5-2ccc-4354-9a5c-28255eea8d3f','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('0bb95c4e-9e1a-40cd-8fa0-9879c419a507','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('0f5f40f2-be3d-4509-af61-fa591e504295','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('0f5f40f2-be3d-4509-af61-fa591e504295','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('1078f9ec-8b05-4db5-b1d1-78a622773983','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('1078f9ec-8b05-4db5-b1d1-78a622773983','d892e3c6-25eb-4291-abfc-b379aa502bc3')
;
INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('1078f9ec-8b05-4db5-b1d1-78a622773983','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('1f45915b-2fb4-4f9e-be47-af2ac1bb5c81','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('1f45915b-2fb4-4f9e-be47-af2ac1bb5c81','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('1f45915b-2fb4-4f9e-be47-af2ac1bb5c81','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('2eb6d006-52a0-49db-b5ff-a8beb9464cf2','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('2eb6d006-52a0-49db-b5ff-a8beb9464cf2','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('39773f50-d881-4fc9-bde3-ae6965cbb18c','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('39773f50-d881-4fc9-bde3-ae6965cbb18c','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('39afbbe4-5854-418b-8ce7-4621980bcdfd','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('39afbbe4-5854-418b-8ce7-4621980bcdfd','d892e3c6-25eb-4291-abfc-b379aa502bc3')
;
INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('39afbbe4-5854-418b-8ce7-4621980bcdfd','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('3c95a42a-7f1f-4c72-8cb4-6c9f9b144fe5','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('3f273730-a2c3-4208-adaf-a8839b78655e','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('6ab183c6-b091-48b1-98f9-869af54dcd1d','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('6ab183c6-b091-48b1-98f9-869af54dcd1d','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('6ab183c6-b091-48b1-98f9-869af54dcd1d','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('86a7adf7-8287-4233-972b-05662444bfa7','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('8b42ec54-6675-4fd0-901c-d30ad5f41cf7','a894d04f-a653-4745-bf11-f76e3cfb228b')
;
INSERT INTO user_role_permissions (permission_id,role_id) VALUES
('8b42ec54-6675-4fd0-901c-d30ad5f41cf7','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('8b42ec54-6675-4fd0-901c-d30ad5f41cf7','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('9206c12a-9ad8-4082-b1f3-cdda9b2c57df','a894d04f-a653-4745-bf11-f76e3cfb228b')
                                                                 ,('9206c12a-9ad8-4082-b1f3-cdda9b2c57df','d892e3c6-25eb-4291-abfc-b379aa502bc3')
                                                                 ,('9206c12a-9ad8-4082-b1f3-cdda9b2c57df','da5d8474-637d-446d-a221-80de009a6f6c')
                                                                 ,('adc5e376-5993-4699-9d09-5a415b9329fe','d892e3c6-25eb-4291-abfc-b379aa502bc3')
;

INSERT INTO user (id,created_date,updated_date,email,enabled,name,password,username) VALUES
('51f6bdc1-4932-4bc3-9589-368646ef7ad3',NULL,NULL,'test@mots.org',1,NULL,'{bcrypt}$2a$10$4IZfidcJzbR5Krvj87ZJdOZvuQoD/kvPAJe549rUNoP3N3uH0Lq2G','admin')
;

INSERT INTO users_roles (user_id,role_id) VALUES
('51f6bdc1-4932-4bc3-9589-368646ef7ad3','d892e3c6-25eb-4291-abfc-b379aa502bc3')
;
