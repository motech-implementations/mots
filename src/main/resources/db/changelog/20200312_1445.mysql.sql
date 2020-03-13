--liquibase formatted sql

--changeset pmuchowski:1584020754864-1
ALTER TABLE course ADD choose_module_question_ivr_id VARCHAR(255) NULL;

--changeset pmuchowski:1584020754864-2
ALTER TABLE unit ADD continuation_question_ivr_id VARCHAR(255) NULL;

--changeset pmuchowski:1584020754864-3
ALTER TABLE course ADD menu_intro_message_ivr_id VARCHAR(255) NULL;

--changeset pmuchowski:1584020754864-4
ALTER TABLE course ADD no_modules_message_ivr_id VARCHAR(255) NULL;

--changeset pmuchowski:1584020754864-6
ALTER TABLE module DROP FOREIGN KEY FKej0l2sdumgw7jwtk64stuh7gm;

--changeset pmuchowski:1584020754864-7
ALTER TABLE course DROP FOREIGN KEY FKn3401kdg0uu7frmte7s569rw1;

--changeset pmuchowski:1584020754864-8
ALTER TABLE unit DROP FOREIGN KEY FKqte1bg2x6d5vu3g4qujmvhsod;

--changeset pmuchowski:1584020754864-9
ALTER TABLE unit DROP COLUMN continuation_question_id;

--changeset pmuchowski:1584020754864-10
ALTER TABLE course DROP COLUMN no_modules_message_id;

--changeset pmuchowski:1584020754864-11
ALTER TABLE module DROP COLUMN start_module_question_id;

--changeset pmuchowski:1584020754864-12
ALTER TABLE course_module ADD start_module_question_ivr_id VARCHAR(255) NULL;

