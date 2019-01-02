DROP TABLE IF EXISTS `t_business_img`;
CREATE TABLE `t_business_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_code` varchar(32) NOT NULL COMMENT '业务编码如tradeInfoId,orderCode',
  `business_type` tinyint(4) NOT NULL COMMENT '业务类型1资讯图片，2订单支付凭证图片……',
  `img_path` varchar(128) NOT NULL COMMENT '图片路径',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_bi_businesscode` (`business_code`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='业务图片表';

DROP TABLE IF EXISTS `t_client_feedback`;
CREATE TABLE `t_client_feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `feedback_content` text COMMENT '反馈内容',
  `creator_account_name` varchar(32) NOT NULL COMMENT '反馈人登录名',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='客户反馈表';

DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(32) DEFAULT NULL COMMENT '资讯标题',
  `content` text COMMENT '内容',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='行业资讯';

DROP TABLE IF EXISTS `t_platform_config`;
CREATE TABLE `t_platform_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `config_key` varchar(32) NOT NULL COMMENT '配置类型1关于我们2交易app主图3用户app主图4用户pc主图',
  `config_value` text COMMENT '配置内容',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='平台配置表';

DROP TABLE IF EXISTS `t_recycle_order`;
CREATE TABLE `t_recycle_order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_code` varchar(32) NOT NULL COMMENT '订单编码',
  `item_type` tinyint(4) DEFAULT NULL COMMENT '废品类型1废家电2废钢铁3废塑料4废玻璃5其他',
  `item_qty` tinyint(4) DEFAULT NULL COMMENT '废品数量1 1-10台，2 11-50台，3 50台以上，4 10-500kg，5 500-1000kg，6 1000kg以上',
  `address` varchar(128) DEFAULT NULL COMMENT '回收地址',
  `remark` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `order_status` tinyint(4) DEFAULT NULL COMMENT '订单状态1待接单2待回收3待结算4已完成5已取消',
  `creator_user_id` int(11) NOT NULL COMMENT '订单创建人用户id',
  `order_receiver` varchar(32) DEFAULT NULL COMMENT '订单接收人',
  `received_time` datetime DEFAULT NULL COMMENT '接收时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `idx_ro_ordercode` (`order_code`) USING BTREE,
  KEY `idx_ro_userid` (`creator_user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='回收订单';


DROP TABLE IF EXISTS `t_system_log`;
CREATE TABLE `t_system_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `log_content` text COMMENT '操作内容',
  `creator_account_name` varchar(32) NOT NULL COMMENT '操作人登陆名',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='系统日志表';


DROP TABLE IF EXISTS `t_trade_info`;
CREATE TABLE `t_trade_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '供求信息id',
  `trade_type` tinyint(4) DEFAULT NULL COMMENT '交易类型1供应2求购',
  `item_type` tinyint(4) DEFAULT NULL COMMENT '废品类型1废家电2废钢铁3废塑料4废玻璃5其他',
  `content` text COMMENT '信息内容',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态1有效2无效',
  `creator_user_id` int(11) NOT NULL COMMENT '信息发布人用户id',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_ti_tradetype` (`trade_type`) USING BTREE,
  KEY `idx_ti_itemtype` (`item_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供求信息';



DROP TABLE IF EXISTS `t_transaction_order`;
CREATE TABLE `t_transaction_order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_code` varchar(32) NOT NULL COMMENT '订单编码',
  `order_type` tinyint(4) NOT NULL COMMENT '订单类型1销售单2采购单',
  `item_type` tinyint(4) DEFAULT NULL COMMENT '废品类型1废家电2废钢铁3废塑料4废玻璃5其他',
  `item_weight` decimal(7,2) DEFAULT NULL COMMENT '废品重量',
  `item_price` decimal(10,2) DEFAULT NULL COMMENT '废品单价',
  `order_amount` decimal(10,2) DEFAULT NULL COMMENT '订单金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `buyer_user_id` int(11) DEFAULT NULL COMMENT '买方用户id',
  `seller_user_id` int(11) DEFAULT NULL COMMENT '卖方用户id',
  `creator_user_id` int(11) DEFAULT NULL COMMENT '创建人用户id',
  `order_status` tinyint(4) DEFAULT NULL COMMENT '订单状态1待接单2待交货3待结算4已完成5已取消',
  `order_receiver` varchar(32) DEFAULT NULL COMMENT '订单接收人',
  `received_time` datetime DEFAULT NULL COMMENT '接收时间',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `idx_to_ordercode` (`order_code`) USING BTREE,
  KEY `idx_to_ordertype` (`order_type`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='交易订单';


DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `account_name` varchar(32) NOT NULL DEFAULT '' COMMENT '登陆账号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `salt` varchar(8) NOT NULL COMMENT '密码加盐',
  `account_type` tinyint(4) DEFAULT NULL COMMENT '1平台用户2回收废品用户3交易成品用户',
  `role_codes` varchar(128) DEFAULT NULL COMMENT '角色编码，以逗号拼接',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态1启用0禁用',
  `avatar_img_path` varchar(128) DEFAULT NULL COMMENT '头像图片路径',
  `verification_status` tinyint(4) DEFAULT NULL COMMENT '是否认证1是0否',
  `verification_content` varchar(32) DEFAULT NULL COMMENT '认证内容',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `is_init` tinyint(4) DEFAULT NULL COMMENT '是否初始化密码1是0否',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_u_accountname` (`account_name`) USING BTREE,
  KEY `idx_u_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户表';



DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contact_name` varchar(32) DEFAULT NULL COMMENT '联系人',
  `contact_tel` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `province_name` varchar(32) DEFAULT NULL COMMENT '省份名称',
  `city_name` varchar(32) DEFAULT NULL COMMENT '城市名称',
  `city_code` varchar(32) DEFAULT NULL COMMENT '城市编码',
  `region_name` varchar(32) DEFAULT NULL COMMENT '区域名称',
  `address_detail` varchar(128) DEFAULT NULL COMMENT '详细地址',
  `user_id` int(11) NOT NULL COMMENT '地址用户id',
  `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_ua_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户地址';


INSERT INTO `t_platform_config` (`config_key`, `config_value`, `creator`, `created_time`) 
VALUES ('aboutus', 'cskc是一家立足于环保的废品交易工业园', 'system', now());

INSERT INTO `t_platform_config` (`config_key`, `config_value`, `creator`, `created_time`) 
VALUES ('transaction_app_main_img_path', 'http://cskcu.pingyuxian.cn/banner.jpg', 'system', now());

INSERT INTO `t_platform_config` (`config_key`, `config_value`, `creator`, `created_time`) 
VALUES ('user_app_main_img_path', 'http://cskcu.pingyuxian.cn/banner.jpg', 'system', now());

INSERT INTO `t_platform_config` (`config_key`, `config_value`, `creator`, `created_time`) 
VALUES ('user_pc_main_img_path', 'http://cskcu.pingyuxian.cn/banner.jpg', 'system', now());


