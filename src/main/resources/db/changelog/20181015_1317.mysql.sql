--liquibase formatted sql

--changeset jkondrat:1539602256869-1
ALTER TABLE multiple_choice_question ADD question_type VARCHAR(255);

--changeset jkondrat:1539602256869-2
UPDATE multiple_choice_question question
 JOIN call_flow_element element ON element.id = question.call_flow_element_id
 JOIN unit ON unit.id = element.unit_id
SET question_type = 'PRE_TEST'
 WHERE unit.list_order = 0;

--changeset jkondrat:1539602256869-3
UPDATE multiple_choice_question SET question_type = 'POST_TEST' WHERE question_type IS NULL;

--changeset jkondrat:1539602256869-4
ALTER TABLE multiple_choice_question MODIFY question_type VARCHAR(255) NOT NULL;
