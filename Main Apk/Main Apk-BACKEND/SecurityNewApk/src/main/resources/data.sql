insert into role (name) values ('ROLE_HUMAN_RESOURCE_MANAGER');
insert into role (name) values ('ROLE_ENGINEER');
insert into role (name) values ('ROLE_PROJECT_MANAGER');
insert into role (name) values ('ROLE_ADMINISTRATOR');
insert into role (name) values ('ROLE_GUEST');

-- AuthenticationController
insert into permission (name) values ('LOGIN'); -- 1
insert into permission (name) values ('PASSWORDLESS_LOGIN'); -- 2
insert into permission (name) values ('PASSWORDLESS_LOGIN_ACTIVATE'); -- 3
insert into permission (name) values ('SIGNUP'); -- 4
insert into permission (name) values ('RESPONSE'); -- 5
insert into permission (name) values ('ACTIVATE_USER'); -- 6
insert into permission (name) values ('REFRESH_ACCESS_TOKEN'); -- 7
insert into permission (name) values ('GET_REQUESTS'); -- 8

-- EmployeeController
insert into permission (name) values ('GET_EMPLOYEE_ALL'); -- 9
insert into permission (name) values ('GET_EMPLOYEE_ONE'); -- 10
insert into permission (name) values ('GET_EMPLOYEE_BY_ENGINEER'); -- 11
insert into permission (name) values ('CREATE_EMPLOYEE'); -- 12
insert into permission (name) values ('UPDATE_EMPLOYEE'); -- 13
insert into permission (name) values ('UPDATE_EMPLOYEE_DESC'); -- 14
insert into permission (name) values ('DELETE_EMPLOYEE'); -- 15
insert into permission (name) values ('GET_EMPLOYEES_BY_PROJECT'); -- 16
insert into permission (name) values ('GET_PROJECTS_BY_EMPLOYEE'); -- 17

-- EngineerController
insert into permission (name) values ('GET_ENGINEER_ALL'); -- 18
insert into permission (name) values ('GET_ENGINEER_ONE'); -- 19
insert into permission (name) values ('GET_ENGINEER_BY_USERNAME'); -- 20
insert into permission (name) values ('CREATE_ENGINEER'); -- 21
insert into permission (name) values ('UPDATE_ENGINEER'); -- 22
insert into permission (name) values ('UPDATE_ENGINEER_SKILLS'); -- 23
insert into permission (name) values ('UPDATE_ENGINEER_PASSWORD'); -- 24
insert into permission (name) values ('DELETE_ENGINEER'); -- 25
insert into permission (name) values ('UPLOAD_CV'); -- 26
insert into permission (name) values ('GET_CV'); -- 27
insert into permission (name) values ('DOWNLOAD_CV'); -- 28

-- ManagerController
insert into permission (name) values ('GET_MANAGER_ALL'); -- 29
insert into permission (name) values ('GET_MANAGER_ONE'); -- 30
insert into permission (name) values ('CREATE_MANAGER'); -- 31
insert into permission (name) values ('UPDATE_MANAGER'); -- 32
insert into permission (name) values ('UPDATE_MANAGER_PASSWORD'); -- 33
insert into permission (name) values ('DELETE_MANAGER'); -- 34

-- ProjectController
insert into permission (name) values ('GET_PROJECT_ALL'); -- 35
insert into permission (name) values ('GET_PROJECT_ONE'); -- 36
insert into permission (name) values ('CREATE_PROJECT'); -- 37
insert into permission (name) values ('GET_PROJECTS_BY_MANAGER'); -- 38
insert into permission (name) values ('ADD_EMPLOYEE_TO_PROJECT'); -- 39
insert into permission (name) values ('REMOVE_EMPLOYEE_FROM_PROJECT'); -- 40
insert into permission (name) values ('DELETE_PROJECT'); -- 41

-- RoleController
insert into permission (name) values ('GET_ROLE_ALL'); -- 42
insert into permission (name) values ('GET_PERMISSION_ALL'); -- 43
insert into permission (name) values ('ADD_PERMISSION_TO_ROLE'); -- 44
insert into permission (name) values ('REMOVE_PERMISSION_FROM_ROLE'); -- 45

-- SystemAdministratorController
insert into permission (name) values ('GET_ADMIN_ALL'); -- 46
insert into permission (name) values ('GET_ADMIN_ONE'); -- 47
insert into permission (name) values ('UPDATE_ADMIN'); -- 48
insert into permission (name) values ('DELETE_ADMIN'); -- 49
insert into permission (name) values ('UPDATE_ADMIN_PASSWORD'); -- 50

-- ROLE_HUMAN_RESOURCE_MANAGER (id: 1)
insert into role_permission (role_id, permission_id) values (1, 7);  -- REFRESH_ACCESS_TOKEN
insert into role_permission (role_id, permission_id) values (1, 18); -- GET_ENGINEER_ALL
insert into role_permission (role_id, permission_id) values (1, 27); -- GET_CV

-- ROLE_ENGINEER (id: 2)
insert into role_permission (role_id, permission_id) values (2, 7);  -- REFRESH_ACCESS_TOKEN
insert into role_permission (role_id, permission_id) values (2, 11); -- GET_EMPLOYEE_BY_ENGINEER
insert into role_permission (role_id, permission_id) values (2, 14); -- UPDATE_EMPLOYEE_DESC
insert into role_permission (role_id, permission_id) values (2, 17); -- GET_PROJECTS_BY_EMPLOYEE
insert into role_permission (role_id, permission_id) values (2, 20); -- GET_ENGINEER_BY_USERNAME
insert into role_permission (role_id, permission_id) values (2, 22); -- UPDATE_ENGINEER
insert into role_permission (role_id, permission_id) values (2, 23); -- UPDATE_ENGINEER_SKILLS
insert into role_permission (role_id, permission_id) values (2, 24); -- UPDATE_ENGINEER_PASSWORD
insert into role_permission (role_id, permission_id) values (2, 26); -- UPLOAD_CV
insert into role_permission (role_id, permission_id) values (2, 27); -- GET_CV
insert into role_permission (role_id, permission_id) values (2, 28); -- DOWNLOAD_CV

-- ROLE_PROJECT_MANAGER (id: 3)
insert into role_permission (role_id, permission_id) values (3, 7);  -- REFRESH_ACCESS_TOKEN
insert into role_permission (role_id, permission_id) values (3, 13); -- UPDATE_EMPLOYEE
insert into role_permission (role_id, permission_id) values (3, 14); -- UPDATE_EMPLOYEE_DESC
insert into role_permission (role_id, permission_id) values (3, 16); -- GET_EMPLOYEES_BY_PROJECT
insert into role_permission (role_id, permission_id) values (3, 27); -- GET_CV
insert into role_permission (role_id, permission_id) values (3, 30); -- GET_MANAGER_ONE
insert into role_permission (role_id, permission_id) values (3, 32); -- UPDATE_MANAGER
insert into role_permission (role_id, permission_id) values (3, 33); -- UPDATE_MANAGER_PASSWORD
insert into role_permission (role_id, permission_id) values (3, 36); -- GET_PROJECT_ONE
insert into role_permission (role_id, permission_id) values (3, 38); -- GET_PROJECTS_BY_MANAGER

-- ROLE_ADMINISTRATOR (id: 4)
insert into role_permission (role_id, permission_id) values (4, 1);  -- LOGIN
insert into role_permission (role_id, permission_id) values (4, 2);  -- PASSWORDLESS_LOGIN
insert into role_permission (role_id, permission_id) values (4, 3);  -- PASSWORDLESS_LOGIN_ACTIVATE
insert into role_permission (role_id, permission_id) values (4, 4);  -- SIGNUP
insert into role_permission (role_id, permission_id) values (4, 5);  -- RESPONSE
insert into role_permission (role_id, permission_id) values (4, 6);  -- ACTIVATE_USER
insert into role_permission (role_id, permission_id) values (4, 7);  -- REFRESH_ACCESS_TOKEN
insert into role_permission (role_id, permission_id) values (4, 8);  -- GET_REQUESTS
insert into role_permission (role_id, permission_id) values (4, 9);  -- GET_EMPLOYEE_ALL
insert into role_permission (role_id, permission_id) values (4, 10); -- GET_EMPLOYEE_ONE
insert into role_permission (role_id, permission_id) values (4, 11); -- GET_EMPLOYEE_BY_ENGINEER
insert into role_permission (role_id, permission_id) values (4, 12); -- CREATE_EMPLOYEE
insert into role_permission (role_id, permission_id) values (4, 13); -- UPDATE_EMPLOYEE
insert into role_permission (role_id, permission_id) values (4, 14); -- UPDATE_EMPLOYEE_DESC
insert into role_permission (role_id, permission_id) values (4, 15); -- DELETE_EMPLOYEE
insert into role_permission (role_id, permission_id) values (4, 16); -- GET_EMPLOYEES_BY_PROJECT
insert into role_permission (role_id, permission_id) values (4, 17); -- GET_PROJECTS_BY_EMPLOYEE
insert into role_permission (role_id, permission_id) values (4, 18); -- GET_ENGINEER_ALL
insert into role_permission (role_id, permission_id) values (4, 19); -- GET_ENGINEER_ONE
insert into role_permission (role_id, permission_id) values (4, 20); -- GET_ENGINEER_BY_USERNAME
insert into role_permission (role_id, permission_id) values (4, 21); -- CREATE_ENGINEER
insert into role_permission (role_id, permission_id) values (4, 22); -- UPDATE_ENGINEER
insert into role_permission (role_id, permission_id) values (4, 23); -- UPDATE_ENGINEER_SKILLS
insert into role_permission (role_id, permission_id) values (4, 24); -- UPDATE_ENGINEER_PASSWORD
insert into role_permission (role_id, permission_id) values (4, 25); -- DELETE_ENGINEER
insert into role_permission (role_id, permission_id) values (4, 26); -- UPLOAD_CV
insert into role_permission (role_id, permission_id) values (4, 27); -- GET_CV
insert into role_permission (role_id, permission_id) values (4, 28); -- DOWNLOAD_CV
insert into role_permission (role_id, permission_id) values (4, 29); -- GET_MANAGER_ALL
insert into role_permission (role_id, permission_id) values (4, 30); -- GET_MANAGER_ONE
insert into role_permission (role_id, permission_id) values (4, 31); -- CREATE_MANAGER
insert into role_permission (role_id, permission_id) values (4, 32); -- UPDATE_MANAGER
insert into role_permission (role_id, permission_id) values (4, 33); -- UPDATE_MANAGER_PASSWORD
insert into role_permission (role_id, permission_id) values (4, 34); -- DELETE_MANAGER
insert into role_permission (role_id, permission_id) values (4, 35); -- GET_PROJECT_ALL
insert into role_permission (role_id, permission_id) values (4, 36); -- GET_PROJECT_ONE
insert into role_permission (role_id, permission_id) values (4, 37); -- CREATE_PROJECT
insert into role_permission (role_id, permission_id) values (4, 38); -- GET_PROJECTS_BY_MANAGER
insert into role_permission (role_id, permission_id) values (4, 39); -- ADD_EMPLOYEE_TO_PROJECT
insert into role_permission (role_id, permission_id) values (4, 40); -- REMOVE_EMPLOYEE_FROM_PROJECT
insert into role_permission (role_id, permission_id) values (4, 41); -- DELETE_PROJECT
insert into role_permission (role_id, permission_id) values (4, 42); -- GET_ROLE_ALL
insert into role_permission (role_id, permission_id) values (4, 43); -- GET_PERMISSION_ALL
insert into role_permission (role_id, permission_id) values (4, 44); -- ADD_PERMISSION_TO_ROLE
insert into role_permission (role_id, permission_id) values (4, 45); -- REMOVE_PERMISSION_FROM_ROLE
insert into role_permission (role_id, permission_id) values (4, 46); -- GET_ADMIN_ALL
insert into role_permission (role_id, permission_id) values (4, 47); -- GET_ADMIN_ONE
insert into role_permission (role_id, permission_id) values (4, 48); -- UPDATE_ADMIN
insert into role_permission (role_id, permission_id) values (4, 49); -- DELETE_ADMIN
insert into role_permission (role_id, permission_id) values (4, 50); -- UPDATE_ADMIN_PASSWORD

-- ROLE_GUEST (id: 5)
insert into role_permission (role_id, permission_id) values (5, 7);  -- REFRESH_ACCESS_TOKEN


insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 1', '11');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 2', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 3', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 4', '22');
insert into addresses (city, country, street, street_num) values ('Novi Sad', 'Srbija', 'Ulica 5', '22');

insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Danilo', 'Managerovic', 0, 'humanResourceManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReply@gmail.com', '065/123-456', 1, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Stefan', 'Inzenjer', 0, 'engineer', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls@gmail.com', '065/123-456', 2, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Petar', 'Managerovic', 0, 'projectManager', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'pls1@gmail.com', '065/123-456', 2, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Admin', 'Adminovic', 0, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789123', true, 'bsepNoReplasdy@gmail.com', '065/123-456', 1, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('guest', 'guest', 0, 'guest', '', '', true, '', '', 1, true, false);


-- DODAVANJE INZENJERA
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Inzenjer1', 'Inzenjer', 0, 'engineer1', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789111', true, 'engineer1@gmail.com', '065/123-111', 3, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Inzenjer2', 'Inzenjer', 0, 'engineer2', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789222', true, 'engineer2@gmail.com', '065/123-222', 4, true, false);
insert into users (name, surname, gender, username, password, jmbg, enabled, mail, phone_number, address_id, request_approved, mfa) values ('Inzenjer3', 'Inzenjer', 0, 'engineer3', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '123456789333', true, 'engineer3@gmail.com', '065/123-333', 5, true, false);

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