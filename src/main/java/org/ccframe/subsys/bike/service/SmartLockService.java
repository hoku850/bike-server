package org.ccframe.subsys.bike.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.BatchImportSupport;
import org.ccframe.commons.data.ExcelReaderError;
import org.ccframe.commons.data.ImportDataCheckUtil;
import org.ccframe.commons.data.ListExcelReader;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.ccframe.subsys.bike.repository.SmartLockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockService extends BaseService<SmartLock, java.lang.Integer, SmartLockRepository>
		implements BatchImportSupport<SmartLock> {

	private static final String SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME = "smartLockImport.xls";

	private Map<String, Double> importStatusMap = new ConcurrentHashMap<String, Double>();

	@Transactional
	public void saveOrUpdateSmartLock(SmartLock smartLock) {
		if (smartLock.getOrgId() == 0 || smartLock.getBikeTypeId() == 0) {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "运营商或单车类型没选择！！！" });
		} else {
			SmartLock smartLockCheck = null;
			SmartLock dbSmartLock = null;
			if(smartLock.getSmartLockId() != null){//smartLockId已存在
				smartLockCheck = SpringContextHelper.getBean(SmartLockService.class).getById(smartLock.getSmartLockId());
				if(smartLockCheck != null){//数据库中smartLockId已存在,update
					dbSmartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, smartLock.getLockerHardwareCode());
					if (dbSmartLock != null && !dbSmartLock.getLockerHardwareCode().equals(smartLockCheck.getLockerHardwareCode())) {// 硬件编码码存在,并且
						throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "硬件编码重复！！！" });
					}
					dbSmartLock = this.getByKey(SmartLock.IMEI_CODE, smartLock.getImeiCode());
					if (dbSmartLock != null && !dbSmartLock.getImeiCode().equals(smartLockCheck.getImeiCode())) {// IMEI码存在
						throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "IMEI码重复！！！" });
					}
					dbSmartLock = this.getByKey(SmartLock.MAC_ADDRESS, smartLock.getMacAddress());
					if (dbSmartLock != null && !dbSmartLock.getMacAddress().equals(smartLockCheck.getMacAddress())) {// MAC地址存在
						throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "MAC地址重复！！！" });
					}
					dbSmartLock = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, smartLock.getBikePlateNumber());
					if (dbSmartLock != null && !dbSmartLock.getBikePlateNumber().equals(smartLockCheck.getBikePlateNumber())) {// 单车车牌号存在
						throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "单车车牌号重复！！！" });
					}
				}
			}else{//smartLockId不存在，add
				dbSmartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, smartLock.getLockerHardwareCode());
				if (dbSmartLock != null) {// 硬件编码存在
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "硬件编码重复！！！" });
				}
				dbSmartLock = this.getByKey(SmartLock.IMEI_CODE, smartLock.getImeiCode());
				if (dbSmartLock != null) {// IMEI码存在
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "IMEI码重复！！！" });
				}
				dbSmartLock = this.getByKey(SmartLock.MAC_ADDRESS, smartLock.getMacAddress());
				if (dbSmartLock != null) {// MAC地址存在
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "MAC地址重复！！！" });
				}
				dbSmartLock = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, smartLock.getBikePlateNumber());
				if (dbSmartLock != null) {// 单车车牌号存在
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "单车车牌号重复！！！" });
				}
			}
			SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
		}
	}

	public Integer countSmartLockOfAgent(Integer agentId) {
		List<SmartLock> list = this.getRepository()
				.findAll(new Criteria<SmartLock>().add(Restrictions.eq(SmartLock.ORG_ID, agentId)));
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Transactional
	public void myDeleteById(Integer smartLockId) {
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(smartLockId);

		if (SpringContextHelper.getBean(CyclingOrderService.class).getById(smartLock.getBikeTypeId()) == null) {
			deleteById(smartLockId);
		} else {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "骑行订单表中使用到该记录，暂时不能删除！！！" });
		}
	}

	private List<ExcelReaderError> dataLogicCheck(SmartLock checkValue, int rowNum) {
		List<ExcelReaderError> resultList = new ArrayList<>();
		ImportDataCheckUtil.stringCheck("硬件编码", 32, false, checkValue.getLockerHardwareCode(), rowNum, 0, resultList);
		ImportDataCheckUtil.stringCheck("IMEI码", 15, false, checkValue.getImeiCode(), rowNum, 1, resultList);
		ImportDataCheckUtil.stringCheck("MAC地址", 17, false, checkValue.getMacAddress(), rowNum, 2, resultList);
		ImportDataCheckUtil.stringCheck("单车车牌号", 15, false, checkValue.getBikePlateNumber(), rowNum, 3, resultList);
		ImportDataCheckUtil.enumCheck("运营商", "总平台,摩拜单车,ofo小黄单,小蓝车,Hello,小鸣", checkValue.getOrgId().toString(), rowNum, 4, resultList);
		ImportDataCheckUtil.enumCheck("状态", "未生产,已生产,已发放,已激活,维修中,已废弃", checkValue.getSmartLockStatCode(), rowNum, 5, resultList);
		ImportDataCheckUtil.enumCheck("单车类型", "标准单车,摩拜 lite,摩拜  小橙车,ofo lite,ofo 经典,小蓝 lite,小蓝 经典,Hello Bike lite,Hello Bike 经典,小鸣 lite,小鸣 经典", checkValue.getBikeTypeId().toString(), rowNum, 6, resultList);
		return resultList;
	}

	@Override
	public List<ExcelReaderError> importBatch(int rowBase, List<SmartLock> importList, boolean isLastRow,
			Map<String, Object> importParam) {
		List<ExcelReaderError> resultList = new ArrayList<ExcelReaderError>();
		int rowNum = rowBase;
		for (SmartLock rowSmartLock : importList) {
			List<ExcelReaderError> checkErrorList = dataLogicCheck(rowSmartLock, rowNum);
			rowNum++;
			if (checkErrorList.size() > 0) { // 有错误，不处理
				resultList.addAll(checkErrorList);
				continue;
			}

			SmartLock smartLock = null;
			smartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, rowSmartLock.getLockerHardwareCode());//数据库内容

			// 判断重复
			SmartLock check = null;
			if (smartLock != null) {// 硬件编号存在,update
				check = this.getByKey(SmartLock.IMEI_CODE, rowSmartLock.getImeiCode());
				if (check != null && !check.getImeiCode().equals(smartLock.getImeiCode())) {// IMEI码存在,并且
					resultList.add(new ExcelReaderError(1, rowNum - 1, "IMEI码重复"));
					continue;
				}
				check = this.getByKey(SmartLock.MAC_ADDRESS, rowSmartLock.getMacAddress());
				if (check != null && !check.getMacAddress().equals(smartLock.getMacAddress())) {// MAC地址存在,并且
					resultList.add(new ExcelReaderError(2, rowNum - 1, "MAC地址重复"));
					continue;
				}
				check = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, rowSmartLock.getBikePlateNumber());
				if (check != null && !check.getBikePlateNumber().equals(smartLock.getBikePlateNumber())) {// 单车车牌号存在，并且
					resultList.add(new ExcelReaderError(3, rowNum - 1, "单车车牌号重复"));
					continue;
				}
			} else {// 硬件编码不存在,add
				check = this.getByKey(SmartLock.IMEI_CODE, rowSmartLock.getImeiCode());
				if (check != null) {// IMEI码存在
					resultList.add(new ExcelReaderError(1, rowNum - 1, "IMEI码重复"));
					continue;
				}
				check = this.getByKey(SmartLock.MAC_ADDRESS, rowSmartLock.getMacAddress());
				if (check != null) {// MAC地址存在
					resultList.add(new ExcelReaderError(2, rowNum - 1, "MAC地址重复"));
					continue;
				}
				check = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, rowSmartLock.getBikePlateNumber());
				if (check != null) {// 单车车牌号存在
					resultList.add(new ExcelReaderError(3, rowNum - 1, "单车车牌号重复"));
					continue;
				}
			}
			
			String smartLockStatCode = null;
			switch (rowSmartLock.getSmartLockStatCode()){
			case "未生产":
				smartLockStatCode = SmartLockStatCodeEnum.UNPRODUCE.toCode();
				break;
			case "已生产":
				smartLockStatCode = SmartLockStatCodeEnum.PRODUCED.toCode();
				break;
			case "已发放":
				smartLockStatCode = SmartLockStatCodeEnum.GRANTED.toCode();
				break;
			case "已激活":
				smartLockStatCode = SmartLockStatCodeEnum.ACTIVED.toCode();
				break;
			case "维修中":
				smartLockStatCode = SmartLockStatCodeEnum.TO_FIX.toCode();
				break;
			case "已废弃":
				smartLockStatCode = SmartLockStatCodeEnum.DESERTED.toCode();
				break;
			default:
				smartLockStatCode = SmartLockStatCodeEnum.UNPRODUCE.toCode();
				break;	
			}

			//运营商转换
			String org = null;
			switch (rowSmartLock.getOrgId().toString()){
			case "总平台":
				org = "1";
				break;
			case "摩拜单车":
				org = "501";
				break;
			case "ofo小黄单":
				org = "502";
				break;
			case "小蓝车":
				org = "503";
				break;
			case "Hello":
				org = "504";
				break;
			case "小鸣":
				org = "505";
				break;
			default:
				org = "1";
				break;	
			}
			
			//单车类型转换
			String bikeType = null;
			switch (rowSmartLock.getBikeTypeId().toString()){
			case "标准单车":
				bikeType = "60000";
				break;
			case "摩拜 lite":
				bikeType = "60001";
				break;
			case "摩拜  小橙车":
				bikeType = "60002";
				break;
			case "ofo lite":
				bikeType = "60011";
				break;
			case "ofo 经典":
				bikeType = "60012";
				break;
			case "小蓝 lite":
				bikeType = "60021";
				break;
			case "小蓝 经典":
				bikeType = "60022";
				break;
			case "Hello Bike lite":
				bikeType = "60031";
				break;
			case "Hello Bike 经典":
				bikeType = "60032";
				break;
			case "小鸣 lite":
				bikeType = "60041";
				break;
			case "小鸣 经典":
				bikeType = "60042";
				break;
			default:
				bikeType = "1";
				break;	
			}
			
			// 保存
			SmartLock dbSmartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, rowSmartLock.getLockerHardwareCode());
			if (dbSmartLock == null) {//add
				dbSmartLock = new SmartLock();
				dbSmartLock.setOrgId(1);
				dbSmartLock.setSmartLockStatCode("0");
				dbSmartLock.setBikeTypeId(60000);
			}else{//update
				dbSmartLock.setOrgId(Integer.parseInt(org));
				dbSmartLock.setSmartLockStatCode(smartLockStatCode);
				dbSmartLock.setBikeTypeId(Integer.parseInt(bikeType));
			}
			dbSmartLock.setLockerHardwareCode(rowSmartLock.getLockerHardwareCode());
			dbSmartLock.setImeiCode(rowSmartLock.getImeiCode());
			dbSmartLock.setMacAddress(rowSmartLock.getMacAddress());
			dbSmartLock.setBikePlateNumber(rowSmartLock.getBikePlateNumber());
			
			SpringContextHelper.getBean(this.getClass()).save(dbSmartLock);
			getRepository().flush(); // 临时刷新到数据库，用于判断重复
		}

		return resultList;
	}

	@Override
	public void doImport(String filePath, Map<String, Object> importParam) {
		ListExcelReader<SmartLock> listExcelReader = new ListExcelReader<>(WebContextHolder.getWarPath()
				+ File.separator + Global.EXCEL_TEMPLATE_DIR + File.separator + SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME,
				SmartLock.class);
		listExcelReader.readFromFile(filePath, SpringContextHelper.getBean(SmartLockService.class), importParam);
	}

	@Override
	public Map<String, Double> getImportStatusMap() {
		return importStatusMap;
	}

	/**
	 * 智能锁导出
	 * 
	 * @param tempFilePath
	 * @return
	 * @throws IOException
	 */
	public String doExport(Integer orgId) throws IOException {
		// 生成一个EXCEL导入文件到TEMP,并且文件名用UUID
		String filePathString = WebContextHolder.getWarPath() + "/exceltemplate/smartLockListExcel.xls";// "war/exceltemplate/goodsInfListExcel.xls";

		ListExcelWriter writer = new ListExcelWriter(filePathString); // GWT.getHostPageBaseURL()+
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
        // 区分总平台跟运营商的导出数据
		List<SmartLock> smartLocks = null;
		if (Global.PLATFORM_ORG_ID != orgId) {
			smartLocks = SpringContextHelper.getBean(SmartLockSearchService.class).findByKey(SmartLock.ORG_ID, orgId);
		} else {
			smartLocks = getRepository().findAll();
		}
		
		for (SmartLock smartLock : smartLocks) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("smartLockId", smartLock.getSmartLockId());
			data.put("imeiCode", smartLock.getImeiCode());
			data.put("macAddress", smartLock.getMacAddress());
			data.put("lockerHardwareCode", smartLock.getLockerHardwareCode());
			data.put("bikePlateNumber", smartLock.getBikePlateNumber());
			data.put("activeDateStr", smartLock.getActiveDateStr());

			Agent org = SpringContextHelper.getBean(AgentService.class).getById(smartLock.getOrgId());
			if (org != null) {
				data.put("orgId", org.getAgentNm());
			}

			BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
			if (bikeType != null) {
				data.put("bikeTypeId", bikeType.getBikeTypeNm());
			}
			switch (SmartLockStatCodeEnum.fromCode(smartLock.getSmartLockStatCode())) {
			case UNPRODUCE:
				data.put("smartLockStatCode", "未生产");
				break;
			case PRODUCED:
				data.put("smartLockStatCode", "已生产");
				break;
			case GRANTED:
				data.put("smartLockStatCode", "已发放");
				break;
			case ACTIVED:
				data.put("smartLockStatCode", "已激活");
				break;
			case TO_FIX:
				data.put("smartLockStatCode", "维修中");
				break;
			case DESERTED:
				data.put("smartLockStatCode", "已废弃");
				break;
			default:
				data.put("smartLockStatCode", "NULL");
				break;
			}
			data.put("lastUseDateStr", smartLock.getLastUseDateStr());
			dataList.add(data);
		}
		String fileName = "temp/" + UUID.randomUUID() + ".xls";
		String outFileName = WebContextHolder.getWarPath() + "/" + fileName;
		writer.fillToFile(dataList, outFileName);

		return JsonBinder.buildNormalBinder().toJson(fileName);
	}
	
	public void doDesert(SmartLockRowDto selectedRow){
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(selectedRow.getSmartLockId());
		smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.DESERTED.toCode());
		SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
	}
}
