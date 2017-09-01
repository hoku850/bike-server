package org.ccframe.client;

import java.util.Date;

import org.ccframe.client.commons.UtilDateTimeClient;


/**
 * 系统常量定义.
 * @author Jim
 *
 */
public interface Global {
	//数据模型常量
	String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
	String FIELD_AS_STRING_SUFFIX = "Str"; //时间和日期的字符串转换方法后缀
	String ES_DEFAULT_INDEX = "default_index";
	String ES_DEFAULT_ANALYSER = "elasticsearch-analyser.json";

	//框架常量
	String SESSION_VALIDATE_CODE = "org_ccframe_ValidateCode";
	String SESSION_LOGIN_ADMIN = "org_ccframe_LoginAdmin";
	String SESSION_LOGIN_MEMBER_USER = "org_ccframe_LoginMember_User";
	String SESSION_START_CYCLING_TIME = "session_start_cycling_time";

	String WEB_ROOT_PROPERTY = "webApp.root";
	
	String LOGIN_REDIRECT_URL = "redirectUrl";
	
	String PRESENTER_RES_KEY = "PresenterRes";
	String REST_REQUEST_URL_SUFFIX = ".json";
	String ACTION_REQUEST_URL_SUFFIX = ".do";
	String ID_BINDER_ID = "id";
	String ID_BINDER_PATH = "/{" + ID_BINDER_ID + "}";
	String DEPOSITON_BINDER_DEPOSITON = "depositionId";
	String DEPOSITON_BINDER_PATH = "/{" + DEPOSITON_BINDER_DEPOSITON + "}";
	String DELETE_DATA_KEY = "@Del";

	String SERVICE_CLASS_SUFFIX = "Service";
	String SEARCH_SERVICE_CLASS_SUFFIX = "SearchService";
	String EH_CACHE_AUTO_CACHE = "auto";
	String EH_CACHE_RESOLVER = "ehCacheResolver";

	String FAKE_PASSWORD = "ABCD1234"; //涉及用户密码的返回统一的假密码
	
	// 经纬度使用
	String NORTH = "N";
	String SOUTH = "S";
	String EAST = "E";
	String WEST = "W";
	String COMMA = ", ";
	
	// 时间
	String HOUR = "时";
	String MINUTE = "分";
	String SECOND = "秒";
	
	// EXCEL导出使用
	String UPLOAD_DIR = "upload";
	String TEMP_DIR = "temp";
	
	String EXCEL_EXPORT_TEMPLATE_DIR = "exceltemplate";
	String EXCEL_EXPORT_POSTFIX = ".xls";
	String EXCEL_EXPORT_CYCLING_ORDER = "cyclingOrderListExcel" + EXCEL_EXPORT_POSTFIX;
	String EXCEL_EXPORT_SMART_LOCK = "smartLockListExcel" + EXCEL_EXPORT_POSTFIX;
	String EXCEL_EXPORT_CHARGE_ORDER = "chargeOrderListExcel" + EXCEL_EXPORT_POSTFIX;
	
	// 单位
	String KM = " km";
	
	String SPRING_MVC_JSON_SUCCESS = "OK.";
	
	Date MIN_SEARCH_DATE = new Date(0L);
	Date MAX_SEARCH_DATE = UtilDateTimeClient.convertStringToDateTime("2199-01-01 00:00:00");
	
	//GWT配置
	String DEFAULT_GWT_BORDER_COLOR = "#6ca8d3";
	String DEFAULT_GWT_WIDGET_BORDER = "1px " + DEFAULT_GWT_BORDER_COLOR + " solid";
	String ENUM_TEXT_SPLIT_CHAR = ",";
	
	int COMBOBOX_ALL_VALUE = 0; //下拉框选择全部的值
	
	int NEW_OBJECT_ID = -1; 
	int DEFAULT_USER_LEVEL = 1;
	
	int ADMIN_USER_ID = 1;
	int PLATFORM_ORG_ID = 1; //总平台的机构ID

	int FAST_MENU_TOOLS_NUM = 4; //快速操作按钮
	
	//导入的几个状态
	double IMPORT_INIT = 2.0; //初始状态
	double IMPORT_SUCCESS_ALL = 3.0; //全部导入成功
	double IMPORT_SUCCESS_WITH_ERROR = 4.0; //导入成功，但是有错误，详见错误文件
	double IMPORT_ERROR = 5.0; //导入失败
	
	//发放的几个状态
	double GRANT_INIT = 2.0; //初始状态
	double GRANT_SUCCESS_ALL = 3.0;
	double GRANT_SUCCESS_WITH_ERROR = 4.0; //部分发放成功
	double GRANT_ERROR = 5.0;
	
	//优选文本的分隔符，默认是回车\n
	String PREFERENCE_TEXT_SPLIT_STR = "\n";
	
	//各种树节点
	public static enum TreeRootEnum{

		MENU_TREE_ROOT(2),
		ARTICLE_CATEGORY_TREE_ROOT(3);

		private int treeNodeId;
		private TreeRootEnum(int treeNodeId){
			this.treeNodeId = treeNodeId;
		}
		public int getTreeNodeId(){
			return treeNodeId;
		}
	}

	//RegEx
	public static final String USER_PASSWORD_PATTERN = "^(?=.*[0-9].*)(?=.*[a-zA-Z].*).{7,}$"; //包含字符数字大于等于8
	public static final String EMAIL_PATTERN = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	public static final String MOBILE_PATTERN = "^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$";

	//停止定时器的CRON
	String DISABLED_SCHEDULED_CRON = "1 1 1 1 JAN *";
	
	// 运营商默认管理密码
	String AGRNT_DEFAULT_PASSWORD = "admin";
	
	//天安门经纬度
	public static final Double BEIJING_LNG = 116.397390;
	public static final Double BEIJING_LAT = 39.908860;
	
	public String DAY_START_TIME = "00:00:00";
	public String DAY_END_TIME = "23:59:59";
	
	// 添加机构默认的管理员 2位机构用户序列号
	public int TRY_START_SEQ = 1;
	public int TRY_END_SEQ = 99;

	//前端app使用的常量
	String PAYMENT_TRANSACTIONAL_NUMBER = "2017071316010001";//TODO 下一步要得到动态的支付流水
	Integer RADIUS = 10000;
	String SUCCESS = "success";
	String FAIL = "fail";

	String TO_REPAIR_DREE = "保修金额减免";

	public String FORMAT_HARDWARECODE = "%016d";
	public String MAP_BASE = "ccframe/map/";

}
