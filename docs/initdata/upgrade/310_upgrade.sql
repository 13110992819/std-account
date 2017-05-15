DROP TABLE IF EXISTS `tstd_charge`;
CREATE TABLE `tstd_charge` (
  `code` varchar(32) NOT NULL COMMENT '针对编号',
  `account_number` varchar(32) DEFAULT NULL COMMENT '针对账号',
  `account_name` varchar(32) DEFAULT NULL COMMENT '针对户名',
  `amount` bigint(20) DEFAULT NULL COMMENT '充值金额',
  `channel_type` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_card_info` varchar(255) DEFAULT NULL COMMENT '支付渠道账号信息',
  `pay_card_no` varchar(32) DEFAULT NULL COMMENT '支付渠道账号',
  
  `pay_group` varchar(32) DEFAULT NULL COMMENT '支付组号',
  `status` varchar(4) NOT NULL COMMENT '状态',
  `apply_user` varchar(32) DEFAULT NULL COMMENT '申请人',
  `apply_note` varchar(255) DEFAULT NULL COMMENT '申请说明',
  `apply_datetime`  datetime DEFAULT NULL COMMENT '申请时间',
  
  `pay_user` varchar(32) DEFAULT NULL COMMENT '支付回录人',
  `pay_note` varchar(255) DEFAULT NULL COMMENT '支付渠道说明',
  `pay_code` varchar(32) DEFAULT NULL COMMENT '支付渠道订单编号',
  `pay_datetime` datetime DEFAULT NULL COMMENT '支付时间',
  `system_code` varchar(32) DEFAULT NULL COMMENT '系统编号',
  PRIMARY KEY (`code`) COMMENT '充值订单'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tstd_withdraw`;
CREATE TABLE `tstd_withdraw` (
  `code` varchar(32) NOT NULL COMMENT '针对编号',
  `account_number` varchar(32) DEFAULT NULL COMMENT '针对账号',
  `account_name` varchar(32) DEFAULT NULL COMMENT '针对户名',
  `amount` bigint(20) DEFAULT NULL COMMENT '取现金额',
  `fee` bigint(20) DEFAULT NULL COMMENT '手续费',

  `channel_type` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_card_info` varchar(255) DEFAULT NULL COMMENT '支付渠道账号信息',
  `pay_card_no` varchar(32) DEFAULT NULL COMMENT '支付渠道账号',
  `status` varchar(4) NOT NULL COMMENT '状态',
  `apply_user` varchar(32) DEFAULT NULL COMMENT '申请人',
  
  `apply_note` varchar(255) DEFAULT NULL COMMENT '申请说明',
  `apply_datetime`  datetime DEFAULT NULL COMMENT '申请时间',
  `approve_user` varchar(32) DEFAULT NULL COMMENT '审批人',
  `approve_note` varchar(255) DEFAULT NULL COMMENT '审批说明',
  `approve_datetime` varchar(32) DEFAULT NULL COMMENT '审批时间',
  
  `pay_user` varchar(32) DEFAULT NULL COMMENT '支付回录人',
  `pay_note` varchar(32) DEFAULT NULL COMMENT '支付回录说明',
  `pay_group` varchar(32) DEFAULT NULL COMMENT '支付组号',
  `pay_code` varchar(32) DEFAULT NULL COMMENT '支付渠道订单编号',
  `pay_datetime` datetime DEFAULT NULL COMMENT '支付回录时间',
  `system_code` varchar(32) DEFAULT NULL COMMENT '系统编号',
  PRIMARY KEY (`code`) COMMENT '取现订单'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tstd_hlorder`
-- ----------------------------
DROP TABLE IF EXISTS `tstd_hlorder`;
CREATE TABLE `tstd_hlorder` (
  `code` varchar(32) NOT NULL COMMENT '编号',
  `account_number` varchar(32) NOT NULL COMMENT '账号',
  `account_name` varchar(32) DEFAULT NULL COMMENT '针对户名',
  `jour_code` varchar(32) DEFAULT NULL COMMENT '流水号',
  `direction` char(1) NOT NULL COMMENT '方向：1=蓝补；0=红冲',
  `amount` bigint(20) NOT NULL COMMENT '金额（精确到厘）',
  `status` varchar(4) NOT NULL COMMENT '状态',
  `apply_user` varchar(32) NOT NULL COMMENT '申请人',
  `apply_note` varchar(255) NOT NULL COMMENT '申请说明',
  `apply_datetime` datetime DEFAULT NULL COMMENT '申请时间',
  `approve_user` varchar(32) DEFAULT NULL COMMENT '审批人（li为程序）',
  `approve_note` varchar(255) DEFAULT NULL COMMENT '审批说明',
  `approve_datetime` datetime DEFAULT NULL COMMENT '审批时间',
  `system_code` varchar(32) NOT NULL COMMENT '系统编号',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


