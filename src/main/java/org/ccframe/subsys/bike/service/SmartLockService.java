package org.ccframe.subsys.bike.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.BatchImportSupport;
import org.ccframe.commons.data.ExcelReaderError;
import org.ccframe.commons.data.ImportDataCheckUtil;
import org.ccframe.commons.data.ListExcelReader;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.ccframe.subsys.bike.repository.SmartLockRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockService extends BaseService<SmartLock, java.lang.Integer, SmartLockRepository> implements BatchImportSupport<SmartLockRowDto> {

	private static final String SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME = "smartLockImport.xls";

	private Map<String, Double> importStatusMap = new ConcurrentHashMap<String, Double>();

	@Transactional
	public void saveOrUpdateSmartLock(SmartLock smartLock) {
		if(smartLock.getSmartLockId()==null){
			smartLock.setActiveDate(null);
			smartLock.setLastUseDate(null);
		}
		
		SmartLock dbSmartLock = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, smartLock.getLockerHardwareCode());
		if(dbSmartLock != null){
			if(smartLock.getSmartLockId() == null || (smartLock.getSmartLockId() != null && (!dbSmartLock.getSmartLockId().equals(smartLock.getSmartLockId())))){
				throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "硬件编码重复！！！" });
			}
		}
		
		if(StringUtils.isNotBlank(smartLock.getImeiCode())){
			SmartLock dbSmartLock1 = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.IMEI_CODE, smartLock.getImeiCode());
			if(dbSmartLock1 != null){
				if(smartLock.getSmartLockId() == null || (smartLock.getSmartLockId() != null && (!dbSmartLock1.getSmartLockId().equals(smartLock.getSmartLockId())))){
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "IMEI码重复！！！" });
				}
			}
		}
		
		if(StringUtils.isNotBlank(smartLock.getMacAddress())){
			SmartLock dbSmartLock1 = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.MAC_ADDRESS, smartLock.getMacAddress());
			if(dbSmartLock1 != null){
				if(smartLock.getSmartLockId() == null || (smartLock.getSmartLockId() != null && (!dbSmartLock1.getSmartLockId().equals(smartLock.getSmartLockId())))){
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "MAC地址重复！！！" });
				}
			}
		}
		
		if(StringUtils.isNotBlank(smartLock.getBikePlateNumber())){
			SmartLock dbSmartLock1 = SpringContextHelper.getBean(SmartLockService.class).getByKey(SmartLock.BIKE_PLATE_NUMBER, smartLock.getBikePlateNumber());
			if(dbSmartLock1 != null){
				if(smartLock.getSmartLockId() == null || (smartLock.getSmartLockId() != null && (!dbSmartLock1.getSmartLockId().equals(smartLock.getSmartLockId())))){
					throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "单车车牌号重复！！！" });
				}
			}
		}
		
		SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
		
	}

//	@Transactional(readOnly=true)
//	public Integer countSmartLockOfAgent(Integer agentId) {
//		List<SmartLock> list = this.getRepository().findAll(new Criteria<SmartLock>().add(Restrictions.eq(SmartLock.ORG_ID, agentId)));
//		if (list != null) {
//			return list.size();
//		} else {
//			return 0;
//		}
//	}

	@Transactional
	public void decideDeleteById(Integer smartLockId) {
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(smartLockId);

		if (SpringContextHelper.getBean(CyclingOrderService.class).getById(smartLock.getBikeTypeId()) == null) {
			SpringContextHelper.getBean(SmartLockService.class).deleteById(smartLockId);
		} else {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[] { "骑行订单表中使用到该记录，暂时不能删除！！！" });
		}
	}

	private List<ExcelReaderError> dataLogicCheck(SmartLockRowDto checkValue, int rowNum) {
		List<ExcelReaderError> resultList = new ArrayList<>();
		ImportDataCheckUtil.stringCheck("硬件编码", 32, false, checkValue.getLockerHardwareCode(), rowNum, 0, resultList);
		ImportDataCheckUtil.stringCheck("IMEI码", 15, true, checkValue.getImeiCode(), rowNum, 1, resultList);
		ImportDataCheckUtil.stringCheck("MAC地址", 17, true, checkValue.getMacAddress(), rowNum, 2, resultList);
		ImportDataCheckUtil.stringCheck("单车车牌号", 15, true, checkValue.getBikePlateNumber(), rowNum, 3, resultList);
		ImportDataCheckUtil.stringCheck("运营商", 15, true, checkValue.getOrgNm(), rowNum, 4, resultList);
		ImportDataCheckUtil.enumCheck("状态", "未生产,已生产,已发放,已激活,维修中,已废弃", checkValue.getSmartLockStatCode(), rowNum, 5, resultList);
		ImportDataCheckUtil.stringCheck("单车类型", 15, true, checkValue.getBikeTypeNm(), rowNum, 6, resultList);
		return resultList;
	}

	@Transactional
	@Override
	public List<ExcelReaderError> importBatch(int rowBase, List<SmartLockRowDto> importList, boolean isLastRow,
			Map<String, Object> importParam) {
		List<ExcelReaderError> resultList = new ArrayList<ExcelReaderError>();
		int rowNum = rowBase;
		for (SmartLockRowDto rowSmartLock : importList) {
			List<ExcelReaderError> checkErrorList = dataLogicCheck(rowSmartLock, rowNum);
			rowNum++;
			if (checkErrorList.size() > 0) { // 有错误，不处理
				resultList.addAll(checkErrorList);
				continue;
			}

			SmartLock smartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, rowSmartLock.getLockerHardwareCode());//数据库内容

			// 判断重复
			SmartLock check = null;
			if (smartLock != null) {// 硬件编号存在,update
				if(rowSmartLock.getImeiCode() != null){
					check = this.getByKey(SmartLock.IMEI_CODE, rowSmartLock.getImeiCode());
					if (check != null && !check.getImeiCode().equals(smartLock.getImeiCode())) {// IMEI码存在,并且
						resultList.add(new ExcelReaderError(1, rowNum - 1, "IMEI码重复"));
						continue;
					}
				}else{
					rowSmartLock.setImeiCode(smartLock.getImeiCode());
				}
				
				if(rowSmartLock.getMacAddress() != null){
					check = this.getByKey(SmartLock.MAC_ADDRESS, rowSmartLock.getMacAddress());
					if (check != null && !check.getMacAddress().equals(smartLock.getMacAddress())) {// MAC地址存在,并且
						resultList.add(new ExcelReaderError(2, rowNum - 1, "MAC地址重复"));
						continue;
					}
				}else{
					rowSmartLock.setMacAddress(smartLock.getMacAddress());
				}
				
				if(rowSmartLock.getBikePlateNumber() != null){
					check = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, rowSmartLock.getBikePlateNumber());
					if (check != null && !check.getBikePlateNumber().equals(smartLock.getBikePlateNumber())) {// 单车车牌号存在，并且
						resultList.add(new ExcelReaderError(3, rowNum - 1, "单车车牌号重复"));
						continue;
					}
				}else{
					rowSmartLock.setBikePlateNumber(smartLock.getBikePlateNumber());
				}
			} else {// 硬件编码不存在,add
				if(rowSmartLock.getImeiCode() != null){
					check = this.getByKey(SmartLock.IMEI_CODE, rowSmartLock.getImeiCode());
					if (check != null) {// IMEI码存在
						resultList.add(new ExcelReaderError(1, rowNum - 1, "IMEI码重复"));
						continue;
					}
				}
				if(rowSmartLock.getMacAddress() != null){
					check = this.getByKey(SmartLock.MAC_ADDRESS, rowSmartLock.getMacAddress());
					if (check != null) {// MAC地址存在
						resultList.add(new ExcelReaderError(2, rowNum - 1, "MAC地址重复"));
						continue;
					}
				}
				if(rowSmartLock.getBikePlateNumber() != null){
					check = this.getByKey(SmartLock.BIKE_PLATE_NUMBER, rowSmartLock.getBikePlateNumber());
					if (check != null) {// 单车车牌号存在
						resultList.add(new ExcelReaderError(3, rowNum - 1, "单车车牌号重复"));
						continue;
					}
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
			int orgId = 1;
			
			Org org = SpringContextHelper.getBean(OrgService.class).getByKey(Org.ORG_NM, rowSmartLock.getOrgNm());
			if(org == null){
				resultList.add(new ExcelReaderError(4, rowNum - 1, "运营商不存在"));
				continue;
			}else{
				orgId = org.getOrgId();
			}
			
			//单车类型转换
			int bikeTypeId = 60000;
			List<BikeType> listBikeType = SpringContextHelper.getBean(BikeTypeService.class).findByKey(BikeType.BIKE_TYPE_NM, rowSmartLock.getBikeTypeNm());
			if(listBikeType.size()==0){
				resultList.add(new ExcelReaderError(6, rowNum - 1, "单车类型不存在"));
				continue;
			}else{
				if(rowSmartLock.getBikeTypeNm().equals("标准单车")){
					List<BikeType> listDbBikeType = SpringContextHelper.getBean(BikeTypeService.class).findByKey(BikeType.ORG_ID, orgId);
					for (BikeType bikeType2 : listBikeType) {
						if(orgId == bikeType2.getOrgId()){
							bikeTypeId = bikeType2.getBikeTypeId();
						}
					}
				}else{
					BikeType db2 = SpringContextHelper.getBean(BikeTypeService.class).getByKey(BikeType.BIKE_TYPE_NM, rowSmartLock.getBikeTypeNm());
					bikeTypeId = db2.getBikeTypeId();
				}
			}
			
			
			// 保存
			SmartLock dbSmartLock = this.getByKey(SmartLock.LOCKER_HARDWARE_CODE, rowSmartLock.getLockerHardwareCode());
			if (dbSmartLock == null) {//add
				dbSmartLock = new SmartLock();
			}
			dbSmartLock.setOrgId(orgId);
			dbSmartLock.setSmartLockStatCode(smartLockStatCode);
			dbSmartLock.setBikeTypeId(bikeTypeId);
			dbSmartLock.setLockerHardwareCode(rowSmartLock.getLockerHardwareCode());
			dbSmartLock.setImeiCode(rowSmartLock.getImeiCode());
			dbSmartLock.setMacAddress(rowSmartLock.getMacAddress());
			dbSmartLock.setBikePlateNumber(rowSmartLock.getBikePlateNumber());
			
			SpringContextHelper.getBean(this.getClass()).save(dbSmartLock);
			getRepository().flush(); // 临时刷新到数据库，用于判断重复
		}

		return resultList;
	}

	@Async
	@Override
	public void doImport(String filePath, Map<String, Object> importParam) {
		ListExcelReader<SmartLockRowDto> listExcelReader = new ListExcelReader<>(WebContextHolder.getWarPath()
				+ File.separator + Global.EXCEL_TEMPLATE_DIR + File.separator + SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME,
				SmartLockRowDto.class);
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
		String filePathString = WebContextHolder.getWarPath() + File.separator + Global.EXCEL_TEMPLATE_DIR + File.separator + "smartLockListExcel.xls";// "war/exceltemplate/goodsInfListExcel.xls";

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
			data.put(SmartLock.SMART_LOCK_ID, smartLock.getSmartLockId());
			data.put(SmartLock.IMEI_CODE, smartLock.getImeiCode());
			data.put(SmartLock.MAC_ADDRESS, smartLock.getMacAddress());
			data.put(SmartLock.LOCKER_HARDWARE_CODE, smartLock.getLockerHardwareCode());
			data.put(SmartLock.BIKE_PLATE_NUMBER, smartLock.getBikePlateNumber());
			data.put(SmartLock.ACTIVE_DATE_STR, smartLock.getActiveDateStr());

			Org org = SpringContextHelper.getBean(OrgService.class).getById(smartLock.getOrgId());
			if (org != null) {
				data.put(SmartLock.ORG_ID, org.getOrgNm());
			}

			BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
			if (bikeType != null) {
				data.put(SmartLock.BIKE_TYPE_ID, bikeType.getBikeTypeNm());
			}
			switch (SmartLockStatCodeEnum.fromCode(smartLock.getSmartLockStatCode())) {
			case UNPRODUCE:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "未生产");
				break;
			case PRODUCED:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "已生产");
				break;
			case GRANTED:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "已发放");
				break;
			case ACTIVED:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "已激活");
				break;
			case TO_FIX:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "维修中");
				break;
			case DESERTED:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "已废弃");
				break;
			default:
				data.put(SmartLock.SMART_LOCK_STAT_CODE, "NULL");
				break;
			}
			data.put(SmartLock.LAST_USE_DATE_STR, smartLock.getLastUseDateStr());
			dataList.add(data);
		}
		String fileName = Global.TEMP_DIR + "/" + UUID.randomUUID() + ".xls";
		String outFileName = WebContextHolder.getWarPath() + File.separator + fileName;
		writer.fillToFile(dataList, outFileName);

		return JsonBinder.buildNormalBinder().toJson(fileName);
	}
	
	public void doDesert(SmartLockRowDto selectedRow){
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(selectedRow.getSmartLockId());
		smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.DESERTED.toCode());
		SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
	}

}
