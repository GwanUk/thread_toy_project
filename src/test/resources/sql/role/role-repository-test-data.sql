insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (1, 'ROLE_ADMIN', '관리자 권한', null);
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (2, 'ROLE_MANAGER', '매니저 권한', 1);
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (3, 'ROLE_VIP', '특급 권한', 2);
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (4, 'ROLE_USER', '유저 권한', 3);
