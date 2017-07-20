package org.ccframe.client;

/**
 * 后台的REST请求资源定义文件.
 * 
 * @author JIM
 *
 */
public interface ControllerMapping {

	String CLIENT_TO_BASE = "../";

	/** ************ 后台资源 ************ **/
	String ADMIN_BASE = "admin/";
	
	String MAIN_FRAME_BASE = ADMIN_BASE + "mainFrame"; //基地址
	String MAIN_FRAME_DO_LOGOUT = "doLogout"; //登出
	String MAIN_FRAME_DO_LOGIN = "doLogin"; //登录
	String MAIN_FRAME_ADMIN_MENU = "adminMenu"; //权限菜单
	String MAIN_FRAME_GET_MENU_RES = "getMenuRes"; //获取菜单信息
	String MAIN_FRAME_UPDATE_PASSWORD = "updatePassword"; //管理员更改用户密码
	String MAIN_FRAME_MENU_HIT = "menuHit";
	
	String ARTICLE_BASE = ADMIN_BASE + "article"; //基地址+getById(path)+增改(POST)+删除(DELETE)
	String ARTICLE_LIST = "findList"; //按分类搜索文章
	
	String TREE_NODE_BASE = ADMIN_BASE + "treeNode"; //基地址+getById(path)+增改(POST)+删除(DELETE)
	String TREE_NODE_TREE = "tree"; //根据树根获取树
	String TREE_NODE_ORG_MENU_TREE = "orgMenuTree"; //获取机构的模板树
	String TREE_NODE_SUB_TREE = "subTree"; //根据树根获取子树
	String GET_NAME_PATH = "treeNodeToPath"; //根据当前节点和根两个节点获得路径节点名称
	
	String ADMIN_USER_LIST = "findList"; //用户分页列表
	
	String ADMIN_USER_BASE = ADMIN_BASE + "adminUser";
	
	
	
	String ADMIN_ROLE_BASE = ADMIN_BASE + "adminRole";
	String ADMIN_ROLE_LIST = "findList"; //角色列表
	String ADMIN_ROLE_REF_USER_COUNT = "roleRefUserCount"; //删除前检查角色被N个用户使用
	String ADMIN_ROLE_SYS_TREE_NODE_ID_LIST = "roleSysTreeNodeIdList"; //获取角色对应的菜单树节点
	String ADMIN_ROLE_SAVE_ROLE_MENU_LIST = "saveRoleMenuList"; //保存某个角色对应的菜单权限
	String ADMIN_ROLE_USER_LIST = "adminRoleUserList"; //查询某个Role下的用户
	String ADMIN_ROLE_BATCH_DELETE_USER_ROLE_REL = "batchDeleteUserRoleRel"; //批量解除关联用户角色
	String ADMIN_ROLE_BATCH_ADD_USER_ROLE_REL = "batchAddUserRoleRel"; //批量关联用户角色
	
	String ADMIN_FILE_INF_BASE = ADMIN_BASE + "fileInf"; //文件管理
	String ADMIN_FILE_INF_UPLOAD = "upload"; //文件上传
	String ADMIN_FILE_INF_START_IMPORT = "startImport"; //开始导入
	String ADMIN_FILE_INF_QUERY_IMPORT = "queryImport"; //查询导入状态

	String ADMIN_CACHE_BASE = ADMIN_BASE + "cache";
	String ADMIN_CACHE_INF_LIST = "findList"; //缓存信息列表
	String ADMIN_CACHE_CLEAR_CACHES = "clearCaches"; //清理缓存

	String ADMIN_PARAM_BASE = ADMIN_BASE + "param";
	String ADMIN_PARAM_PREFERENCE_TEXT = ADMIN_PARAM_BASE + "preferenceText"; //获取预设文本列表
	String ADMIN_PARAM_LIST = "findList"; //参数列表

	String MEMBER_PATH = "member";
	String MEMBER_MEMBER = MEMBER_PATH + "/member";

	String SIMPLE_LABEL_VALUE_BASE = ADMIN_BASE + "simpleLabelValue"; //物品导出

	String GOODS_INF_BASE = ADMIN_BASE + "goodsInf"; //基地址+getById(path)+增改(POST)+删除(DELETE)
	String GOODS_INF_LIST = "findList"; //按分类搜索文章
	String GOODS_INF_IMPORT = "doImport"; //物品导入
	String GOODS_INF_EXPORT = "doExport"; //物品导出
	String GOODS_INF_PINYIN_NM = "findListByNmOrPinYin";
	String GOODS_INF_INVENTORY_CHECK_DIFFERENCE_EXPORT = "doInventoryCheckDifferenceExport"; //盘库差异单导出
	String GOODS_INF_SAVE_REPEAT = "saveRepeat";
	
	//chuqin
	//人员档案
	String PERSONNEL_ARCHIVES_BASE = ADMIN_BASE + "personnelArchives";
	String PERSONNEL_ARCHIVES_LIST = "findList";
	String GET_ADMIN_USER = "getAdminUser";
	String PERSONNEL_ARCHIVES_IMPORT = "doImport"; //用户档案导入
	String PERSONNEL_ARCHIVES_EXPORT = "doExport"; //用户档案导出
	//挂号列表

	String REGISTER_BASE = "register";
	String REGISTER_LIST = "findList";;
	String REGISTER_SAVE = "save";
	String Register_SEARCH = "search";
	String RECEPTION = "reception";
	String RE_NEW_REGISTER = "reNewRegister";
	
	String PERSONNEL_ARCHIVES_SEARCH = "findByKeyWord";
	/** ************ 会员中心资源 ************ **/
	/**
	 * 诊断
	 */
	String DIAGNOSIS_BASE = "diagnosis";
	String ILLNESS_RECORD_ROW_DTO_LIST = "IllnessRecordRowDto";		//查找历史病情
	String DIAGNOSIS_PRINT = "doPrintDiagnosis";
	String FINISH_DIAGNOSIS = "finishDiagnosis";
	String GET_RECEPTED_DIAGNOSIS = "getReceptedDiagnosis";
	
	String DISEASE_MONITOR_WARNING_BASE = "diseaseMonitorWarning";
	String DISEASE_MONITOR_WARNING_ROW_DTO_LIST = "diseaseMonitorWarningRowDto";

	//处方物品项
	String PRESCRIPTIONGOODSITEM_BASE = ADMIN_BASE+"prescriptiongoodsitem";
	String PRESCRIPTIONGOODSITEM_SAVE = "save";

	//付费
	String PAY_BASE = "pay";
	String FIND_ALL_UN_PAYED_LIST_DTO = "findAllUnPayedListDto";
	String GET_CHARGE_DETAIL_DTO = "getChargeDetailDto";
	String CHARGE = "charge";
	String PAY_PRINT = "doPrintPayResult";
	

	String INVENTORY_CHECK_BASE = ADMIN_BASE + "inventory";
    String INVENTORY_CHECK_LIST = "inventoryList";
    String INVENTORY_CHECK_EXPORT = "doInventoryCheckExport";
    

    String DEPOSIT_LOCATION_BASE = ADMIN_BASE + "depositLocation";
    String DEPOSIT_LOCATION_LIST = "findList";
    String DEPOSIT_LOCATION_DTO = "getDto";
    
	String FIND_GET_MEDICINAL_LIST_DTO_LIST = "findGetMedicinalListDtoList";
	String GET_MEDICINAL_BASE = "getMedicinalBase";
	String GET_GET_MEDICINAL_DETAIL_DTO = "getGetMedicinalDetailDto";
	String FINISH_GET_MEDICINAL = "finishGetMedicinal";
    String COUNT_GOODS_TYPE = "count";
    
    
    //产品药品盘库差别
    String MEIDICINAL_INVENTORY_CHECK_DIFFERENCE_EXPORT = "doMedicinalInventoryCheckDifferenceExport"; //盘库差别列表导出

    String MEDICINAL_INVENTORY_CHECK_DIFF_BASE = "admin/inventoryDiff";
    String INVENTORY_DIFF_LIST = "findList";
    String INVENTORY_DIFF_UPDATE = "update";
    String INVENTORY_DIFF_IMPORT_SHEET = "importSheet";
    //护士站
    String GET_NURSESTATION_BASE = "getNurseStationBase";
    String FIND_NOT_PROCESS_NURSESTATION_LIST_DTO_LIST = "findNotProcessNurseStationListDtoList";
	String TREATMENT_RECIPE_BASE = "treatmentRecipe";
	String GET_TREATMENT_RECIPE_DETAIL = "getTreatmentRecipeDetail";
	String UPDATE_TREATMENT_RECIPE_LAST_PROCESS_TIME = "updateTreatmentRecipeLastProcessTime";
	String FINISH_TREATMENT_RECIPE = "finishTreatmentRecipe";
	String FIND_PROCESS_ING_NURSESTATION_LIST_DTO_LIST = "findProcessingNurseStationListDtoList";
	String FIND_FINISH_PROCESS_NURSESTATION_LIST_DTO_LIST = "findFinishProcessNurseStationListDtoList";
	
	String BIKE_BASE = ADMIN_BASE + "bike"; //基地址+getById(path)+增改(POST)+删除(DELETE)

	//智能锁管理
	String SMART_LOCK_BASE = ADMIN_BASE + "smartLock"; //基地址+getById(path)+增改(POST)+删除(DELETE)
	String SMART_LOCK_GRANT_LIST = "grant"; 
	String SMART_LOCK_SEARCH_LIST = "grantSearch"; 
	String SMART_LOCK_LIST = "findSmartLockList"; //用户分页列表
	String SMART_LOCK_EXPORT = "doExport"; //导出
	String SMART_LOCK_DESERT = "doDesert"; //报废
	
	//单车类型管理
	String BIKE_TYPE_BASE = ADMIN_BASE + "bikeType";
	String BIKE_TYPE_LIST = "findList";
	
	//前端管理
	String MEMBER_BASE = "member/";
	String MEMBER_CHARGEAMOUNT_BASE = MEMBER_BASE + "chargeAmount";
	String MEMBER_LOGIN_BASE = MEMBER_BASE + "login";
	String MEMBER_TRAVEL_BASE = MEMBER_BASE + "travel";
	String MEMBER_TRADEDETAIL_BASE = MEMBER_BASE + "tradeDetail";
	String MEMBER_FIXREPORT_BASE = MEMBER_BASE + "fixReport";
	String MEMBER_USINGBIKE_BASE = MEMBER_BASE + "usingBike";
	String MEMBER_ORDER_PAY_BASE = MEMBER_BASE + "orderPay";
	
	//骑行订单管理
	String CYCLING_ORDER_BASE = ADMIN_BASE + "cyclingOrder";
	String CYCLING_ORDER_LIST = "findList";
	String CYCLING_ORDER_EXPORT = "doExport"; //导出

	//运营商APP管理
	String AGENT_APP_BASE = ADMIN_BASE + "agentApp";
	String AGENT_APP_LIST = "findAgentAppList";

	//充值订单管理
	String CHARGE_ORDER_BASE = ADMIN_BASE + "chargeOrder";
	String CHARGE_ORDER_LIST = "findList";
	String CHARGE_ORDER_EXPORT = "doExport"; //导出
	
	//用户报修记录
	String USER_TO_REPAIR_RECORD_BASE = ADMIN_BASE + "userToRepairRecord";
	String USER_TO_REPAIR_RECORD_LIST = "findList";
	
	// 账户管理
	String MEMBER_ACCOUNT_BASE = ADMIN_BASE + "memberAccount";
	String MEMBER_ACCOUNT_LIST = "findList";

	// 账户交易日志
	String MEMBER_ACCOUNT_LOG_BASE = ADMIN_BASE + "memberAccountLog";

	// 运营商管理
	String ORG_BASE = ADMIN_BASE + "org";
	String ORG_LIST = "findList";

	String AGENT_BASE = ADMIN_BASE + "agent";
	String AGENT_LIST = "findList";
}
