--liquibase formatted sql

--changeset pmuchowski:1519812660070-1
CREATE TABLE call_flow_element_log (id VARCHAR(255) NOT NULL, created_date datetime NULL, updated_date datetime NULL, end_date datetime NULL, start_date datetime NULL, call_flow_element_id VARCHAR(255) NOT NULL, unit_progress_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1519812660070-2
CREATE TABLE message_log (call_flow_element_log_id VARCHAR(255) NOT NULL);

--changeset pmuchowski:1519812660070-3
CREATE TABLE multiple_choice_question_log (number_of_attempts INT NOT NULL, call_flow_element_log_id VARCHAR(255) NOT NULL, response_id VARCHAR(255) NULL);

--changeset pmuchowski:1519812660070-4
ALTER TABLE call_flow_element_log ADD PRIMARY KEY (id);

--changeset pmuchowski:1519812660070-5
ALTER TABLE message_log ADD PRIMARY KEY (call_flow_element_log_id);

--changeset pmuchowski:1519812660070-6
ALTER TABLE multiple_choice_question_log ADD PRIMARY KEY (call_flow_element_log_id);

--changeset pmuchowski:1519812660070-7
ALTER TABLE call_flow_element_log ADD CONSTRAINT FK7uxgn0fe0tf4mcbf7rb7pcf1c FOREIGN KEY (unit_progress_id) REFERENCES unit_progress (id);

--changeset pmuchowski:1519812660070-8
ALTER TABLE message_log ADD CONSTRAINT FK8mmo4p9rxt2jjioct3q3vcaio FOREIGN KEY (call_flow_element_log_id) REFERENCES call_flow_element_log (id);

--changeset pmuchowski:1519812660070-9
ALTER TABLE multiple_choice_question_log ADD CONSTRAINT FKgx1boftsm9kfq5fq3v3ywtaq6 FOREIGN KEY (call_flow_element_log_id) REFERENCES call_flow_element_log (id);

--changeset pmuchowski:1519812660070-10
ALTER TABLE multiple_choice_question_log ADD CONSTRAINT FKraoecc8g2enm2nb8f4wjkcoq2 FOREIGN KEY (response_id) REFERENCES choice (id);

--changeset pmuchowski:1519812660070-11
ALTER TABLE call_flow_element_log ADD CONSTRAINT FKth2klq4eqfmp825vboeyblypn FOREIGN KEY (call_flow_element_id) REFERENCES call_flow_element (id);

--changeset pmuchowski:1519812660070-12
ALTER TABLE question_response DROP FOREIGN KEY FK38jlskoqmxu97ma7qywfvud1t;

--changeset pmuchowski:1519812660070-13
ALTER TABLE question_response DROP FOREIGN KEY FKcn5kg8v65bk53it5wykkncrmw;

--changeset pmuchowski:1519812660070-14
ALTER TABLE question_response DROP FOREIGN KEY FKhfrr5otnbipe5kd6w5qx3t9t9;

--changeset pmuchowski:1519812660070-15
DROP TABLE question_response;

