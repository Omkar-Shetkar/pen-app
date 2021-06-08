INSERT INTO language (id, name) VALUES (1, 'Kannada');
INSERT INTO language (id, name) VALUES (2, 'English');
INSERT INTO language (id, name) VALUES (3, 'Hindi');
INSERT INTO language (id, name) VALUES (4, 'Tamil');
INSERT INTO language (id, name) VALUES (5, 'Telugu');

INSERT INTO exam (id, name) VALUES (101, 'SSLC');
INSERT INTO exam (id, name) VALUES (102, 'PUC');
INSERT INTO exam (id, name) VALUES (103, 'BSc');

-------------------------------------------------------------
INSERT INTO candidate (id, first_name, last_name, gender, phone)
               VALUES (1, 'candidate1-fn','candidate1-ln', 'MALE', '123-111');
INSERT INTO candidate (id, first_name, last_name, gender, phone)
               VALUES (2, 'candidate2-fn','candidate2-ln', 'FEMALE', '456-222');
INSERT INTO candidate (id, first_name, last_name, gender, phone)
               VALUES (3, 'candidate3-fn','candidate3-ln', 'MALE', '789-333');

-------------------------------------------------------------

INSERT INTO scribe (id, first_name, last_name, gender, phone, email, location)
            VALUES (1, 'scribe1-fn','scribe1-ln', 'MALE', '111-111', 'scribe01@gmail.com', 100);
INSERT INTO scribe (id, first_name, last_name, gender, phone, email, location)
            VALUES (2, 'scribe2-fn','scribe2-ln', 'FEMALE', '222-222', 'scribe02@gmail.com', 200);
INSERT INTO scribe (id, first_name, last_name, gender, phone, email, location)
            VALUES (3, 'scribe3-fn','scribe3-ln', 'MALE', '333-333', 'scribe03@gmail.com', 300);

INSERT INTO scribe_language(scribe, language) VALUES (1, 1);
INSERT INTO scribe_language(scribe, language) VALUES (1, 2);
INSERT INTO scribe_language(scribe, language) VALUES (2, 1);
INSERT INTO scribe_language(scribe, language) VALUES (3, 3);

-------------------------------------------------------------

INSERT INTO availability(scribe, available_from, available_to) VALUES (1, '2020-12-24 06:00:00', '2020-12-24 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (2, '2020-12-24 06:00:00', '2020-12-24 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (2, '2020-12-25 06:00:00', '2020-12-25 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (1, '2020-12-26 06:00:00', '2020-12-26 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (2, '2020-12-26 06:00:00', '2020-12-26 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (3, '2020-12-27 06:00:00', '2020-12-27 10:00:00');
INSERT INTO availability(scribe, available_from, available_to) VALUES (3, '2020-12-28 06:00:00', '2020-12-28 10:00:00');

-------------------------------------------------------------

INSERT INTO country(id, name) VALUES (1, 'India');
INSERT INTO country(id, name) VALUES (2, 'USA');

INSERT INTO state(id, name, country) VALUES (1, 'Karnataka', 1);
INSERT INTO state(id, name, country) VALUES (2, 'NYC', 2);

INSERT INTO city(id, name, state) VALUES (1, 'Bangalore', 1);
INSERT INTO city(id, name, state) VALUES (2, 'NY', 2);

INSERT INTO address(id, city, state, country, pincode, gps_location) VALUES (1, 1, 1,1,'560102','location-01');
INSERT INTO address(id, city, state, country, pincode, gps_location) VALUES (2, 2, 2,2,'110882','location-02');

-------------------------------------------------------------
