insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (13, 'ROLE_ADMIN', '관리자 권한', null);
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (12, 'ROLE_VIP', '특별 권한', 13);
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION, PARENT_ID)
values (11, 'ROLE_USER', '유저 권한', 12);
