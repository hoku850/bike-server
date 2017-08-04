-- 账户表 30000条（押金）数据，每个用户属于3个运营商 --
-- 积分 0 --
INSERT INTO sys_member_account(SELECT NULL, '0', 0.0, 2, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '0', 0.0, 3, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '0', 0.0, 4, USER_ID FROM sys_user);
-- 预存款 1 --
INSERT INTO sys_member_account(SELECT NULL, '1', 10.0, 2, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '1', 25.0, 3, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '1', 15.0, 4, USER_ID FROM sys_user);
-- 押金 2 --
INSERT INTO sys_member_account(SELECT NULL, '2', 300.0, 2, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '2', 200.0, 3, USER_ID FROM sys_user);
INSERT INTO sys_member_account(SELECT NULL, '2', 350.0, 4, USER_ID FROM sys_user);




-- 充值订单表 30000条 （充值）数据【insert select from】 （每个运营商各10000条）
INSERT INTO bike.prd_charge_order 
     --    充值数     	   充值订单号    充值已完成  		账户ID	     账户日志ID   机构   	流水	    支付类型 --
SELECT NULL, 50, NOW(), m.MEMBER_ACCOUNT_ID, 1, NOW(), m.MEMBER_ACCOUNT_ID,  m.USER_ID, m.ORG_ID, '201707131601001111', 1, NULL, m.USER_ID 
FROM sys_member_account m WHERE ACCOUNT_TYPE_CODE = 1 ;




-- 会员账户日志表 60000条 （充值）数据【insert select from】
-- 会员账户日志表 60000条 （支付骑行订单，每个用户2条骑行记录）数据【insert select from】

INSERT INTO sys_member_account_log
SELECT 	NULL, 100, 100, m.MEMBER_ACCOUNT_ID, m.USER_ID, m.ORG_ID, 0, "充值", NOW(), m.USER_ID
FROM sys_member_account m WHERE ACCOUNT_TYPE_CODE = 1;

INSERT INTO sys_member_account_log
SELECT 	NULL, 150, 50, m.MEMBER_ACCOUNT_ID, m.USER_ID, m.ORG_ID, 100, "充值", NOW(), m.USER_ID
FROM sys_member_account m WHERE ACCOUNT_TYPE_CODE = 1;

INSERT INTO sys_member_account_log
SELECT 	NULL, 15, 15, m.MEMBER_ACCOUNT_ID, m.USER_ID, m.ORG_ID, 0, "充值", NOW(), m.USER_ID
FROM sys_member_account m WHERE ACCOUNT_TYPE_CODE = 0; -- 积分 --

INSERT INTO sys_member_account_log
SELECT 	NULL, 20, 5, m.MEMBER_ACCOUNT_ID, m.USER_ID, m.ORG_ID, 15, "充值", NOW(), m.USER_ID
FROM sys_member_account m WHERE ACCOUNT_TYPE_CODE = 0; -- 积分 --


-- --
INSERT INTO `prd_cycling_trajectory_record` (SELECT NULL, `CYCLING_ORDER_ID`, 23.125415, 113.264286, `END_TIME` FROM `prd_cycling_order`);
INSERT INTO `prd_cycling_trajectory_record` (SELECT NULL, `CYCLING_ORDER_ID`, 23.126402, 113.275745, `END_TIME` FROM `prd_cycling_order`);
INSERT INTO `prd_cycling_trajectory_record` (SELECT NULL, `CYCLING_ORDER_ID`, 23.126481, 113.285572, `END_TIME` FROM `prd_cycling_order`);

-- --
INSERT INTO prd_smart_lock_stat
SELECT NULL, 'N', NOW(), 90, 23.126165, 113.267097, '1', 2, s.SMART_LOCK_ID FROM prd_smart_lock s;