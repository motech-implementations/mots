--liquibase formatted sql

--changeset kcissewski:1530706554533-1
UPDATE multiple_choice_question_log
SET number_of_attempts = 4
WHERE number_of_attempts = 5;

