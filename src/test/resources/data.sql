INSERT INTO language (id, name) VALUES (1, 'Kannada');
INSERT INTO language (id, name) VALUES (2, 'English');
INSERT INTO language (id, name) VALUES (3, 'Hindi');
INSERT INTO language (id, name) VALUES (4, 'Tamil');
INSERT INTO language (id, name) VALUES (5, 'Telugu');

INSERT INTO exam (id, name) VALUES (101, 'SSLC');
INSERT INTO exam (id, name) VALUES (102, 'PUC');
INSERT INTO exam (id, name) VALUES (103, 'BSc');

-------------------------------------------------------------
INSERT INTO candidate (id, first_name, last_name, gender, mobile)
               VALUES (1, 'candidate1-fn','candidate1-ln', 'MALE', '123-111');
INSERT INTO candidate (id, first_name, last_name, gender, mobile)
               VALUES (2, 'candidate2-fn','candidate2-ln', 'FEMALE', '456-222');
INSERT INTO candidate (id, first_name, last_name, gender, mobile)
               VALUES (3, 'candidate3-fn','candidate3-ln', 'MALE', '789-333');

-------------------------------------------------------------

INSERT INTO scribe (id, first_name, last_name, gender, mobile, email, location, availability)
            VALUES (1, 'scribe1-fn','scribe1-ln', 'MALE', '111-111', 'scribe01@gmail.com', 100, 'WEEKENDS');
INSERT INTO scribe (id, first_name, last_name, gender, mobile, email, location, availability)
            VALUES (2, 'scribe2-fn','scribe2-ln', 'FEMALE', '222-222', 'scribe02@gmail.com', 200, 'WEEKDAYS');
INSERT INTO scribe (id, first_name, last_name, gender, mobile, email, location, availability)
            VALUES (3, 'scribe3-fn','scribe3-ln', 'MALE', '333-333', 'scribe03@gmail.com', 300, 'ANY_DAY');
--INSERT INTO scribe (id, first_name, last_name, gender, mobile, email, location, availability)
--            VALUES (4, 'scribe4-fn','scribe4-ln', 'MALE', '444-444', 'scribe04@gmail.com', 300, 'NOT_AVAILABLE');

INSERT INTO scribe_language(scribe, language) VALUES (1, 1);
INSERT INTO scribe_language(scribe, language) VALUES (1, 2);
INSERT INTO scribe_language(scribe, language) VALUES (2, 1);
INSERT INTO scribe_language(scribe, language) VALUES (3, 3);

-------------------------------------------------------------

INSERT INTO country(id, name) VALUES (1, 'India');
INSERT INTO country(id, name) VALUES (2, 'USA');

INSERT INTO state(id, name, country) VALUES (1, 'Karnataka', 1);
INSERT INTO state(id, name, country) VALUES (2, 'NYC', 2);

INSERT INTO city(id, name, state) VALUES (1, 'Bangalore', 1);
INSERT INTO city(id, name, state) VALUES (2, 'Mysore', 1);
INSERT INTO city(id, name, state) VALUES (3, 'Bidar', 1);
INSERT INTO city(id, name, state) VALUES (4, 'NY', 2);

INSERT INTO address(id, city, state, country, pincode, gps_location) VALUES (1, 1, 1,1,'560102','location-01');
INSERT INTO address(id, city, state, country, pincode, gps_location) VALUES (2, 2, 2,2,'110882','location-02');

-------------------------------------------------------------
