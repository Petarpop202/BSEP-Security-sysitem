insert into role (name) values ('ROLE_HUMAN_RESOURCE_MANAGER');
insert into role (name) values ('ROLE_ENGINEER');
insert into role (name) values ('ROLE_PROJECT_MANAGER');
insert into role (name) values ('ROLE_ADMINISTRATOR');
insert into role (name) values ('ROLE_GUEST');

-- AuthenticationController
insert into permission (name) values ('LOGIN');
insert into permission (name) values ('PASSWORDLESS_LOGIN');
insert into permission (name) values ('PASSWORDLESS_LOGIN_ACTIVATE');
insert into permission (name) values ('SIGNUP');
insert into permission (name) values ('RESPONSE');
insert into permission (name) values ('ACTIVATE_USER');
insert into permission (name) values ('REFRESH_ACCESS_TOKEN');

-- EmployeeController
insert into permission (name) values ('GET_EMPLOYEE_ALL');
insert into permission (name) values ('GET_EMPLOYEE_ONE');
insert into permission (name) values ('CREATE_EMPLOYEE');
insert into permission (name) values ('UPDATE_EMPLOYEE');
insert into permission (name) values ('UPDATE_EMPLOYEE_DESC');
insert into permission (name) values ('DELETE_EMPLOYEE');
insert into permission (name) values ('GET_EMPLOYEES_BY_PROJECT');
insert into permission (name) values ('GET_PROJECTS_BY_EMPLOYEE');

-- EngineerController
insert into permission (name) values ('GET_ENGINEER_ALL');
insert into permission (name) values ('GET_ENGINEER_ONE');
insert into permission (name) values ('CREATE_ENGINEER');
insert into permission (name) values ('UPDATE_ENGINEER');
insert into permission (name) values ('UPDATE_ENGINEER_SKILLS');
insert into permission (name) values ('DELETE_ENGINEER');

-- ManagerController
insert into permission (name) values ('GET_MANAGER_ALL');
insert into permission (name) values ('GET_MANAGER_ONE');
insert into permission (name) values ('CREATE_MANAGER');
insert into permission (name) values ('UPDATE_MANAGER');
insert into permission (name) values ('DELETE_MANAGER');

-- ProjectController
insert into permission (name) values ('GET_PROJECT_ALL');
insert into permission (name) values ('GET_PROJECT_ONE');
insert into permission (name) values ('CREATE_PROJECT');
insert into permission (name) values ('GET_PROJECTS_BY_MANAGER');
insert into permission (name) values ('ADD_EMPLOYEE_TO_PROJECT');
insert into permission (name) values ('REMOVE_EMPLOYEE_FROM_PROJECT');
insert into permission (name) values ('DELETE_PROJECT');

-- RoleController
insert into permission (name) values ('GET_ROLE_ALL');
insert into permission (name) values ('GET_PERMISSION_ALL');
insert into permission (name) values ('ADD_PERMISSION_TO_ROLE');
insert into permission (name) values ('REMOVE_PERMISSION_FROM_ROLE');

-- SystemAdministratorController
insert into permission (name) values ('GET_ADMIN_ALL');
insert into permission (name) values ('GET_ADMIN_ONE');
insert into permission (name) values ('UPDATE_ADMIN');
insert into permission (name) values ('DELETE_ADMIN');

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

insert into project_employee (project_id, employee_id) values (1, 1)