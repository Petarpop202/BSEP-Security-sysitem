insert into role (name) values ('ROLE_HUMAN_RESOURCE_MANAGER');
insert into role (name) values ('ROLE_ENGINEER');
insert into role (name) values ('ROLE_PROJECT_MANAGER');
insert into role (name) values ('ROLE_ADMINISTRATOR');
insert into role (name) values ('ROLE_GUEST');

insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 1', '11');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 2', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 3', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 4', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 5', '22');

insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Danilo', 'Bulatović', 0, 'humanResoureManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReply@gmail.com', '065/123-456', 1, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Stefan', 'Lepsanovic', 0, 'engineer', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls@gmail.com', '065/123-456', 2, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Petar', 'Popovic', 0, 'projectManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls1@gmail.com', '065/123-456', 2, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Admin', 'Adminovic', 0, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReplasdy@gmail.com', '065/123-456', 1, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('guest', 'guest', 0, 'guest', '', '', true, '', '', 1, true);


-- DODAVANJE INZENJERA
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Inzenjer1', 'Inzenjer1', 0, 'engineer1', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789111', true, 'engineer1@gmail.com', '065/123-111', 3, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Inzenjer2', 'Inzenjer2', 0, 'engineer2', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789222', true, 'engineer2@gmail.com', '065/123-222', 4, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Inzenjer3', 'Inzenjer3', 0, 'engineer3', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789333', true, 'engineer3@gmail.com', '065/123-333', 5, true);
insert into engineers (id) values (2);
insert into engineers (id) values (6);
insert into engineers (id) values (7);
insert into engineers (id) values (8);
INSERT INTO user_role (user_id, role_id) VALUES (6, 2);
INSERT INTO user_role (user_id, role_id) VALUES (7, 2);
INSERT INTO user_role (user_id, role_id) VALUES (8, 2);

insert into managers (id) values (3);
insert into system_administrators (id) values (4);



INSERT INTO user_role (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu HUMAN_RESOURCE_MANAGER
INSERT INTO user_role (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ENGINEER
INSERT INTO user_role (user_id, role_id) VALUES (3, 3); -- user-u dodeljujemo rolu PROJECT_MANAGER
INSERT INTO user_role (user_id, role_id) VALUES (4, 4); -- user-u dodeljujemo rolu ADMINISTRATOR
INSERT INTO user_role (user_id, role_id) VALUES (5, 5); -- user-u dodeljujemo rolu GUEST

insert into projects (name, start_date, end_date, manager_id) values ('BSEP', '2022-10-22', '2023-10-22', 3);

insert into employees (description, end_date, start_date, engineer_id) values ('backend developer', '2023-10-22', '2022-10-22', 2);
insert into employees (description, end_date, start_date, engineer_id) values ('fronted developer', '2023-10-22', '2022-10-22', 6);
insert into employees (description, end_date, start_date, engineer_id) values ('java developer', '2023-10-22', '2022-10-22', 7);
insert into employees (description, end_date, start_date, engineer_id) values ('dotnet developer', '2023-10-22', '2022-10-22', 8);

