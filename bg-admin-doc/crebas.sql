/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/4/26 17:08:15                           */
/*==============================================================*/


drop table if exists t_behavior_log;

drop table if exists t_dict;

drop table if exists t_org;

drop table if exists t_role;

drop table if exists t_role_tree;

drop table if exists t_tree;

drop table if exists t_user;

drop table if exists t_user_org;

drop table if exists t_user_role;

/*==============================================================*/
/* Table: t_behavior_log                                        */
/*==============================================================*/
create table t_behavior_log
(
   behaviorLogId        bigint not null auto_increment comment '行为日志流水ID',
   actionMethod         varchar(200) comment '响应方法',
   actionDate           datetime comment '请求时间',
   actionUser           varchar(100) comment '请求人',
   actionUserId         varchar(32) comment '请求人ID',
   primary key (behaviorLogId)
);

/*==============================================================*/
/* Table: t_dict                                                */
/*==============================================================*/
create table t_dict
(
   id                   varchar(32) not null comment '字典流水ID',
   dictType             varchar(100) comment '字典类型',
   dictCode             varchar(100) comment '字典编码',
   dictText             varchar(100) comment '字典文本',
   dictValue            varchar(100) comment '字典值',
   primary key (id)
);

/*==============================================================*/
/* Table: t_org                                                 */
/*==============================================================*/
create table t_org
(
   orgId                int not null auto_increment comment '流水ID',
   orgName              varchar(100) comment '组织名字',
   orgCode              varchar(32) comment '组织编码',
   parentOrgId          int comment '父流水ID',
   parentOrgName        varchar(100) comment '父组织架构名字',
   crtDate              datetime comment '创建时间',
   fullPath             varchar(255) comment '菜单流水完整路径(1.2.3)',
   primary key (orgId)
);

/*==============================================================*/
/* Table: t_role                                                */
/*==============================================================*/
create table t_role
(
   roleId               varchar(32) not null comment '角色流水ID',
   roleName             varchar(100) comment '角色名字',
   roleCode             varchar(100) comment '角色编码',
   crtDate              datetime comment '创建时间',
   primary key (roleId)
);

/*==============================================================*/
/* Table: t_role_tree                                           */
/*==============================================================*/
create table t_role_tree
(
   roleTreeId           varchar(32) not null comment '角色菜单关联流水ID',
   roleId               varchar(32) comment '角色ID',
   treeId               int comment '菜单ID',
   primary key (roleTreeId)
);

/*==============================================================*/
/* Table: t_tree                                                */
/*==============================================================*/
create table t_tree
(
   treeId               int not null auto_increment comment '菜单流水ID',
   treeName             varchar(100) comment '菜单名字',
   treeCode             varchar(100) comment '菜单编码',
   treeState            varchar(10) comment '菜单状态【1：可用；2：禁用】',
   treeType             varchar(10) comment '菜单类型【1：菜单节点；2：按钮节点】',
   parentTreeId         int comment '父节点编号',
   parentTreeName       varchar(100) comment '父节点名称',
   crtDate              datetime comment '创建时间',
   fullPath             varchar(255) comment '菜单流水完整路径(1.2.3)',
   primary key (treeId)
);

/*==============================================================*/
/* Table: t_user                                                */
/*==============================================================*/
create table t_user
(
   userId               varchar(32) not null comment '用户流水ID',
   loginAccount         varchar(100) comment '用户登录账号',
   loginPassword        varchar(32) comment '用户登录密码',
   crtDate              datetime comment '创建时间',
   nickName             varchar(100) comment '真实姓名',
   lastLoginDate        datetime comment '最后登录时间',
   token                varchar(32) comment '登录的token',
   headImg              varchar(100) comment '用户头像图片地址',
   primary key (userId)
);

/*==============================================================*/
/* Table: t_user_org                                            */
/*==============================================================*/
create table t_user_org
(
   userOrgId            varchar(32) not null comment '用户组织关联流水ID',
   userId               varchar(32) comment '用户流水ID',
   orgId                int comment '组织流水ID',
   primary key (userOrgId)
);

/*==============================================================*/
/* Table: t_user_role                                           */
/*==============================================================*/
create table t_user_role
(
   userRoleId           varchar(32) not null comment '用户角色关联流水ID',
   userId               varchar(32) comment '用户ID',
   roleId               varchar(32) comment '角色ID',
   primary key (userRoleId)
);

