insert into role (name) values ('ROLE_HUMAN_RESOURCE_MANAGER');
insert into role (name) values ('ROLE_ENGINEER');
insert into role (name) values ('ROLE_PROJECT_MANAGER');
insert into role (name) values ('ROLE_ADMINISTRATOR');

insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 1', '11');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 2', '22');

insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Danilo', 'Bulatović', 0, 'humanResoureManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReply@gmail.com', '065/123-456', 1, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Stefan', 'Lepsanovic', 0, 'engineer', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls@gmail.com', '065/123-456', 2, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Petar', 'Popovic', 0, 'projectManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls1@gmail.com', '065/123-456', 2, true);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved) values ('Admin', 'Adminovic', 0, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReplasdy@gmail.com', '065/123-456', 1, true);

INSERT INTO user_role (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu HUMAN_RESOURCE_MANAGER
INSERT INTO user_role (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ENGINEER
INSERT INTO user_role (user_id, role_id) VALUES (3, 3); -- user-u dodeljujemo rolu PROJECT_MANAGER
INSERT INTO user_role (user_id, role_id) VALUES (4, 4); -- user-u dodeljujemo rolu ADMINISTRATOR