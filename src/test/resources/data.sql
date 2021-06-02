insert into language (id, name) values (1, 'Kannada');
insert into language (id, name) values (2, 'English');
insert into language (id, name) values (3, 'Hindi');
insert into language (id, name) values (4, 'Tamil');
insert into language (id, name) values (5, 'Telugu');

-------------------------------------------------------------
insert into candidate (id, first_name, last_name, gender, phone)
               values (1, 'candidate1-fn','candidate1-ln', 'MALE', '123-111');
insert into candidate (id, first_name, last_name, gender, phone)
               values (2, 'candidate2-fn','candidate2-ln', 'FEMALE', '456-222');
insert into candidate (id, first_name, last_name, gender, phone)
               values (3, 'candidate3-fn','candidate3-ln', 'MALE', '789-333');

-------------------------------------------------------------

insert into scribe (id, first_name, last_name, gender, phone, email, location)
            values (1, 'scribe1-fn','scribe1-ln', 'MALE', '111-111', 'scribe01@gmail.com', 100);
insert into scribe (id, first_name, last_name, gender, phone, email, location)
            values (2, 'scribe2-fn','scribe2-ln', 'FEMALE', '222-222', 'scribe02@gmail.com', 200);
insert into scribe (id, first_name, last_name, gender, phone, email, location)
            values (3, 'scribe3-fn','scribe3-ln', 'MALE', '333-333', 'scribe03@gmail.com', 300);


insert into scribe_language(scribe, language) values (1, 1);
insert into scribe_language(scribe, language) values (1, 2);
insert into scribe_language(scribe, language) values (2, 1);
insert into scribe_language(scribe, language) values (3, 3);

-------------------------------------------------------------

insert into availability(scribe, available_from, available_to) values (1, '2020-12-24 06:00:00', '2020-12-24 10:00:00');
insert into availability(scribe, available_from, available_to) values (1, '2020-12-24 06:00:00', '2020-12-24 10:00:00');
insert into availability(scribe, available_from, available_to) values (2, '2020-12-25 06:00:00', '2020-12-25 10:00:00');
insert into availability(scribe, available_from, available_to) values (1, '2020-12-26 06:00:00', '2020-12-26 10:00:00');
insert into availability(scribe, available_from, available_to) values (2, '2020-12-26 06:00:00', '2020-12-26 10:00:00');
insert into availability(scribe, available_from, available_to) values (3, '2020-12-27 06:00:00', '2020-12-27 10:00:00');
insert into availability(scribe, available_from, available_to) values (3, '2020-12-28 06:00:00', '2020-12-28 10:00:00');

-------------------------------------------------------------
-------------------------------------------------------------
