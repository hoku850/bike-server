package org.ccframe.subsys.bike.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ccframe.client.Global;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.ccframe.subsys.bike.repository.CyclingOrderRepository;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CyclingOrderService extends BaseService<CyclingOrder,java.lang.Integer, CyclingOrderRepository>{

	@Transactional
	public void softDeleteById(Integer cyclingOrderId) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void saveOrUpdateCyclingOrder(CyclingOrder cyclingOrder) {
		// TODO Auto-generated method stub
		
	}

	public CyclingOrderRowDto getDtoById(Integer cyclingOrderId) {
		CyclingOrder cyclingOrder = getById(cyclingOrderId);
		CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
		BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
		
		User user = SpringContextHelper.getBean(UserService.class).getById(cyclingOrder.getUserId());
		cyclingOrderRowDto.setUserNm(user == null ? null : user.getUserNm());
		if (cyclingOrder.getStartTime() != null && cyclingOrder.getEndTime() != null) {
			long time = cyclingOrder.getStartTime().getTime() - cyclingOrder.getEndTime().getTime();
			String timeStr = UtilDateTimeClient.convertDateTimeToString(new Date(time)).substring(11, 19);
			String data = timeStr.substring(0, 2) + "时 " + timeStr.substring(3, 5) + "分 " + timeStr.substring(6, 8) + "秒";
			cyclingOrderRowDto.setContinueTimeStr(data);
		}
		if (cyclingOrder.getCyclingDistanceMeter() != null) {
			Double cyclingDistanceMeter =  (double)cyclingOrder.getCyclingDistanceMeter() / 1000;
			cyclingOrderRowDto.setCyclingDistanceMeterStr(cyclingDistanceMeter + " km");
		}
		return cyclingOrderRowDto;
	}
	
	public String doExport(String tempFilePath) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
    	String filePathString = WebContextHolder.getWarPath()+"/exceltemplate/cyclingOrderListExcel.xls";//"war/exceltemplate/goodsInfListExcel.xls";
        
    	ListExcelWriter writer = new ListExcelWriter(filePathString);   //GWT.getHostPageBaseURL()+     
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<CyclingOrder> cyclingOrders = getRepository().findAll();
		for (CyclingOrder cyclingOrder : cyclingOrders) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("cyclingOrderId", cyclingOrder.getCyclingOrderId());
			data.put("userId", cyclingOrder.getUserId());
			
			Agent org = SpringContextHelper.getBean(AgentService.class).getById(cyclingOrder.getOrgId());
			if (org != null) {
				data.put("orgId", org.getAgentNm());
			}
			data.put("smartLockId", cyclingOrder.getSmartLockId());
			data.put("bikePlateNumber", cyclingOrder.getBikePlateNumber());
			data.put("startTimeStr", cyclingOrder.getStartTimeStr());
			data.put("startLocationLng", cyclingOrder.getStartLocationLng());
			data.put("startLocationLat", cyclingOrder.getStartLocationLat());
			data.put("endTimeStr", cyclingOrder.getEndTimeStr());
			data.put("endLocationLng", cyclingOrder.getEndLocationLng());
			data.put("endLocationLat", cyclingOrder.getEndLocationLat());
			switch (CyclingOrderStatCodeEnum.fromCode(cyclingOrder.getCyclingOrderStatCode())) {
			case ON_THE_WAY:
				data.put("cyclingOrderStatCode", "骑行中");
				break;
			case CYCLING_FINISH:
				data.put("cyclingOrderStatCode", "骑行完成");
				break;
			case PAY_FINISH:
				data.put("cyclingOrderStatCode", "支付完成");
				break;
			case TO_BE_REPAIRED:
				data.put("cyclingOrderStatCode", "已报修");
				break;
			case TEMPORARY_LOCKING:
				data.put("cyclingOrderStatCode", "临时锁定");
				break;
			default:
				data.put("cyclingOrderStatCode", "NULL");
				break;
			}
			data.put("cyclingContinousSec", cyclingOrder.getCyclingContinousSec());
			data.put("cyclingDistanceMeter", cyclingOrder.getCyclingDistanceMeter());
			data.put("orderAmmount", cyclingOrder.getOrderAmmount());
			dataList.add(data);
		}
		String fileName =  "temp/" + UUID.randomUUID() + ".xls";
     	String outFileName = WebContextHolder.getWarPath() +"/"+ fileName;
        writer.fillToFile(dataList, outFileName);
     	
		return JsonBinder.buildNormalBinder().toJson(fileName);
	}
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public List<CyclingOrder> getPayDetail() {
		
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderService.class).findByKey(CyclingOrder.USER_ID, 1, new Order(Direction.DESC,CyclingOrder.END_TIME));
		List<CyclingOrder> list2 = null;
		if(list != null && list.size()>0){
			list2 = new ArrayList<CyclingOrder>();
			for(CyclingOrder cyclingOrder : list){
				String code = cyclingOrder.getCyclingOrderStatCode();
				if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
					cyclingOrder.setCyclingOrderStatCode("行程消费");
					list2.add(cyclingOrder);
				}
			}
		}
		
		return list2;
	}
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public List<CyclingOrder> getTravelList() {
		User user = (User) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
				.findByUserIdAndOrgIdOrderByEndTimeDesc(user.getUserId(), 1);

		List<CyclingOrder> list2 = null;
		if(list != null && list.size()>0){
			list2 = new ArrayList<CyclingOrder>();
			for(CyclingOrder cyclingOrder : list){
				String code = cyclingOrder.getCyclingOrderStatCode();
				if(code.equals(CyclingOrderStatCodeEnum.TO_BE_REPAIRED.toCode())){
					cyclingOrder.setCyclingOrderStatCode("已报修");
					
					BigDecimal result = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(60), MathContext.DECIMAL32);
					Double min = result.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
					//用来保存骑行时长（分钟）
					cyclingOrder.setBikePlateNumber(min+"");
					list2.add(cyclingOrder);
				} else if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
					cyclingOrder.setCyclingOrderStatCode("已完成");
					
					BigDecimal result = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(60), MathContext.DECIMAL32);
					Double min = result.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
					//用来保存骑行时长（分钟）
					cyclingOrder.setBikePlateNumber(min+"");
					list2.add(cyclingOrder);
				}
			}
		}
		
		return list2;
	}

}
