package org.ccframe.client;


/**
 * GWT模块资源定义.
 */
public enum ViewResEnum{

	/** *************** menu start ************** **/
	
	/**
	 * 文章管理 
	 */
	ARTICLE_INF_MGR,
	
	/**
	 * 文章管理窗口 增改
	 */
	ARTICLE_INF_WINDOW,

	/**
	 * 用户管理
	 */
	ADMIN_USER_LIST,

	/**
	 * 用户管理 增改
	 */
	ADMIN_USER_WINDOW,

	/**
	 * 角色管理 
	 */
	ADMIN_ROLE_MGR,

	/**
	 * 文件上传组件 
	 */
	UPLOAD_WINDOW,

	/**
	 * 修改密码窗口
	 */
	USER_PASSWORD_WINDOW, 
	
	/**
	 * 关于本系统
	 */
	ABOUT_SYSTEM, 

	/**
	 * 预设文本管理
	 */
	PREFERENCE_TEXT_EDIT_WINDOW, 

	/**
	 * 用户导入窗体
	 */
	USER_IMPORT_WINDOW,

	/**
	 * 系统参数管理
	 */
	ADMIN_SYS_PARAM,
	/**
	 * 系统缓存管理
	 */
	ADMIN_CACHE_INF,
	/**
	 * 物品管理
	 */
	GOODS_MGR,
	
	/**
	 * 物品管理窗体
	 */
	GOODS_WINDOW,
	
	/**
	 * 人员档案管理
	 */
	PERSONNEL_ARCHIVES_LIST, 
	
	/**
	 * 人员档案管理 增改
	 */
	PERSONNEL_ARCHIVES_WINDOW,
	
	/**
	 * 物品导入窗体
	 */
	GOOD_IMPORT_WINDOW,
	
	/**
	 * 显示为接诊列表
	 */ 
	UN_RECEPTED_LIST,
	/**
	 * 流程第一步挂号
	 */
	REGISTER_INF_ADD,
	
	/**
	 * 人员档案导入窗体
	 */
	PERSONNEL_ARCHIVES_IMPORT_WINDOW,
	
	/**
	 * 医保信息导入窗体
	 */
	PERSONNEL_CARD_NUM_IMPORT_WINDOW,
	
	REGISTER_MGR,
	
	/**
	 * 流程第三步诊断
	 */
	DIAGNOSIS_ADD,
	
	/**
	 * ‘流程第三步诊’断的‘历史记录’窗口
	 */
	ILLNESS_RECORD_LIST,
	/**
	 * 添加药品窗体
	 */
	MEDICINAL_ADD_WINDOW,
	/**
	 * ‘流程第三步诊’断的‘补充转诊证明’窗口
	 */
	ADD_TRANSFER_TREATMENT_PROVE_WINDOW,
	
	/**
	 * ‘流程第三步诊’断的‘补充休假证明’窗口
	 */
	ADD_SICK_LEAVE_PROVE_WINDOW,
	
	/**
	 * ‘流程第三步诊’断的‘补充换药用法’窗口
	 */
	ADD_CHANGE_MEDICAL_PRESCRIPTION_WINDOW,
	
	/**
	 * ‘流程第三步诊’中独立出来的‘人员档案’
	 */
	MEDICINAL_ADD_WINDOW_PERSONNELARCHIVES,
	
	/**
	 * ‘流程第三步诊’断的‘补充注射用法’窗口
	 */
	ADD_INJECT_PRESCRIPTION_WINDOW_VIEW,
	
	/**
	 * 流程第四步收费，未收费列表
	 */
	UN_PAYED_LIST,
	/**
	 * 盘库管理
	 */
	INVENTORY_CHECK_MGR,
	 /**
     * 新的盘库作业窗体
     */
	INVENTORY_CHECK_ADD_WINDOW,
	 /**
     *   处理盘库差异窗体
     */
    FIX_INVENTORY_DIFF,
	/**
	 * 流程第四步收费，费用明细
	 */
	CHARGE_WINDOW, 
	/**
	 * 流程第五步取药
	 */
	GET_MEDICINAL_LIST,
	
	/**
	 * 流程第五步，费用明细
	 */
	GET_MEDICINAL_DETAIL_DTO_WINDOW,
	
	/**
	 * 护士站
	 */
	NURSE_STATION_LIST,
	
	/**
	 * 处理治疗单窗口
	 */
	TREATMENT_RECIPE_DETAIL_WINDOW,
	/**
	 * 库房设置窗体
	 */
	SET_DEPOSITION_WINDOW,
	
	/**
	 * 库房增加窗体
	 */
	ADD_DEPOSITION_WINDOW, 
	
	/**
	 * 单车类型管理
	 */
	BIKE_TYPE_LIST, 
	
	/**
	 * 单车类型增加窗体
	 */
	BIKE_TYPE_WINDOW,
	
	/**
	 * 骑行订单管理
	 */
	CYCLING_ORDER_LIST,
	
	/**
	 * 骑行订单增加窗体
	 */
	CYCLING_ORDER_WINDOW,
	
	/**
	 *运营商APP列表
	 */
	AGENT_APP_LIST,
	
	/**
	 *运营商APP增改窗口
	 */
	AGENT_APP_WINDOW,
	
	/**
	 * 智能锁导入窗口
	 */
	SMART_LOCK_IMPORT_WINDOW,
	
	/**
	 * 智能锁发放窗口
	 */
	SMART_LOCK_GRANT_WINDOW,
	
	/**
	 * 智能锁列表
	 */
	SMART_LOCK_LIST,
	
	/**
	 * 智能锁增改
	 */
	SMART_LOCK_WINDOW,
	
	/**
	 * 用户保修记录
	 */
	USER_TO_REPAIR_RECORD,
	USER_TO_REPAIR_WINDOW,
	/**
	 * 充值订单管理
	 */
	CHARGE_ORDER_LIST, 
	
	/**
	 * 账户管理
	 */
	MEMBER_ACCOUNT_MGR, 
	
	/**
	 * 账户管理新增窗口
	 */
	MEMBER_ACCOUNT_WINDOW, 
	
	/**
	 * 运营商管理
	 */
	AGENT_LIST_LIST,
	
	/**
	 * 运营商窗口
	 */
	AGENT_WINDOW, 
	
	/**
	 * 运营商成功添加后提示账户和密码的窗口
	 */
	AGENT_TIP_WINDOW;
	
	
	public static ViewResEnum fromValue(String value){
		for(ViewResEnum presenterResEnum: ViewResEnum.values()){
			if(presenterResEnum.name().equals(value)){
				return presenterResEnum;
			}
		}
		return null;
	}
	
	public String toValue(){
		return this.name();
	}
}
