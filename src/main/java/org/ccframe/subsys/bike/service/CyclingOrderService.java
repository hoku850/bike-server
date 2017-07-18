package org.ccframe.subsys.bike.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ccframe.client.Global;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.DateDistanceUtil;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
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

	// 返回骑行轨迹的窗口
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
	
	public String doExport(Integer orgId) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
    	String filePathString = WebContextHolder.getWarPath()+"/exceltemplate/cyclingOrderListExcel.xls";//"war/exceltemplate/goodsInfListExcel.xls";
        
    	ListExcelWriter writer = new ListExcelWriter(filePathString);   //GWT.getHostPageBaseURL()+     
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        // 区分总平台跟运营商的导出数据
    	List<CyclingOrder> cyclingOrders = null;
		if (Global.PLATFORM_ORG_ID != orgId) {
			cyclingOrders = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByKey(CyclingOrder.ORG_ID, orgId);
		} else {
			cyclingOrders = getRepository().findAll();
		}
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
					Double min = result.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
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

	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public Map<String, Object> getTravelDetail(Integer cyclingOrderId) {
		
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderService.class).getById(cyclingOrderId);
		BigDecimal result = new BigDecimal(cyclingOrder.getCyclingDistanceMeter()).divide(new BigDecimal(1000), MathContext.DECIMAL32);
		Double km = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal result2 = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(60), MathContext.DECIMAL32);
		Double min = result2.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		DecimalFormat df = new DecimalFormat("#0.00");  
		String orderAmmount = df.format(cyclingOrder.getOrderAmmount());
		String startPos = "["+cyclingOrder.getStartLocationLng()+","
				+cyclingOrder.getStartLocationLat()+"]";
		String endPos = "["+cyclingOrder.getEndLocationLng()+","
				+cyclingOrder.getEndLocationLat()+"]";
		String bikeNumber = cyclingOrder.getBikePlateNumber();
		
		
		List<CyclingTrajectoryRecord> list = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, cyclingOrderId,
				new Order(Direction.ASC, CyclingTrajectoryRecord.RECORD_TIME));
		
		List<String> posList = new ArrayList<String>();
		if(list!=null && list.size()>0) {
			String posString = "";
			for(CyclingTrajectoryRecord record : list) {
				posString = "["+record.getRecordLocationLng()+","+record.getRecordLocationLat()+"]";
				
				posList.add(posString);
			}
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", posList);
		map.put("km", km+"");
		map.put("min", min+"");
		map.put("payMoney", orderAmmount+"");
		map.put("startPos", startPos);
		map.put("endPos", endPos);
		map.put("bikeNumber", bikeNumber);

		return map;
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public Map<String, Object> getUsingBikeData(String startPos) {
		
		Date startDate = (Date) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_START_CYCLING_TIME);
		Date nowDate = null;
		System.out.println("session中的startDate：" + startDate);
		if(startDate == null) {
			startDate = new Date();
			WebContextHolder.getSessionContextStore().setServerValue(Global.SESSION_START_CYCLING_TIME, startDate);
			
			User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
			//生成骑行订单
			CyclingOrder cyclingOrder = new CyclingOrder();
			cyclingOrder.setSmartLockId(60001);
			cyclingOrder.setBikePlateNumber("60001");
			cyclingOrder.setUserId(user.getUserId());
			cyclingOrder.setOrgId(1);
			nowDate = new Date();
			cyclingOrder.setStartTime(nowDate);
			String[] splits = PositionUtil.splitPos(startPos);
			cyclingOrder.setStartLocationLng(Double.valueOf(splits[0]));
			cyclingOrder.setStartLocationLat(Double.valueOf(splits[1]));
			cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode());
			cyclingOrder.setCyclingContinousSec(0);
			cyclingOrder.setCyclingDistanceMeter(0);
			cyclingOrder.setOrderAmmount(0.00);
			
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		} else {
			nowDate = new Date();
		}
		
		
		System.out.println("当前时间：" + nowDate);
		long[] time = DateDistanceUtil.getDistanceTimes(startDate, nowDate);
		System.out.println("已骑行" + time[2] + "分钟");
		//写死 每半小时支付0.5元
		Integer count = (int) (time[2]/30.0);
		//Integer count = (int) (31/30.0);
		BigDecimal result = new BigDecimal(0.5).multiply(new BigDecimal(count), MathContext.DECIMAL32);
		Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("min", time[2]+"");
		map.put("bikeNumber", "SZCT00323424");
		map.put("payMoney", payMoney+"");

		return map;
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public String closeLock(String paths) {
		User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		//更新骑行订单记录
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), 1);
		CyclingOrder cyclingOrder = list.get(0);
		cyclingOrder.setEndTime(new Date());
		String[] splits = PositionUtil.splitPos(paths);
		cyclingOrder.setEndLocationLng(Double.valueOf(splits[0]));
		cyclingOrder.setEndLocationLat(Double.valueOf(splits[1]));
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode());
		//-----------未完待续
		
		return null;
	}



}
