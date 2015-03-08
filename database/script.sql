create database DPF;

use DPF;

create table t_user_type (
	typeId int not null auto_increment,
	createTime timestamp,
	typeName varchar(255),
	primary key (typeId)
)ENGINE=InnoDB default CHARSET=utf8;

insert into t_user_type (typeId, createTime, typeName) values (1, '2014-11-13 16:30:10', '超级管理员');
insert into t_user_type (typeId, createTime, typeName) values (2, '2014-11-25 11:05:34', '系统安全管理员');
insert into t_user_type (typeId, createTime, typeName) values (3, '2014-11-13 16:30:13', '普通用户');


create table t_user (
	guid varchar(36) not null,	
	createTime timestamp,
	email varchar(255),
	lastLogin timestamp,
	loginIP varchar(15),
	online bit not null,
	mobilePhone varchar(11),
	password varchar(255) not null,
	username varchar(255) not null,
	userType int,
	primary key (guid),
	constraint FK_8v7kwe8lm002vb83kv44e3pja foreign key (userType) references t_user_type(typeId),
	index FK_8v7kwe8lm002vb83kv44e3pja (userType)
)ENGINE=InnoDB default CHARSET=utf8;

insert into t_user (guid, createTime, email, lastLogin, loginIP, mobilePhone, password, username, userType,online) values ('5f80d72a-6b11-11e4-82c7-976430c23ef0', '2014-11-13 16:45:13', 'user_rcy@163.com', null, null, '13585740802', '0A113EF6B61820DAA5611C870ED8D5EE', 'rency', 1,false);//888

create table t_authority (
    id int not null auto_increment,
    createTime timestamp,
	description varchar(255) default '',
	resources varchar(255) not null default '',
	userType int,
	primary key (id),
	constraint FK_1pagt2jes5xm6prpfe8s009f1 foreign key (userType) references t_user_type(typeId),
	index FK_1pagt2jes5xm6prpfe8s009f1 (userType)
)ENGINE=InnoDB default CHARSET=utf8;

insert into t_authority (id, createTime, description, resources, userType) values (1, '2014-11-18 16:40:12', '主页面', 'index.do', 1);
insert into t_authority (id, createTime, description, resources, userType) values (2, '2014-11-18 16:40:12', '主页面', 'index.do', 2);
insert into t_authority (id, createTime, description, resources, userType) values (3, '2014-11-18 16:40:12', '主页面', 'index.do', 3);
insert into t_authority (id, createTime, description, resources, userType) values (4, '2014-11-18 16:40:12', '加载菜单', 'json/menus!loadMenu.do', 1);
insert into t_authority (id, createTime, description, resources, userType) values (5, '2014-11-18 16:40:12', '加载菜单', 'json/menus!loadMenu.do', 2);
insert into t_authority (id, description, resources, userType) values (6,'加载用户', 'json/user!load.do', 1);
insert into t_authority (id, description, resources, userType) values (7,'新增用户', 'json/user!save.do', 1);
insert into t_authority (id, description, resources, userType) values (8,'删除用户', 'json/user!remove.do', 1);
insert into t_authority (id, description, resources, userType) values (9,'修改密码', 'json/user!modifyPassword.do',1);
insert into t_authority (id, description, resources, userType) values (10,'修改密码', 'json/user!modifyPassword.do',2);
insert into t_authority (id, description, resources, userType) values (11,'修改密码', 'json/user!modifyPassword.do',3);
insert into t_authority (id, description, resources, userType) values (12,'加载用户类别', 'json/userT!load.do', 1);
insert into t_authority (id, description, resources, userType) values (13,'新增用户类别', 'json/userT!save.do', 1);
insert into t_authority (id, description, resources, userType) values (14,'更新用户类别', 'json/userT!update.do', 1);
insert into t_authority (id, description, resources, userType) values (15,'删除用户类别', 'json/userT!remove.do', 1);
insert into t_authority (id, description, resources, userType) values (16,'加载菜单列表', 'json/menus!list.do', 2);
insert into t_authority (id, description, resources, userType) values (17,'查询菜单列表', 'json/menus!queryByResName.do', 2);
insert into t_authority (id, description, resources, userType) values (18,'加载树形结构', 'json/menus!queryResConstruct.do', 2);
insert into t_authority (id, description, resources, userType) values (19,'加载优先级', 'json/menus!queryPriority.do', 2);
insert into t_authority (id, description, resources, userType) values (20,'加载用户类别', 'json/userT!load.do', 2);
insert into t_authority (description, resources, userType) values ('增加菜单', 'json/menus!save.do', 2);
insert into t_authority (description, resources, userType) values ('更新菜单', 'json/menus!update.do', 2);
insert into t_authority (description, resources, userType) values ('删除菜单', 'json/menus!remove.do', 2);
insert into t_authority (description, resources, userType) values ('加载权限列表', 'json/authority!load.do', 2);
insert into t_authority (description, resources, userType) values ('权限查询', 'json/authority!queryByAddr.do', 2);
insert into t_authority (description, resources, userType) values ('增加权限', 'json/authority!add.do', 2);
insert into t_authority (description, resources, userType) values ('更新权限', 'json/authority!update.do', 2);
insert into t_authority (description, resources, userType) values ('删除权限', 'json/authority!remove.do', 2);


create table t_menus (
	resId varchar(23) not null,
	createTime timestamp,
	description varchar(255),
	href varchar(255),
	leaf bit not null,
	parentResId varchar(23) not null,
	parentResName varchar(255),
	priority int not null,
	resName varchar(255),
	userType int,
	primary key (resId),
	constraint FK_j7fbbwjcsebkmixbmngj2hrsv foreign key (userType) references t_user_type(typeId),
	index FK_j7fbbwjcsebkmixbmngj2hrsv (userType)
)ENGINE=InnoDB default CHARSET=utf8;

insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('57482014111917311901053', '2014-11-19 17:31:13', '爬虫管理', '', false, '66922014111412071267523', '数据平台', 1, '爬虫管理', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071267521', '2014-11-14 13:40:34', '数据平台', null, false, '-1', '根节点', 0, '数据平台', 1);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071267522', '2014-11-14 13:40:34', '数据平台', null, false, '-1', '根节点', 0, '数据平台', 2);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071267523', '2014-11-14 13:40:34', '数据平台', null, false, '-1', '根节点', 0, '数据平台', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071367521', '2014-11-14 12:13:45', '用户管理', null, false, '66922014111412071267521', '数据平台', 1, '用户管理', 1);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071367522', '2014-12-05 16:03:02', '用户管理', 'jsp_users_maintain.do', true, '66922014111412071367521', '用户管理', 1, '用户管理', 1);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071367523', '2014-12-05 16:03:02', '用户类别管理', 'jsp_usert_maintain.do', true, '66922014111412071367521', '用户管理', 1, '用户类别管理', 1);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071467521', '2014-11-25 11:01:40', '资源管理', null, false, '66922014111412071267522', '数据平台', 1, '资源管理', 2);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071467522', '2014-12-05 16:03:02', '菜单管理', 'jsp_menus_maintain.do', true, '66922014111412071467521', '资源管理', 1, '菜单管理', 2);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071567521', '2014-11-25 11:02:33', '权限管理', null, false, '66922014111412071267522', '数据平台', 2, '权限管理', 2);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('66922014111412071567522', '2014-12-05 16:03:02', '访问权限', 'jsp_authencation_maintain.do', true, '66922014111412071567521', '权限管理', 1, '访问权限', 2);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('75082014111917381920725', '2014-12-11 16:07:31', '爬虫监控', 'jsp_monitor_crawler.do', true, '57482014111917311901053', '爬虫管理', 3, '爬虫监控', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('94282014121116181105362', '2014-12-11 16:18:09', '索引监控', 'jsp_monitor_lucene.do', true, '95482014121115581100973', '索引管理', 6, '索引监控', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('95482014121115581100973', '2014-12-11 15:58:38', '索引管理', '', false, '66922014111412071267523', '数据平台', 2, '索引管理', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('95482014121115591136737', '2014-12-11 16:07:31', '数据监控', 'jsp_monitor_hadoop.do', true, '95482014121115591154948', '数据管理', 4, '数据监控', 3);
insert into t_menus (resId, createTime, description, href, leaf, parentResId, parentResName, priority, resName, userType) values ('95482014121115591154948', '2014-12-11 15:59:12', '数据管理', '', false, '66922014111412071267523', '数据平台', 5, '数据管理', 3);


create table t_crawler_config(
	crawlerId varchar(32) not null,
	crawlerDesc varchar(255),
	crawlerName varchar(255),
	remoteAddr varchar(20),
	remotePort varchar(10),
	createTime timestamp,
	initAddr varchar(255),
	poolSize int default 5,
	status bit not null,
	primary key (crawlerId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table t_message_queue(
	id int not null auto_increment,
	createTime timestamp,
	message varchar(255),
	receiver varchar(255),
	send bit not null,
	sender varchar(255),
	primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table t_loggers(
	id int not null auto_increment,
	action varchar(255),
	browser varchar(100),
	clientIP varchar(50),
	createTime timestamp,
	logType int not null,
	logTypeDesc varchar(255) not null,
	param longtext,
	usetime varchar(10),
	user varchar(36),
	primary key (id),
	constraint FK_pvo1cvepiy6bgq6i1dit4duc4 foreign key (user) references t_user (guid),
	index FK_pvo1cvepiy6bgq6i1dit4duc4 (user)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table t_crawler_cookie_param(
	host varchar(255) not null,
	cookieDomain varchar(255),
	cookiePath varchar(255),
	cookieVersion int not null,
	execDate timestamp,
	isSecure bit not null,
	name varchar(255),
	path varchar(255),
	cookieValue varchar(255),
	primary key (host)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
create table t_crawler_task_queue(
	url varchar(255) not null,
	crawlerId varchar(255),
    download bit not null,
    execDate timestamp,
    host varchar(255),
    lastModified varchar(255),
    statusCode int not null,
    timeout int not null,
    visited bit not null,
    requestMethod int not null,
    httpParams longtext,
    primary key (url)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
create table t_crawler_web_page(
    url varchar(255) not null,
    contentMD5 varchar(255) not null,
    charset varchar(255),
    html longtext,
    content longtext,
    createIndex bit not null,
    dataLength int not null,
    description varchar(255),
    execDate timestamp,
    keywords varchar(255),
    lastModified varchar(255),
    title varchar(255),
    primary key (url, contentMD5)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--------测试
select * from t_user_type;
select * from t_user;
select * from t_authority;
select * from t_menus;

delete from t_user_type;
delete from t_user;
delete from t_authority;
delete from t_menus;

