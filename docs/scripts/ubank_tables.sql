-- ----------------------------
--  
-- ----------------------------
DROP DATABASE IF EXISTS ubank;
CREATE DATABASE ubank;
use ubank;
-- ----------------------------
--  Table structure for tbl_users
-- ----------------------------
DROP TABLE IF EXISTS tbl_users;
create table tbl_users(
   user_id          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '用户编号',
   user_name        varchar(50) DEFAULT NULL COMMENT '用户名称',
   login_password   varchar(64) DEFAULT NULL COMMENT '登录密码',
   pay_password     varchar(64) DEFAULT NULL COMMENT '支付密码',
   login_ticket     varchar(64) DEFAULT NULL COMMENT '登录凭据',
   access_token 	varchar(64) DEFAULT NULL COMMENT '认证token',
   mobile           varchar(20) DEFAULT NULL COMMENT '手机号码',
   reserve_mobile   varchar(20) DEFAULT NULL COMMENT '银行预留手机号码',
   cardType         varchar(10) DEFAULT NULL COMMENT '证件类型 1:身份证;2:护照;3:港澳通行证;21:工作证;99:其它',
   cardNo           varchar(50) DEFAULT NULL COMMENT '证件号',
   bankNo           varchar(50) DEFAULT NULL COMMENT '银行编号',
   bankCardNo       varchar(50) DEFAULT NULL COMMENT '银行卡号',
   secondBankCardNo varchar(50) DEFAULT NULL COMMENT '二类户账号',
   email            varchar(50) DEFAULT NULL COMMENT '邮箱(唯一)',
   status           int(10) DEFAULT 0 COMMENT '状态 0:启用;1:停用;2:已注销',
   role_id          int(10) DEFAULT 2 COMMENT '1:系统管理角色;2:企业角色：财务、HR、行政;3:普通角色',
   expiredTime      varchar(20) DEFAULT NULL COMMENT '账号有效期至',
   loginTime        varchar(20) DEFAULT NULL COMMENT '登陆时间',
   createTime       varchar(20) DEFAULT NULL COMMENT '添加时间',
   remark           varchar(600) DEFAULT NULL COMMENT '备注',
   login_fail_num   int(10) DEFAULT 0 COMMENT '连续登陆失败次数,登陆成功后请归零',
   login_fail_time  varchar(20) DEFAULT NULL COMMENT '最近登陆验证时间',
   lock_time        varchar(20) DEFAULT NULL COMMENT '最近登陆验证时间',
   PRIMARY KEY(user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息';

insert into tbl_users(user_name, login_password, role_id, createTime) values('admin', '35f3de13ad5aebeb76180c90b6515314', 1, now());
insert into tbl_users(user_name, login_password, role_id, createTime) values('ubank', '686d5d3445d288a722e2467ef570c088', 1, now());

-- ----------------------------
--  Table structure for tbl_banks
-- ----------------------------
DROP TABLE IF EXISTS tbl_banks;
create table tbl_banks(
	bankNo 	 varchar(50) NOT NULL COMMENT '银行编号',
	bankName varchar(50) DEFAULT NULL COMMENT '银行名称',
	PRIMARY KEY(bankNo)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行信息';

insert into tbl_banks(bankNo, bankName) values('102100099996', '中国工商银行');
insert into tbl_banks(bankNo, bankName) values('103100000026', '中国农业银行');
insert into tbl_banks(bankNo, bankName) values('403100000004', '中国邮储银行');
insert into tbl_banks(bankNo, bankName) values('105100000017', '中国建设银行');
insert into tbl_banks(bankNo, bankName) values('303100000006', '光大银行');
insert into tbl_banks(bankNo, bankName) values('309391000011', '兴业银行');
insert into tbl_banks(bankNo, bankName) values('302100011000', '中信银行');
insert into tbl_banks(bankNo, bankName) values('104100000004', '中国银行');
insert into tbl_banks(bankNo, bankName) values('301290000007', '交通银行');
insert into tbl_banks(bankNo, bankName) values('305100000013', '民生银行');
insert into tbl_banks(bankNo, bankName) values('307584007998', '平安银行');
insert into tbl_banks(bankNo, bankName) values('325290000012', '上海银行');
insert into tbl_banks(bankNo, bankName) values('313100000013', '北京银行');

-- ----------------------------
--  Table structure for tbl_roles
-- ----------------------------
DROP TABLE IF EXISTS tbl_roles;
create table tbl_roles(
	role_id   int(10)     NOT NULL COMMENT '角色ID',
	role_name varchar(50) DEFAULT NULL COMMENT '角色名称',
	PRIMARY KEY(role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

insert into tbl_roles(role_id, role_name) values(1, '系统管理角色');
insert into tbl_roles(role_id, role_name) values(2, '企业角色：财务、HR、行政');
insert into tbl_roles(role_id, role_name) values(3, '普通用户');

-- ----------------------------
--  Table structure for tbl_salaries
-- ----------------------------
DROP TABLE IF EXISTS tbl_salaries;
create table tbl_salaries(
	sid       bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '工资流水号',
	userName  varchar(50)  DEFAULT NULL COMMENT '操作人',
	totalNumber int(10)    DEFAULT 0 COMMENT '总笔数', 
	totalAmount double  DEFAULT 0.0 COMMENT '总金额', 
	salaryDate varchar(50) DEFAULT NULL COMMENT '发放时间',
	salary_createTime varchar(20) DEFAULT NULL COMMENT '工资流水生成时间',
	company varchar(128) DEFAULT NULL COMMENT '企业名称',
	status varchar(10) DEFAULT '0' COMMENT '工资流水状态',
	entrustSeqNo varchar(20) DEFAULT NULL COMMENT '业务委托编号, 银企直联代发工资接口返回的编号',
	remark varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(sid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工资流水';

-- ----------------------------
--  Table structure for tbl_salaries_detail
-- ----------------------------
DROP TABLE IF EXISTS tbl_salaries_detail;
create table tbl_salaries_detail(
	did       bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '流水号',
	sid       bigint(20)  NOT NULL COMMENT '工资流水号',
	userName  varchar(50) DEFAULT NULL COMMENT '收款人名',
	cardNo    varchar(50) DEFAULT 0 COMMENT '身份证号', 
	salaries  double   DEFAULT 0.0 COMMENT '工资金额', 
	salaryDate varchar(20) DEFAULT NULL COMMENT '工资发放时间',
	remark varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(did)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工资流水详细';

-- ----------------------------
--  Table structure for tbl_categories
-- ----------------------------
DROP TABLE IF EXISTS tbl_categories;
create table tbl_categories(
	cid       int(10)      NOT NULL AUTO_INCREMENT COMMENT '类别编号',
	category_name varchar(50)  NOT NULL COMMENT '类别名称',
	createTime varchar(20) DEFAULT NULL COMMENT '创建时间', 
	updateTime varchar(20) DEFAULT NULL COMMENT '最近修改时间',
	remark    varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(cid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类别信息';

insert into tbl_categories(category_name,createTime,remark) values('金融',now(),'金融');
insert into tbl_categories(category_name,createTime,remark) values('保险',now(),'保险');
insert into tbl_categories(category_name,createTime,remark) values('电影票',now(),'电影票');
insert into tbl_categories(category_name,createTime,remark) values('飞机票/火车票',now(),'飞机票/火车票');

-- ----------------------------
--  Table structure for tbl_modules
-- ----------------------------
DROP TABLE IF EXISTS tbl_modules;
create table tbl_modules(
	mid         int(10)      NOT NULL AUTO_INCREMENT COMMENT '模块编号',
	module_name varchar(50)  NOT NULL COMMENT '模块名称',
	createTime varchar(20) DEFAULT NULL COMMENT '创建时间', 
	updateTime varchar(20) DEFAULT NULL COMMENT '最近修改时间',
	remark      varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(mid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块信息';

insert into tbl_modules(mid,module_name,createTime) values(1,'首页链接块',now());
insert into tbl_modules(mid,module_name,createTime) values(2,'首页顶部banner模块',now());
insert into tbl_modules(mid,module_name,createTime) values(3,'生活顶部banner模块',now());
insert into tbl_modules(mid,module_name,createTime) values(4,'生活4按钮链接模块',now());
insert into tbl_modules(mid,module_name,createTime) values(5,'生活8按钮链接模块',now());
insert into tbl_modules(mid,module_name,createTime) values(6,'生活底部banner模块',now());
insert into tbl_modules(mid,module_name,createTime) values(7,'首页底部banner模块',now());

-- ----------------------------
--  Table structure for tbl_third_party_urls
-- ----------------------------
DROP TABLE IF EXISTS tbl_third_party_urls;
create table tbl_third_party_urls(
	uid      bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'URL编号',
	url_name varchar(50)  NOT NULL COMMENT 'URL名称',
	url      varchar(300) DEFAULT NULL COMMENT 'URL链接',
	pic_path varchar(128) DEFAULT NULL COMMENT '图片地址', 
	url_cid  int(10)   DEFAULT NULL COMMENT 'URL属于哪个类别', 
	url_mid  int(10)   DEFAULT NULL COMMENT 'URL属于哪个模块',
	orderby  int(10)   DEFAULT NULL COMMENT '排序',
	createTime varchar(20) DEFAULT NULL COMMENT '创建时间',
	updateTime varchar(20) DEFAULT NULL COMMENT '最近修改时间',
	remark   varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(uid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方url信息';

-- ----------------------------
--  Table structure for tbl_banners
-- ----------------------------
DROP TABLE IF EXISTS tbl_banners;
create table tbl_banners(
	bid      bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'banner编号',
	banner_name varchar(50)  NOT NULL COMMENT 'banner名称',
	url      varchar(300) DEFAULT NULL COMMENT 'banner链接',
	pic_path varchar(128) DEFAULT NULL COMMENT '图片地址', 
	banner_mid  int(10)   DEFAULT NULL COMMENT 'banner属于哪个模块',
	orderby  int(10)   DEFAULT NULL COMMENT '排序',
	createTime varchar(20) DEFAULT NULL COMMENT '创建时间',
	updateTime varchar(20) DEFAULT NULL COMMENT '最近修改时间',
	remark   varchar(600) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY(bid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='banner信息';

-- ----------------------------
--  Table structure for `tbl_bankAccount`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_bankAccount`;
CREATE TABLE `tbl_bankAccount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bankNumber` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '银行编号',
  `account` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司银行账户',
  `customer` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司账户客户号',
  `certificateNumber` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '证书序号',
  `authorizationCode` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '银行数字证书授权码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='U发展银行-银行账户信息';

-- ----------------------------
--  Table structure for `tbl_company`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_company`;
CREATE TABLE `tbl_company` (
  `companyNumber` int(11) NOT NULL AUTO_INCREMENT COMMENT '公司编号',
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司名称',
  `account` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '银行账户',
  `legalPerson` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司法人',
  `group_num` int(255) DEFAULT NULL COMMENT '集团编号',
  `details` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '详情',
  PRIMARY KEY (`companyNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=20001 DEFAULT CHARSET=utf8 COMMENT='U发展银行-公司表';


-- ----------------------------
--  Table structure for `tbl_departments`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_departments`;
CREATE TABLE `tbl_departments` (
  `dept_number` int(20) NOT NULL AUTO_INCREMENT COMMENT '部门编号',
  `name` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '部门名称',
  `company_num` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '公司编号',
  `count` int(11) DEFAULT NULL COMMENT '部门人数',
  `remark` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dept_number`)
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8 COMMENT='U发展银行-部门表';

-- ----------------------------
--  Table structure for `tbl_group`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_group`;
CREATE TABLE `tbl_group` (
  `groupNumber` int(11) NOT NULL AUTO_INCREMENT COMMENT '集团编号',
  `name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '集团名称',
  `registeredCapital` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '注册资本',
  `legalPerson` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人',
  `contacts` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `contactsTelephone` varchar(15) DEFAULT NULL COMMENT '联系人电话',
  `details` text CHARACTER SET utf8 COMMENT '详情',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`groupNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8 COMMENT='U发展银行-集团表';

-- ----------------------------
--  Table structure for `tbl_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_menu`;
CREATE TABLE `tbl_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单名',
  `url` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '页面访问路径',
  `code` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单编码',
  `father` varchar(10) CHARACTER SET utf8 DEFAULT NULL COMMENT '父类编码',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `sorting` varchar(10) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='优发展银行后台-系统菜单表';

insert into tbl_menu(name,url,code,father,icon,sorting) values('业务管理', '', '101', '', '', '101');
insert into tbl_menu(name,url,code,father,icon,sorting) values('转账记录', '/backend/tomanager', '101001', '101', 'icon-recond', '101001');
insert into tbl_menu(name,url,code,father,icon,sorting) values('权限管理', '', '102', '', '', '102');
insert into tbl_menu(name,url,code,father,icon,sorting) values('集团管理', '/backend/group', '102001', '102', 'icon_jituan_default', '102001');
insert into tbl_menu(name,url,code,father,icon,sorting) values('公司管理', '/backend/company', '102002', '102', 'icon_company_default', '102002');
insert into tbl_menu(name,url,code,father,icon,sorting) values('部门管理', '/backend/department', '102003', '102', 'icon_bumen_default', '102003');
insert into tbl_menu(name,url,code,father,icon,sorting) values('系统设置', '', '103', '', '', '103');
insert into tbl_menu(name,url,code,father,icon,sorting) values('模块管理', '/backend/module', '103001', '103', 'icon-module', '103001');
insert into tbl_menu(name,url,code,father,icon,sorting) values('类别管理', '/backend/category', '103002', '103', 'icon_classify', '103002');
insert into tbl_menu(name,url,code,father,icon,sorting) values('URL管理', '/backend/thirdUrl', '103003', '103', 'icon_url_default', '102003');
insert into tbl_menu(name,url,code,father,icon,sorting) values('Banner管理', '/backend/banner', '103004', '103', 'icon_banner_default', '102004');
insert into tbl_menu(name,url,code,father,icon,sorting) values('行政管理', '', '104', '', '', '104');
insert into tbl_menu(name,url,code,father,icon,sorting) values('考勤记录', '/backend/attendance', '104001', '104', 'icon-module', '104001');
insert into tbl_menu(name,url,code,father,icon,sorting) values('考勤统计', '/backend/statistics', '104002', '104', 'icon_classify', '104002');
insert into tbl_menu(name,url,code,father,icon,sorting) values('考勤规则', '/backend/rule', '104003', '104', 'icon_classify', '104003');

-- ----------------------------
--  Table structure for `tbl_attendance_records`
-- ----------------------------
DROP TABLE IF EXISTS tbl_attendance_records;
CREATE TABLE tbl_attendance_records (
   rid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '考勤记录流水号',
   user_id bigint(20)  NOT NULL COMMENT '用户编号',
   user_name varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '员工姓名',
   dept_num int(11) DEFAULT NULL COMMENT '部门',
   company_num int(11) DEFAULT NULL COMMENT '公司',
   clock_date varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '打卡日期',
   clock_time varchar(20) DEFAULT NULL COMMENT '打卡时间',
   clock_type int(5) DEFAULT NULL COMMENT '打卡类型 0：签到 1：签退',
   clock_status int(5) DEFAULT NULL COMMENT '打卡状态 0：正常 1：迟到 2：早退',
   clock_location varchar(100) DEFAULT NULL COMMENT '打卡位置',
   clock_device varchar(100) DEFAULT NULL COMMENT '打卡设备号',
  PRIMARY KEY (rid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考勤记录表'; 

insert into tbl_attendance_records(user_id,user_name,dept_num,company_num,clock_date,clock_time,clock_type,clock_status,clock_location,clock_device)
	values(10,'huangguoqing',40000,20006,'2017-08-11','10:10',0,0,'广东省深圳市南山区海王大厦','iphone100000');
insert into tbl_attendance_records(user_id,user_name,dept_num,company_num,clock_date,clock_time,clock_type,clock_status,clock_location,clock_device)
	values(10,'huangguoqing',40000,20006,'2017-08-11','20:10',1,0,'广东省深圳市南山区海王大厦','iphone100000');

-- ----------------------------
--  Table structure for `tbl_attendance_statistics`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_attendance_statistics`;
CREATE TABLE `tbl_attendance_statistics` (
   sid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '考勤统计流水号',
   user_name varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '员工姓名',
   dept_num int(11) DEFAULT NULL COMMENT '部门',
   company_num int(11) DEFAULT NULL COMMENT '公司',
   normal_clock_on_count int(10) DEFAULT NULL COMMENT '正常打卡次数',
   later_count int(10) DEFAULT NULL COMMENT '迟到次数',
   not_clock_on_count int(10) DEFAULT NULL COMMENT '未签到次数',
   normal_sign_off_count int(10) DEFAULT NULL COMMENT '正常签退次数',
   leave_early_count int(10) DEFAULT NULL COMMENT '早退次数',
   not_clock_off_count int(10) DEFAULT NULL COMMENT '未签退次数',
   PRIMARY KEY (sid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考勤记录统计表';

-- ----------------------------
--  Table structure for `tbl_attendance_rules`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_attendance_rules`;
CREATE TABLE `tbl_attendance_rules` (
   rid bigint(20) NOT NULL AUTO_INCREMENT COMMENT '考勤规则id',
   rule_name varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '规则名称',
   clock_on_time varchar(20) DEFAULT NULL COMMENT '上班打卡时间',
   clock_off_time varchar(20) DEFAULT NULL COMMENT '下班打卡时间',
   clock_on_advance_hours int(5) DEFAULT 0 COMMENT '上班打卡提前小时数',
   clock_on_start_time varchar(20) DEFAULT NULL COMMENT '最早上班打卡时间',
   clock_off_delay_hours int(5) DEFAULT 0 COMMENT '下班打卡延迟小时数',
   clock_off_end_time varchar(20) DEFAULT NULL COMMENT '最晚下班打卡时间',
   rest_start_time varchar(20) DEFAULT NULL COMMENT '休息开始时间',
   rest_end_time varchar(20) DEFAULT NULL COMMENT '休息结束时间',
   workday varchar(30) DEFAULT NULL COMMENT '工作日',
   holiday varchar(300) DEFAULT NULL COMMENT '节假日',
   holiday_flag  int(5) DEFAULT 0 COMMENT '0:遵循, 1:不遵循',
   flexible_time int(5) DEFAULT 0 COMMENT '弹性上班时间',
   flexible_flag int(5) DEFAULT 0 COMMENT '0:开启弹性上班时间, 1:不开启弹性上班时间',
   postpone_flag int(5) DEFAULT 0 COMMENT '0:下班时间自动顺延, 1:下班时间不顺延',
   longit_latit varchar(50) DEFAULT NULL COMMENT '经纬度',
   clock_location varchar(100) DEFAULT NULL COMMENT '打卡位置',
   clock_bounds int(10) DEFAULT NULL COMMENT '打卡范围',
   PRIMARY KEY (rid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='考勤规则表';

insert into tbl_attendance_rules(clock_on_time,clock_off_time,clock_on_advance_hours,clock_on_start_time,clock_off_delay_hours,clock_off_end_time,rest_start_time,rest_end_time,longit_latit,clock_location,clock_bounds)
	values('09:30','18:30',2,'07:30',6,'00:30','12:30','14:00','113.941664,22.542380','深圳市优融网络科技有限公司','1000');
	
-- ----------------------------
--  Table structure for `tbl_holidays`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_holidays`;
CREATE TABLE `tbl_holidays` (
   hid bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
   year varchar(10) DEFAULT NULL COMMENT '年',
   holiday varchar(300) DEFAULT NULL COMMENT '节假日日期',
   workday varchar(300) DEFAULT NULL COMMENT '节假日调休日期',
   PRIMARY KEY (hid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='节假日期表';	
	

-- ----------------------------
--  Table structure for `tbl_users_of_rules`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_users_of_rules`;
CREATE TABLE `tbl_users_of_rules` (
   userid bigint(20) NOT NULL COMMENT '用户id',
   rid bigint(20) NOT NULL COMMENT '考勤规则id',
   deptid int(11) NOT NULL COMMENT '部门id',
   companyid int(11) NOT NULL COMMENT '公司id',
   PRIMARY KEY (userid,rid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与考勤规则关联表';

insert into tbl_users_of_rules(userid,rid,deptid,companyid) values(315,1,'40000','20006');


































