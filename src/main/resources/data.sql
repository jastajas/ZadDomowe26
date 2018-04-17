INSERT INTO city (id, name) VALUES (1, 'Wrocław');
INSERT INTO city (id, name) VALUES (2, 'Warszawa');
INSERT INTO city (id, name) VALUES (3, 'Kraków');
INSERT INTO city (id, name) VALUES (4, 'Gdańsk');
INSERT INTO city (id, name) VALUES (5, 'Poznań');
INSERT INTO city (id, name) VALUES (6, 'Katowice');
INSERT INTO city (id, name) VALUES (7, 'Szczecin');

INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (1, 'jasta', '$2a$10$VzL39eN.KaBKGtyd74pBzuiAhJHBJYsaaN85lCaMOz7cfCNetgKLW', TRUE, 'Jan', 'Kowalski', '1980-05-13', 'Kołątaja', '23a', 'Wrocław', '23-098');
INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (2, 'pimpek', '$2a$10$OoDjC2PHobapgL7wSG5wUOdO97S7pL7VTqQ3GCZdTalPp0iDMQrTG', TRUE, 'Marian', 'Nowek', '1986-03-11', 'Piłsudzkiego', '12', 'Poznań', '11-289');
INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (3, 'wafel', '$2a$10$erT.IcqB2iLxxUULD5nx0eVndmBLosJChQWhFIUkLIV/SRnxNaeuS', TRUE, 'Edek', 'Zgredek', '1990-01-03', 'Żmigrodzka', '129', 'Warszawa', '55-123');
INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (4, 'blondie', '$2a$10$OhVQ5e.rb2ieIsh50B.S7Ocp4Pq7x/Fr3yqmfJUg5iBd5vjiM79fm', TRUE, 'Zosia', 'Abacka', '1977-12-30', 'Lotnicza', '2b', 'Katowice', '44-321');
INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (5, 'strzała', '$2a$10$mx0UljzUU8eT1hZi93SfouNALZFP9.wP/P9GrFA.p2pL485siMrNa', TRUE, 'Kasia', 'Bednarz', '1995-09-07', 'Wiśniowa', '45', 'Kraków', '68-671');
INSERT INTO user (id, username, password, enabled, name, surname, born_date, street, number, city, post_code) VALUES (6, 'admin', '$2a$10$MSPSQbqqsLNQmFNCiXDLt.VyKglbkJ1ajCTflBtGKOz1Z7jHk5myW', TRUE, 'Jacek', 'Placek', '1986-08-12', 'Kasztanowa', '7', 'Wrocław', '54-123');

INSERT INTO USER_ROLE (id, username, role) VALUES (1, 'admin', 'ROLE_ADMIN');
INSERT INTO USER_ROLE (id, username, role) VALUES (2, 'jasta', 'ROLE_USER');
INSERT INTO USER_ROLE (id, username, role) VALUES (3, 'pimpek', 'ROLE_USER');
INSERT INTO USER_ROLE (id, username, role) VALUES (4, 'wafel', 'ROLE_USER');
INSERT INTO USER_ROLE (id, username, role) VALUES (5, 'blondie', 'ROLE_USER');
INSERT INTO USER_ROLE (id, username, role) VALUES (6, 'strzała', 'ROLE_USER');
INSERT INTO USER_ROLE (id, username, role) VALUES (7, 'admin', 'ROLE_USER');

INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (1,1,2, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (2,1,3, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (3,1,4, FALSE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (4,5,1, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (5,2,3, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (6,2,4, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (7,6,2, FALSE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (8,6,3, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (9,5,2, TRUE );
INSERT INTO relation (id, user_initial_id, user_invited_id, confirmed) VALUES (10,3,5, FALSE );
