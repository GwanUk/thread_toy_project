insert into URI (URI_ID, URI_NAME, URI_ORDER)
values (1, '/api/thread', 1);
insert into URI (URI_ID, URI_NAME, URI_ORDER)
values (2, '/api/admin', 2);

insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION)
values (1, 'ROLE_USER', '유저 권한');
insert into ROLE (ROLE_ID, ROLE_NAME, DESCRIPTION)
values (2, 'ROLE_ADMIN', '관리자 권한');

insert into URI_ROLE (URI_ROLE_ID, URI_ID, ROLE_ID)
values (1, 1, 1);
insert into URI_ROLE (URI_ROLE_ID, URI_ID, ROLE_ID)
values (2, 2, 2);

