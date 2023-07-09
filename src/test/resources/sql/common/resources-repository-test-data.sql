
insert into RESOURCES (resources_id, uri)
values (1, '/api/thread');
insert into RESOURCES (resources_id, uri)
values (2, '/api/admin');

insert into ROLES (roles_id, role_name, description)
values (1, 'ROLE_USER', '유저 권한');
insert into ROLES (roles_id, role_name, description)
values (2, 'ROLE_ADMIN', '관리자 권한');

insert into RESOURCES_ROLES (resources_roles_id, resources_id, roles_id, order_num)
values (1, 1, 1, 1);
insert into RESOURCES_ROLES (resources_roles_id, resources_id, roles_id, order_num)
values (2, 2, 2, 1);

