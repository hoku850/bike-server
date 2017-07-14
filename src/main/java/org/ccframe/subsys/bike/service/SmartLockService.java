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
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.BatchImportSupport;
import org.ccframe.commons.data.ExcelReaderError;
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
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.ccframe.subsys.bike.repository.SmartLockRepository;
import org.ccframe.subsys.bike.search.SmartLockSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockService extends BaseService<SmartLock,java.lang.Integer, SmartLockRepository> implements BatchImportSupport<SmartLock>{
	
	private static final String SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME = "smartLockImport.xls";

	private Map<String, Double> importStatusMap = new ConcurrentHashMap<String, Double>();
	
	@Transactional
	public void saveOrUpdateSmartLock(SmartLock smartLock) {
		if(smartLock.getOrgId()==0 || smartLock.getBikeTypeId()==0){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"运营商或单车类型没选择！！！"});
		}else{
			SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
		}
	}
	
	public Integer countSmartLockOfAgent(Integer agentId) {
        List<SmartLock> list = this.getRepository().findAll(new Criteria<SmartLock>().add(Restrictions.eq(SmartLock.ORG_ID, agentId)));
        if (list!=null) {
            return list.size();
        }else {
            return 0;
        }
    }
	
	@Transactional
	public void myDeleteById(Integer smartLockId) {
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(smartLockId);
		
		if(SpringContextHelper.getBean(CyclingOrderService.class).getById(smartLock.getBikeTypeId())==null){
			deleteById(smartLockId);
		}else{
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"骑行订单表中使用到该记录，暂时不能删除！！！"});
		}
	}
	
	private List<ExcelReaderError> dataLogicCheck(SmartLock checkValue,int rowNum){
		List<ExcelReaderError> resultList = new ArrayList<>();
//		ImportDataCheckUtil.stringCheck("IMEI码", 15, false, checkValue.getImeiCode(), rowNum, 0, resultList);
//		ImportDataCheckUtil.stringCheck("MAC地址", 17, false, checkValue.getMacAddress(), rowNum, 1, resultList);
//		ImportDataCheckUtil.stringCheck("硬件编码", 32, false, checkValue.getLockerHardwareCode(), rowNum, 2, resultList);
//		ImportDataCheckUtil.stringCheck("单车车牌号", 15, false, checkValue.getBikePlateNumber(), rowNum, 3, resultList);
//		ImportDataCheckUtil.stringCheck("机构ID", 32, false, checkValue.getOrgId(), rowNum, 4, resultList);
//		ImportDataCheckUtil.stringCheck("单车类型", 32, false, checkValue.getBikeTypeId(), rowNum, 5, resultList);
//		ImportDataCheckUtil.enumCheck("智能锁状态", "未生产,已生产,已发放,已激活,维修中,已废弃", checkValue.getSmartLockStatCode(), rowNum, 6, resultList);
//		ImportDataCheckUtil.enumCheck("智能锁状态", "未生产,已生产,已发放,已激活,维修中,已废弃", checkValue.getSmartLockStatCode(), rowNum, 6, resultList);
		return resultList;
	}
	
	@Override
	public List<ExcelReaderError> importBatch(int rowBase, List<SmartLock> importList, boolean isLastRow,
			Map<String, Object> importParam) {
		List<ExcelReaderError> resultList = new ArrayList<ExcelReaderError>();
		int rowNum = rowBase;
		for(SmartLock rowUser: importList){
			List<ExcelReaderError> checkErrorList = dataLogicCheck(rowUser, rowNum);
			rowNum ++;
			if(checkErrorList.size() > 0){ //有错误，不处理
				resultList.addAll(checkErrorList);
				continue;
			}
		}
			
//			User loginUser = this.getByKey(User.LOGIN_ID, rowUser.getLoginId());
//			User mobileUser = null;
//			if(StringUtils.isNotBlank(rowUser.getUserMobile())){
//				mobileUser = this.getByKey(User.USER_MOBILE, rowUser.getUserMobile());
//			}
//			User emailUser = null;
//			if(StringUtils.isNotBlank(rowUser.getUserEmail())){
//				emailUser = this.getByKey(User.USER_EMAIL, rowUser.getUserEmail());
//			}
//			if(loginUser != null){ //add
//				if(mobileUser != null){
//					resultList.add(new ExcelReaderError(2, rowNum - 1, "用户手机重复"));
//					continue;
//				}
//				if(emailUser != null){
//					resultList.add(new ExcelReaderError(3, rowNum - 1, "用户E-MAIL重复"));
//					continue;
//				}
//			}else{ //update
//				if(mobileUser != null && !mobileUser.equals(loginUser)){
//					resultList.add(new ExcelReaderError(2, rowNum - 1, "用户手机重复"));
//					continue;
//				}
//				if(emailUser != null && !mobileUser.equals(emailUser)){
//					resultList.add(new ExcelReaderError(3, rowNum - 1, "用户E-MAIL重复"));
//					continue;
//				}
//			}
			
//			User dbUser = this.getByKey(User.LOGIN_ID, rowUser.getLoginId());
//			if(dbUser == null){ //add
//				dbUser = new User();
//				dbUser.setLoginId(rowUser.getLoginId());
//				dbUser.setUserPsw(DigestUtils.sha512Hex("123456"));
//				dbUser.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
//			}
//			dbUser.setUserNm(rowUser.getUserNm());
//			dbUser.setUserMobile(rowUser.getUserMobile());
//			dbUser.setUserEmail(rowUser.getUserEmail());
//			dbUser.setIfAdmin(BoolCodeEnum.toCode("是".equals(rowUser.getIfAdmin())));
//			dbUser.setCreateDate(rowUser.getCreateDate());
//			SpringContextHelper.getBean(this.getClass()).save(dbUser);
//			getRepository().flush(); //临时刷新到数据库，用于判断重复
//		}
//		try {
//			Thread.sleep(2000); //TODO 去掉，延迟观察进度条
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	@Override
	public void doImport(String filePath, Map<String, Object> importParam) {
		ListExcelReader<SmartLock> listExcelReader= new ListExcelReader<>(WebContextHolder.getWarPath() + File.separator + Global.EXCEL_TEMPLATE_DIR + File.separator + SMART_LOCK_IMPORT_TEMPLATE_FILE_NAME, SmartLock.class);
		listExcelReader.readFromFile(filePath, SpringContextHelper.getBean(SmartLockService.class), importParam);
	}

	@Override
	public Map<String, Double> getImportStatusMap() {
		return importStatusMap;
	}
	
	/**
	 * 智能锁导出
	 * @param tempFilePath
	 * @return
	 * @throws IOException
	 */
	public String doExport(String tempFilePath) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
    	String filePathString = WebContextHolder.getWarPath()+"/exceltemplate/smartLockListExcel.xls";//"war/exceltemplate/goodsInfListExcel.xls";
        
    	ListExcelWriter writer = new ListExcelWriter(filePathString);   //GWT.getHostPageBaseURL()+     
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<SmartLock> smartLocks = getRepository().findAll();
		
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
		String fileName =  "temp/" + UUID.randomUUID() + ".xls";
     	String outFileName = WebContextHolder.getWarPath() +"/"+ fileName;
        writer.fillToFile(dataList, outFileName);
     	
		return JsonBinder.buildNormalBinder().toJson(fileName);
	}
	

}
