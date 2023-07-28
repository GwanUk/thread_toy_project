DROP TABLE IF EXISTS URI_ROLE CASCADE;
DROP TABLE IF EXISTS URI CASCADE;
DROP TABLE IF EXISTS ROLE_ CASCADE;

CREATE TABLE URI
(
    URI_ID             BIGINT AUTO_INCREMENT,
    URI_NAME           VARCHAR(255) NOT NULL,
    URI_ORDER          INTEGER      NOT NULL,
    CREATED_DATE       TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    CONSTRAINT URI_PK PRIMARY KEY (URI_ID),
    CONSTRAINT URI_UK_01 UNIQUE (URI_NAME)
);

CREATE TABLE ROLE_
(
    ROLE_ID            BIGINT AUTO_INCREMENT,
    ROLE_NAME          VARCHAR(255) NOT NULL,
    DESCRIPTION        VARCHAR(255),
    PARENT_ID          BIGINT,
    CREATED_DATE       TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    CONSTRAINT ROLE_PK PRIMARY KEY (ROLE_ID),
    CONSTRAINT ROLE_UK_01 UNIQUE (ROLE_NAME),
    CONSTRAINT ROLE_FK_01 FOREIGN KEY (PARENT_ID) REFERENCES ROLE_ (ROLE_ID) ON DELETE SET NULL ON UPDATE RESTRICT
);

CREATE TABLE URI_ROLE
(
    URI_ROLE_ID        BIGINT AUTO_INCREMENT,
    URI_ID             BIGINT       NOT NULL,
    ROLE_ID            BIGINT       NOT NULL,
    CREATED_DATE       TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP,
    CREATED_BY         VARCHAR(255),
    LAST_MODIFIED_BY   VARCHAR(255),
    CONSTRAINT URI_ROLE_PK PRIMARY KEY (URI_ROLE_ID),
    CONSTRAINT URI_ROLE_UK_01 UNIQUE (URI_ID, ROLE_ID),
    CONSTRAINT URI_ROLE_FK_01 FOREIGN KEY (URI_ID) REFERENCES URI (URI_ID) ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT URI_ROLE_FK_02 FOREIGN KEY (ROLE_ID) REFERENCES ROLE_ (ROLE_ID) ON DELETE CASCADE ON UPDATE RESTRICT
);

insert into URI (URI_NAME, URI_ORDER, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('/api/users/**', 1, now(), now(), 'system', 'system');
insert into URI (URI_NAME, URI_ORDER, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('/api/role/**', 2, now(), now(), 'system', 'system');
insert into URI (URI_NAME, URI_ORDER, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('/api/uri/**', 3, now(), now(), 'system', 'system');
insert into URI (URI_NAME, URI_ORDER, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('/api/ipaddress/**', 4, now(), now(), 'system', 'system');
insert into URI (URI_NAME, URI_ORDER, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('/api/**', 5, now(), now(), 'system', 'system');


insert into ROLE_ (ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('ROLE_ADMIN', '관리자 권한', null, now(), now(), 'system', 'system');
insert into ROLE_ (ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('ROLE_MANAGER', '매니저 권한', 1, now(), now(), 'system', 'system');
insert into ROLE_ (ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('ROLE_VIP', '특급 권한', 2, now(), now(), 'system', 'system');
insert into ROLE_ (ROLE_NAME, DESCRIPTION, PARENT_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values ('ROLE_USER', '유저 권한', 3, now(), now(), 'system', 'system');

insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (1, 1, now(), now(), 'system', 'system');
insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (2, 2, now(), now(), 'system', 'system');
insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (3, 2, now(), now(), 'system', 'system');
insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (4, 2, now(), now(), 'system', 'system');
insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (5, 4, now(), now(), 'system', 'system');
insert into URI_ROLE (URI_ID, ROLE_ID, CREATED_DATE, LAST_MODIFIED_DATE, CREATED_BY, LAST_MODIFIED_BY)
values (5, 3, now(), now(), 'system', 'system');

