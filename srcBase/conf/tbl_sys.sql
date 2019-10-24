/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/9/4 14:56:28                            */
/*==============================================================*/


DROP TABLE IF EXISTS tbl_sys_resource;

DROP TABLE IF EXISTS tbl_sys_role;

DROP TABLE IF EXISTS tbl_sys_role_resource;

DROP TABLE IF EXISTS tbl_sys_user;

DROP TABLE IF EXISTS tbl_sys_user_role;

drop table if exists tbl_sys_dict_def;

drop table if exists tbl_sys_dict_item;


/*==============================================================*/
/* Table: tbl_sys_resource                                      */
/*==============================================================*/
CREATE TABLE tbl_sys_resource
(
   resourceId           INT(12) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
   resourcePid          INT(12) COMMENT '父资源ID',
   resourceInd          VARCHAR(20) COMMENT '资源标识',
   resourceName         VARCHAR(50) COMMENT '资源名称',
   resourceIndex        INT(12) COMMENT '资源顺序号',
   resourceUrl          VARCHAR(100) COMMENT '资源地址',
   resourceType         VARCHAR(2) COMMENT '资源类型
            1-功能菜单
            2-操作菜单
            3-按钮（超链接）',
   PRIMARY KEY (resourceId)
);

/*==============================================================*/
/* Table: tbl_sys_role                                          */
/*==============================================================*/
CREATE TABLE tbl_sys_role
(
   roleId               INT(12) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
   roleCode             VARCHAR(32) NOT NULL COMMENT '角色标识',
   roleName             VARCHAR(50) COMMENT '角色名称',
   roleDesc             VARCHAR(100) COMMENT '角色描述',
   PRIMARY KEY (roleId)
);

/*==============================================================*/
/* Table: tbl_sys_role_resource                                 */
/*==============================================================*/
CREATE TABLE tbl_sys_role_resource
(
   roleId               INT(12) NOT NULL COMMENT '角色编号',
   resourceId           INT(12) NOT NULL COMMENT '资源编号',
   PRIMARY KEY (roleId, resourceId)
);

/*==============================================================*/
/* Table: tbl_sys_user                                          */
/*==============================================================*/
CREATE TABLE tbl_sys_user
(
   userId               INT(12) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
   userAct              VARCHAR(32) NOT NULL COMMENT '账号',
   userPwd              VARCHAR(100) COMMENT '密码',
   userName             VARCHAR(50) COMMENT '姓名',
   userPhone            VARCHAR(30) COMMENT '手机号',
   userMail             VARCHAR(30) COMMENT '邮箱',
   actTime              DATETIME COMMENT '帐号有效期',
   PRIMARY KEY (userId)
);

/*==============================================================*/
/* Table: tbl_sys_user_role                                     */
/*==============================================================*/
CREATE TABLE tbl_sys_user_role
(
   userId               INT(12) NOT NULL  COMMENT '用户ID',
   roleId               INT(12) NOT NULL COMMENT '角色ID',
   PRIMARY KEY (userId, roleId)
);

/*==============================================================*/
/* Table: tbl_sys_dict_def                                      */
/*==============================================================*/
create table tbl_sys_dict_def
(
   dictId               int(12) not null auto_increment comment '数据字典编号',
   dictCode             varchar(50) not null comment '数据字典编码',
   dictName             varchar(50) not null comment '数据字典名称',
   dictDesc             varchar(100) comment '数据字典描述',
   primary key (dictId)
);

/*==============================================================*/
/* Table: tbl_sys_dict_item                                     */
/*==============================================================*/
create table tbl_sys_dict_item
(
   itemId               int(12) not null auto_increment comment '字典项编号',
   dictCode             varchar(50) not null comment '字典定义编号',
   itemCode             varchar(50) not null comment '字典项编码',
   itemName             varchar(50) not null comment '字典项名称',
   itemOrder            int(4) comment '字典项排序',
   itemDesc             varchar(100) comment '字典项描述',
   primary key (itemId)
);

INSERT INTO tbl_sys_user (userId, userAct, userPwd, userName, userPhone, userMail, actTime) VALUES('1','admin','3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d','系统管理员','13507769876','13507769876@139.com','2099-01-30 00:00:00');
INSERT INTO tbl_sys_user (userId, userAct, userPwd, userName, userPhone, userMail, actTime) VALUES('2','zg','3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d','业务主管','13507725412','13507725412@139.com','2014-09-15 00:00:00');
INSERT INTO tbl_sys_user (userId, userAct, userPwd, userName, userPhone, userMail, actTime) VALUES('3','cza','3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d','业务操作员A','13507766541','13507766541@139.com','2015-01-30 00:00:00');
INSERT INTO tbl_sys_user (userId, userAct, userPwd, userName, userPhone, userMail, actTime) VALUES('4','czb','3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d','业务操作员B','13507765487','13507765487@139.com','2015-01-30 00:00:00');

INSERT INTO tbl_sys_role (roleId, roleCode, roleName, roleDesc) VALUES('1','xtyw','系统运维岗','负责系统、数据库的日常运维，保障24×7稳定运行；负责系统、数据库的性能优化，并推动解决问题。');
INSERT INTO tbl_sys_role (roleId, roleCode, roleName, roleDesc) VALUES('2','zggl','主管管理岗','负责公司的直接工作，分管人员安排，授权相关业务人员完成相关工作。');
INSERT INTO tbl_sys_role (roleId, roleCode, roleName, roleDesc) VALUES('3','ywcza','业务操作岗A','完成XX的相关业务。');
INSERT INTO tbl_sys_role (roleId, roleCode, roleName, roleDesc) VALUES('4','ywczb','业务操作岗B','完成XX的相关业务。');

INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('1','0',NULL,'系统管理','1',NULL,'1');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('2','1',NULL,'用户角色权限','1',NULL,'1');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('3','1',NULL,'系统数据设置','2',NULL,'1');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('4','2','','用户管理','1','/mgr/user','2');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('5','2',NULL,'角色管理','2','/mgr/role','2');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('6','2','','资源管理','3','/mgr/resource','2');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('7','3',NULL,'数据字典管理','1','/mgr/dictDef','2');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('9','4','btnAdd','新增','1','/mgr/user/add.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('10','4','btnEdit','编辑','2','/mgr/user/update.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('11','4','btnRemove','删除','3','/mgr/user/delete.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('12','4','btnGrant','授权','4','/mgr/user/grant.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('13','5','btnAdd','新增','1','/mgr/role/add.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('14','5','btnEdit','编辑','2','/mgr/role/update.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('15','5','btnRemove','删除','3','/mgr/role/delete.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('16','5','btnResource','资源配置','4','/mgr/role/updateResource.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('17','6','btnAdd','新增','1','/mgr/resource/add.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('18','6','btnEdit','编辑','2','/mgr/resource/update.json','3');
insert into tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) values('19','6','btnRemove','删除','3','/mgr/resource/delete.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('20','7','btnAdd','新增','1','/mgr/dictDef/add.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('21','7','btnEdit','编辑','2','/mgr/dictDef/update.json','3');
insert into tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) values('22','7','btnRemove','删除','3','/mgr/dictDef/delete.json','3');
insert into tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) values('23','7','btnItem','维护字典项','4','/mgr/dictItem/list','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('24','6','btnTop','置顶','5','/mgr/resource/top.json','3');
INSERT INTO tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) VALUES('25','6','btnUp','向上','6','/mgr/resource/up.json','3');
insert into tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) values('26','6','btnDown','向下','7','/mgr/resource/down.json','3');
insert into tbl_sys_resource (resourceId, resourcePid, resourceInd, resourceName, resourceIndex, resourceUrl, resourceType) values('27','6','btnLow','置底','8','/mgr/resource/low.json','3');

insert into `tbl_sys_user_role` (`userId`, `roleId`) values('1','1');

insert into tbl_sys_role_resource (roleId, resourceId) values('1','4');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','5');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','6');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','7');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','9');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','10');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','11');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','12');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','13');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','14');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','15');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','16');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','17');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','18');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','19');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','20');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','21');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','22');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','23');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','24');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','25');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','26');
insert into tbl_sys_role_resource (roleId, resourceId) values('1','27');
commit;