package org.ccframe.client;

public interface MedicalGlobal {

	//未诊断挂号列表刷新时间 单位秒
	public static final Integer UN_RECEPTED_LIST_RELOAD_TIME = 30;
	//挂号的有效期  单位天
	int REGISTERED_VALIDITY = 1;
	//收费有效期 单位天
	int PAY_VALIDITY = 1;
	//取药有效期 单位天
	int GET_MEDICINAL_VALIDITY = 1;
	//疾控预防统计 单位天
	int DISEASE_MONITOR_WARNING_VALIDITY = 7;
	//最近消费的范围 单位小时
	int RECENT_DIAGNOSIS_CONSUMPTION = 365*24;
	
	//治疗单收费标准
	double CHANGE_MEDICAL_PRESCRIPTION_TYPE_SMALL_PRICE = 12;	//换药小
	double CHANGE_MEDICAL_PRESCRIPTION_TYPE_MIDDLE_PRICE = 15;	//换药中
	double CHANGE_MEDICAL_PRESCRIPTION_TYPE_LARGER_PRICE = 17;	//换药大
	double INJECT_MUSCLE_INJECT_PRICE = 2;						//肌注
	double INJECT_PUST_INJECT_INJECT_PRICE = 5;					//静推
	double INJECT_DRIP_INJECT_PRICE = 12;						//静滴

	//换行
	public static final String NEW_LINE = "\n";
	
	//问诊挂号新人员信息添加
	Integer ADD_NEW_PERSON_IN_REGISTER_INF_ADD = -1;
	
	//新生导入时（新的身份证号）默认有医保,医保卡号全部设为0
	String NEW_STUDENT_MEDICALINSURANCECARDNUM = "0";
}
