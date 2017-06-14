/*
-- Query: select `type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code` from tsys_dict
LIMIT 0, 10000

-- Date: 2017-04-01 19:22
*/
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'bankcard_status','银行卡状态','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','bankcard_status','0','弃用','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','bankcard_status','1','启用','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'currency','货币','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','currency','CNY','人民币','admin',now(),'','CD-JKEG000011');

INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'account_type','账户类型','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_type','C','C端用户','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_type','B','B端用户','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_type','PA','合伙人用户','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_type','P','平台用户','admin',now(),'','CD-JKEG000011');

INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'account_status','账户状态','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_status','0','正常','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_status','1','程序锁定','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','account_status','2','人工锁定','admin',now(),'','CD-JKEG000011');

INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'jour_status','流水状态','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','jour_status','1','待对账','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','jour_status','3','已对账且账已平','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','jour_status','4','账不平待调账','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','jour_status','5','已调账','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','jour_status','6','无需对账','admin',now(),'','CD-JKEG000011');

INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'channel_type','渠道类型','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','channel_type','0','内部账','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','channel_type','35','微信H5支付','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','channel_type','37','微信扫码支付','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','channel_type','90','人工线下','admin',now(),'','CD-JKEG000011');

INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('0',NULL,'biz_type','业务类型','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','11','充值','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','-11','取现','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','19','蓝补','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','-19','红冲','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','JKEG_O2O_RMB','店铺消费云币支付','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','JKEG_FW','服务购买','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','JKEG_FWTK','服务购买退款','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','03','发帖送积分','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','04','评论送积分','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','05','点赞加积分','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','06','被点赞送积分','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','07','被评论送积分','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','10','删除帖子','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','11','删除评论','admin',now(),'','CD-JKEG000011');
INSERT INTO `tsys_dict` (`type`,`parent_key`,`dkey`,`dvalue`,`updater`,`update_datetime`,`remark`,`system_code`) VALUES ('1','biz_type','12','取消点赞','admin',now(),'','CD-JKEG000011');

/*
-- Query: SELECT * FROM std_account.tstd_account where user_id = 'CD-JKEG000011'
-- Date: 2016-12-29 15:08
*/
INSERT INTO `tstd_account` (`account_number`,`user_id`,`real_name`,`type`,`status`,`currency`,`amount`,`frozen_amount`,`md5`,`create_datetime`,`last_order`,`company_code`,`system_code`) VALUES ('CD-JKEG000011','SYS_USER_JKEG_TG','平台托管账户','P','0','CNY',0,0,'b99e0407fedc3d160f73fec8d1fa9a0c',now(),NULL,'CD-JKEG000011','CD-JKEG000011');
INSERT INTO `tstd_account` (`account_number`,`user_id`,`real_name`,`type`,`status`,`currency`,`amount`,`frozen_amount`,`md5`,`create_datetime`,`last_order`,`company_code`,`system_code`) VALUES ('JKEGA2016100000000000000','SYS_USER_JKEG','平台人民币账户','P','0','CNY',0,0,'d3b907844e4c1c51573c6be663f57eaf',now(),NULL,'CD-JKEG000011','CD-JKEG000011');
INSERT INTO `tstd_account` (`account_number`,`user_id`,`real_name`,`type`,`status`,`currency`,`amount`,`frozen_amount`,`md5`,`create_datetime`,`last_order`,`company_code`,`system_code`) VALUES ('JKEGA2016100000000000001','SYS_USER_JKEG','平台积分账户','P','0','JF',0,0,'6a3ea19778f8e415f8371457636abf7a',now(),NULL,'CD-JKEG000011','CD-JKEG000011');

/*
-- Query: SELECT bank_code,bank_name,channel_type,status,channel_bank,max_order,order_amount,day_amount,month_amount,remark FROM std_account.tstd_channel_bank
LIMIT 0, 50000

-- Date: 2017-01-15 20:50
*/
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('ICBC','中国工商银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('ABC','中国农业银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('CCB','中国建设银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('BOC','中国银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('BCM','中国交通银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('CIB','兴业银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('CITIC','中信银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('CEB','中国光大银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('PAB','平安银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('PSBC','中国邮政储蓄银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('SHB','上海银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('SPDB','浦东发展银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `tstd_channel_bank` (`bank_code`,`bank_name`,`channel_type`,`status`,`channel_bank`,`max_order`,`order_amount`,`day_amount`,`month_amount`,`remark`) VALUES ('CIB','兴业银行','40','1',NULL,NULL,NULL,NULL,NULL,NULL);
