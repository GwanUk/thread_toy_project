insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION)
values (1, 'ROLE_USER', '유저 권한');
insert into USER_ (USER_ID, USERNAME, NICKNAME, PASSWORD, ROLE_ID)
values (1, 'user', 'kim', '1234', 1);