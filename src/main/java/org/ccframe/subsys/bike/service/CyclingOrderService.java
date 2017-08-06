package org.ccframe.subsys.bike.service;

import java.io.File;
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
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.sdk.bike.utils.DateDistanceUtil;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.sdk.bike.websocket.WebsocketEndPoint;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStateZhCodeEnum;
import org.ccframe.subsys.bike.domain.code.LockSwitchStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.ccframe.subsys.bike.repository.CyclingOrderRepository;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.MemberAccountLogService;
import org.ccframe.subsys.core.service.MemberAccountSearchService;
import org.ccframe.subsys.core.service.MemberAccountService;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.ccframe.subsys.core.service.UserSearchService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

@Service
public class CyclingOrderService extends BaseService<CyclingOrder,java.lang.Integer, CyclingOrderRepository>{
	
	/**
	 * 获取需要强制结束的骑行订单 
	 * @param cyclingOrderId
	 * @return
	 */
	@Transactional(readOnly=true)
	public CyclingOrderRowDto finishGetById(Integer cyclingOrderId){
		
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(cyclingOrderId);
		CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
		BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
		
		User user = SpringContextHelper.getBean(UserSearchService.class).getById(cyclingOrder.getUserId());
		Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(cyclingOrder.getOrgId());
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
		BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(smartLock.getBikeTypeId());
		cyclingOrderRowDto.setOrgNm(org.getOrgNm());
		cyclingOrderRowDto.setLoginId(user.getLoginId());
		cyclingOrderRowDto.setHardwareCodeStr(String.format(Global.FORMAT_HARDWARECODE, smartLock.getHardwareCode()));
		cyclingOrderRowDto.setBikeTypeNm(bikeType.getBikeTypeNm());
		
		return cyclingOrderRowDto;
	}

	/**
	 * 返回骑行轨迹窗口的需要数据
	 * @param cyclingOrderId
	 * @return
	 */
	@Transactional(readOnly=true)
	public CyclingOrderRowDto getCyclingOrderDtoById(Integer cyclingOrderId) {
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(cyclingOrderId);
		CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
		
		BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
		
		User user = SpringContextHelper.getBean(UserSearchService.class).getById(cyclingOrder.getUserId());
		cyclingOrderRowDto.setUserNm(user == null ? null : user.getUserNm());
		if (cyclingOrder.getStartTime() != null && cyclingOrder.getEndTime() != null) {
			long time = cyclingOrder.getStartTime().getTime() - cyclingOrder.getEndTime().getTime();
			String timeStr = UtilDateTimeClient.convertDateTimeToString(new Date(time)).substring(11, 19);
			timeStr = timeStr.substring(0, 2) + Global.HOUR + timeStr.substring(3, 5) + Global.MINUTE + timeStr.substring(6, 8) + Global.SECOND;
			cyclingOrderRowDto.setContinueTimeStr(timeStr);
		}
		if (cyclingOrder.getCyclingDistanceMeter() != null) {
			cyclingOrderRowDto.setCyclingDistanceMeterStr(BigDecimalUtil.divide(cyclingOrder.getCyclingDistanceMeter(), 1000) + Global.KM);
		}
		return cyclingOrderRowDto;
	}
	
	/**
	 * EXCEL导出
	 * @param orgId
	 * @return
	 * @throws IOException
	 */
	@Transactional(readOnly=true)
	public String doExport(Integer orgId) throws IOException {
		//生成一个EXCEL导入文件到TEMP,并且文件名用UUID
		String filePathString = WebContextHolder.getWarPath() + File.separator + Global.EXCEL_EXPORT_TEMPLATE_DIR + File.separator + Global.EXCEL_EXPORT_CYCLING_ORDER;//"war/exceltemplate/goodsInfListExcel.xls";
        
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
			data.put(CyclingOrder.CYCLING_ORDER_ID, cyclingOrder.getCyclingOrderId());
			
			User user = SpringContextHelper.getBean(UserSearchService.class).getById(cyclingOrder.getUserId());
			if (user != null) data.put(User.LOGIN_ID, user.getLoginId());
			
			Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(cyclingOrder.getOrgId());
			if (org != null) data.put(Org.ORG_NM, org.getOrgNm());
			
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
			if (smartLock != null) data.put(SmartLock.HARDWARE_CODE, smartLock.getHardwareCode());
			
			data.put(CyclingOrder.BIKE_PLATE_NUMBER, cyclingOrder.getBikePlateNumber());
			data.put(CyclingOrder.START_TIME_STR, cyclingOrder.getStartTimeStr());
			 
			String startLngLat = lngLatFormat(cyclingOrder.getStartLocationLng(), cyclingOrder.getStartLocationLat());
			data.put(CyclingOrder.START_LOCATION_LNG, startLngLat);

			data.put(CyclingOrder.END_TIME_STR, cyclingOrder.getEndTimeStr());
			
			String endLngLat = lngLatFormat(cyclingOrder.getEndLocationLng(), cyclingOrder.getEndLocationLat());
			data.put(CyclingOrder.END_LOCATION_LNG, endLngLat);

			switch (CyclingOrderStatCodeEnum.fromCode(cyclingOrder.getCyclingOrderStatCode())) {
			case ON_THE_WAY:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "骑行中");
				break;
			case CYCLING_FINISH:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "骑行完成");
				break;
			case PAY_FINISH:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "支付完成");
				break;
			case TO_BE_REPAIRED:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "已报修");
				break;
			case TEMPORARY_LOCKING:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "临时锁定");
				break;
			default:
				data.put(CyclingOrder.CYCLING_ORDER_STAT_CODE, "NULL");
				break;
			}
			data.put(CyclingOrder.CYCLING_CONTINOUS_SEC, timeFormat(cyclingOrder.getCyclingContinousSec()));
			data.put(CyclingOrder.CYCLING_DISTANCE_METER, BigDecimalUtil.divide(cyclingOrder.getCyclingDistanceMeter(), 1000));
			data.put(CyclingOrder.ORDER_AMMOUNT, cyclingOrder.getOrderAmmount());
			dataList.add(data);
		}
		String fileName = UUID.randomUUID() + Global.EXCEL_EXPORT_POSTFIX;
     	String outFileName = WebContextHolder.getWarPath() + File.separator + Global.TEMP_DIR + File.separator + fileName;
        writer.fillToFile(dataList, outFileName);
     	
        // URL请求路径
		return JsonBinder.buildNormalBinder().toJson(Global.TEMP_DIR + "/" + fileName);
	}
	
	private String lngLatFormat(Double lng, Double lat) {
		String lngStr = lng >= 0 ? (Global.EAST + lng) : (Global.WEST + lng);
		String latStr = lat >= 0 ? (Global.NORTH + lat) : (Global.SOUTH + lat);
		return lngStr + Global.COMMA + latStr;
	}
	
	private String timeFormat(Integer time){
		return String.format("%02d:%02d:%02d", time/60/60, time/60%60, time%60);
	}
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public List<CyclingOrder> getPayDetail() {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByEndTimeDesc(user.getUserId(), user.getOrgId());
		List<CyclingOrder> list2 = null;
		if(list != null && list.size()>0){
			list2 = new ArrayList<CyclingOrder>();
			for(CyclingOrder cyclingOrder : list){
				String code = cyclingOrder.getCyclingOrderStatCode();
				if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
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
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
				.findByUserIdAndOrgIdOrderByEndTimeDesc(user.getUserId(), user.getOrgId());

		List<CyclingOrder> list2 = null;
		if(list != null && list.size()>0){
			list2 = new ArrayList<CyclingOrder>();
			for(CyclingOrder cyclingOrder : list){
				String code = cyclingOrder.getCyclingOrderStatCode();
				if(code.equals(CyclingOrderStatCodeEnum.TO_BE_REPAIRED.toCode())){
					cyclingOrder.setCyclingOrderStatCode(CyclingOrderStateZhCodeEnum.ORDER_FIXED.toCode());
					
					BigDecimal result = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(60), MathContext.DECIMAL32);
					Double min = result.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					//用来保存骑行时长（分钟）
					cyclingOrder.setBikePlateNumber(min+"");
					list2.add(cyclingOrder);
				} else if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
					cyclingOrder.setCyclingOrderStatCode(CyclingOrderStateZhCodeEnum.ORDER_FINISHED.toCode());
					
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
	public AppPageDto getTravelDetail(Integer cyclingOrderId) {
		
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(cyclingOrderId);
		BigDecimal result = new BigDecimal(cyclingOrder.getCyclingDistanceMeter()).divide(new BigDecimal(1000), MathContext.DECIMAL32);
		Double km = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

		long min = cyclingOrder.getCyclingContinousSec() / 60;
		
		double burnCalories = this.countBurnCalories(cyclingOrder.getCyclingDistanceMeter());
		double reduceEmissions = burnCalories * 2.7;
		//不含计算，只是指定格式
		DecimalFormat df = new DecimalFormat("#0.00");  
		String orderAmmount = df.format(cyclingOrder.getOrderAmmount());
		
		String startPos = "["+cyclingOrder.getStartLocationLng()+","
				+cyclingOrder.getStartLocationLat()+"]";
		String endPos = "["+cyclingOrder.getEndLocationLng()+","
				+cyclingOrder.getEndLocationLat()+"]";
		String bikeNumber = cyclingOrder.getBikePlateNumber();
		
		StringBuffer polylinePath = PositionUtil.getPolylinePath(cyclingOrder.getCyclingOrderId());

		if(polylinePath.length()>=3) {
			polylinePath = polylinePath.insert(1, startPos+",");
			polylinePath = polylinePath.insert(polylinePath.length()-1, ","+endPos);
		}
		AppPageDto appPageDto = new AppPageDto();
		appPageDto.setList(polylinePath+"");
		appPageDto.setKm(km+"");
		appPageDto.setMin(min+"");
		appPageDto.setPayMoney(orderAmmount+"");
		appPageDto.setStartPos(startPos);
		appPageDto.setEndPos(endPos);
		appPageDto.setBikeNumber(bikeNumber);
		appPageDto.setBurnCalories(burnCalories+"");
		appPageDto.setReduceEmissions(reduceEmissions+"");

		return appPageDto;
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public AppPageDto getUsingBikeData() {
		MemberUser user = (MemberUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		
		AppPageDto appPageDto = new AppPageDto();
		if(list!=null && list.size()>0) {
			
			CyclingOrder cyclingOrder = list.get(0);
			long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), new Date());
			long min = sec / 60;
			System.out.println("开始时间" + cyclingOrder.getStartTime());
			System.out.println("已骑行" + min + "分钟");
			
			Integer count = (int) (min/30.0) + 1;
			//Integer count = (int) (31/30.0);
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(cyclingOrder.getBikeTypeId());
			
			Double halfHourAmmount = bikeType.getHalfhourAmmount();
			
			BigDecimal result = new BigDecimal(halfHourAmmount).multiply(new BigDecimal(count), MathContext.DECIMAL32);
			Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			//更新骑行距离
			/*Integer meter = this.countMeter(cyclingOrder.getCyclingOrderId());
			cyclingOrder.setCyclingDistanceMeter(meter);*/

			appPageDto.setMin(min+"");
			appPageDto.setBikeNumber(cyclingOrder.getBikePlateNumber());
			appPageDto.setPayMoney(payMoney+"");
			
		} 


		return appPageDto;
	}

	public Integer countMeter(Integer orderId) {
		List<CyclingTrajectoryRecord> list = SpringContextHelper.getBean(CyclingTrajectoryRecordSearchService.class).findByCyclingOrderIdOrderByRecordTimeAsc(orderId);
		double totalMeter = 0;
		if(list!=null && list.size()>0) {
			for(int i=0; i<list.size()-1; i++) {
				totalMeter += PositionUtil.getDistanceByLongNLat
						(list.get(i).getRecordLocationLng(), list.get(i).getRecordLocationLat(), list.get(i+1).getRecordLocationLng(), list.get(i+1).getRecordLocationLat());
			}
		}
		
		return (int)Math.round(totalMeter);
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public String closeLock(String paths) {
		MemberUser user = (MemberUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		
		if(list!=null && list.size()>0) {
			//更新骑行订单记录
			CyclingOrder cyclingOrder = list.get(0);
			
			String[] splits = PositionUtil.splitPos(paths);
			cyclingOrder.setEndLocationLng(Double.valueOf(splits[0]));
			cyclingOrder.setEndLocationLat(Double.valueOf(splits[1]));
			cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode());
			
			long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), new Date());
			long min = sec / 60;
			System.out.println("已骑行" + min + "分钟");
			
			Integer count = (int) (min/30.0) + 1;
			//Integer count = (int) (31/30.0);
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(cyclingOrder.getBikeTypeId());
			Double halfHourAmmount = bikeType.getHalfhourAmmount();
			BigDecimal result = new BigDecimal(halfHourAmmount).multiply(new BigDecimal(count), MathContext.DECIMAL32);
			Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			cyclingOrder.setCyclingContinousSec((int)sec);
			cyclingOrder.setOrderAmmount(payMoney);
			//更新骑行距离
			Integer meter = this.countMeter(cyclingOrder.getCyclingOrderId());
			cyclingOrder.setCyclingDistanceMeter(meter);
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		}
			
		return Global.SUCCESS;
	}
	
	

	/**
	 * @author zjm
	 */
	@Transactional
	public AppPageDto newCyclingOrder(String nowPos) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		AppPageDto appPageDto = new AppPageDto();
		if(list!=null && list.size()>0) {
			
				//用车中显示在前台时执行
				CyclingOrder cyclingOrder2 = list.get(0);
				
				long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder2.getStartTime(), new Date());
				long min = sec / 60;
				//System.out.println("已骑行" + min + "分钟");
				//写死 每半小时支付0.5元
				Integer count = (int) (min/30.0) + 1;
				//Integer count = (int) (31/30.0);
				BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(cyclingOrder2.getBikeTypeId());
				
				Double halfHourAmmount = bikeType.getHalfhourAmmount();
				BigDecimal result = new BigDecimal(halfHourAmmount).multiply(new BigDecimal(count), MathContext.DECIMAL32);
				Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				StringBuffer polylinePath = PositionUtil.getPolylinePath(cyclingOrder2.getCyclingOrderId());
				String firstPos = "";
				if(!polylinePath.toString().equals("")) {
					polylinePath = polylinePath.insert(polylinePath.length()-1, ",["+nowPos+"]");
					firstPos = polylinePath.substring(1, polylinePath.indexOf("]")+1);
				} else {
					polylinePath = new StringBuffer("[["+nowPos+"]]");
					firstPos = "["+nowPos+"]";
				}
				
				appPageDto.setMin(min+"");
				appPageDto.setBikeNumber(cyclingOrder2.getBikePlateNumber());
				appPageDto.setPayMoney(payMoney+"");
				appPageDto.setFirstPos(firstPos);
				appPageDto.setPolylinePath(polylinePath+"");
				
				//System.out.println("firstPos:"+firstPos+" polylinePath:"+polylinePath);
	
		}
		//生成骑行订单
		/*CyclingOrder cyclingOrder = new CyclingOrder();
		cyclingOrder.setSmartLockId(60001);
		cyclingOrder.setBikePlateNumber("SZCT00323424");
		cyclingOrder.setUserId(user.getUserId());
		cyclingOrder.setOrgId(1);
		Date nowDate = new Date();
		cyclingOrder.setStartTime(nowDate);
		String[] splits = null;
		if(nowPos!=null) {
			splits = PositionUtil.splitPos(nowPos);
			cyclingOrder.setStartLocationLng(Double.valueOf(splits[0]));
			cyclingOrder.setStartLocationLat(Double.valueOf(splits[1]));
		}
		
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode());
		cyclingOrder.setCyclingContinousSec(0);
		cyclingOrder.setCyclingDistanceMeter(0);
		cyclingOrder.setOrderAmmount(0.00);
		
		SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		
		//更新智能锁状态表记录
		List<SmartLockStat> statList = SpringContextHelper.getBean(SmartLockStatService.class).findByKey(SmartLockStat.SMART_LOCK_ID, cyclingOrder.getSmartLockId());
		SmartLockStat smartLockStat = statList.get(0);
		smartLockStat.setLockSwitchStatCode(LockSwitchStatCodeEnum.OPEN.toCode());
		smartLockStat.setLastLocationUpdTime(nowDate);
		if(splits != null) {
			smartLockStat.setLockLng(Double.valueOf(splits[0]));
			smartLockStat.setLockLat(Double.valueOf(splits[1]));
		}

		SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
		
		map.put("min", "0");
		map.put("bikeNumber", cyclingOrder.getBikePlateNumber());
		map.put("payMoney", "0.00");
		map.put("firstPos", "["+nowPos+"]");
		map.put("polylinePath", "[["+nowPos+"]]");*/
		return appPageDto;
	}

	/**
	 * @author lzh
	 */
	public AppPageDto getMenuData(MemberUser user){
		AppPageDto appPageDto = new AppPageDto();
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
			.findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		Integer cyclingDistace = 0;
		for (CyclingOrder cyclingOrder : list) { //TODO Fly 1.更改为数据库运算？ 2.米的问题
			cyclingDistace += cyclingOrder.getCyclingDistanceMeter();
		}
		appPageDto.setDistance(String.format("%.2f",cyclingDistace * 0.001));
		appPageDto.setAchievement(Double.toString(this.countBurnCalories(cyclingDistace)));
		appPageDto.setMoney(SpringContextHelper.getBean(MemberAccountSearchService.class).findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.PRE_DEPOSIT.toCode()).get(0).getAccountValue().toString());
		appPageDto.setRecord(Integer.toString(list.size()));
		appPageDto.setInvite("0");
		return appPageDto;
	}
	
	public double countBurnCalories(Integer distance) {
		return (distance / 16.0) * 413.27 / 1000.0;
	}
	
	/**
	 * 骑行
	 * @author lzh
	 */
	public String orderPay(Integer orderId, MemberUser user){
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(orderId);
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.PAY_FINISH.toCode());
		MemberAccount memberAccount = SpringContextHelper.getBean(MemberAccountSearchService.class).findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.PRE_DEPOSIT.toCode()).get(0);
		//构造并添加账户日志
//		MemberAccountLog memberAccountLog = SpringContextHelper.getBean(MemberAccountLog.class);
		MemberAccountLog memberAccountLog = new MemberAccountLog();
		
		memberAccountLog.setUserId(cyclingOrder.getUserId());
		memberAccountLog.setOrgId(cyclingOrder.getOrgId());
		memberAccountLog.setMemberAccountId(memberAccount.getMemberAccountId());
		memberAccountLog.setPrevValue(memberAccount.getAccountValue());
		memberAccountLog.setAfterValue(BigDecimalUtil.subtract(memberAccount.getAccountValue(), cyclingOrder.getOrderAmmount()));
		memberAccountLog.setChangeValue(cyclingOrder.getOrderAmmount());
		memberAccountLog.setSysTime(new Date());
		memberAccountLog.setReason("系统正常骑行扣费");
		memberAccountLog.setOperationManId(null);
		//设置用户数值
		memberAccount.setAccountValue(memberAccountLog.getAfterValue());
		//保存数据
		SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
		SpringContextHelper.getBean(MemberAccountLogService.class).save(memberAccountLog);
		
		return "success";
	}

	/**
	 * 关锁时调用
	 * @author zjm
	 */
	@Transactional
	public void updateCyclingOrderAndLockStat(Integer smartLockId, Double lng, Double lat, Integer lockBattery) {
		//更新骑行订单记录
		//List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
				//.findBySmartLockIdAndCyclingOrderStatCodeOrderByStartTimeDesc(smartLockId, CyclingOrderStatCodeEnum.ON_THE_WAY.toCode());
		//测试smartLockId为60001
		
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
				.findBySmartLockIdOrderByStartTimeDesc(smartLockId);
		if(list!=null && list.size()>0) {
			
			CyclingOrder cyclingOrder = list.get(0);
			cyclingOrder.setEndLocationLng(lng);
			cyclingOrder.setEndLocationLat(lat);
			cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode());
			Date nowDate = new Date();
			cyclingOrder.setEndTime(nowDate);
			long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), nowDate);
			long min = sec / 60;
			System.out.println("已骑行" + min + "分钟");
			Integer count = (int) (min/30.0) + 1;
			
			//获取半小时金额
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(smartLockId);
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getByKey(BikeType.BIKE_TYPE_ID, smartLock.getBikeTypeId());
			Double halfhourAmmount = bikeType.getHalfhourAmmount();
			BigDecimal result = new BigDecimal(halfhourAmmount).multiply(new BigDecimal(count), MathContext.DECIMAL32);
			Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			cyclingOrder.setCyclingContinousSec((int)sec);
			cyclingOrder.setOrderAmmount(payMoney);
			//更新骑行距离
			Integer totalMeter = this.countMeter(cyclingOrder.getCyclingOrderId());
			
			cyclingOrder.setCyclingDistanceMeter(totalMeter);
			SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
			

			updateSmartLockStat(smartLockId, lng, lat, lockBattery);
			
			//通知app跳转页面
			WebsocketEndPoint.sendMessageToUser(cyclingOrder.getUserId(),new TextMessage("success"));
		} else {
			//如果订单已经结束，那么将不再更新订单，但是需要更新锁状态
			updateSmartLockStat(smartLockId, lng, lat, lockBattery);
		}
		
	}
	/**
	 * 更新智能锁状态表
	 * @author lzh
	 */
	private void updateSmartLockStat(Integer smartLockId, Double lng, Double lat, Integer lockBattery) {
		//更新智能锁状态表记录
		SmartLockStat smartLockStat = SpringContextHelper.getBean(SmartLockStatService.class).getByKey(SmartLockStat.SMART_LOCK_ID, smartLockId);
		smartLockStat.setLockLng(lng);
		smartLockStat.setLockLat(lat);
		smartLockStat.setLockSwitchStatCode(LockSwitchStatCodeEnum.CLOCK.toCode());
		smartLockStat.setLastLocationUpdTime(new Date());
		smartLockStat.setLockBattery(lockBattery);
		
		SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
	} 
	
	/**
	 * 强制结束骑行订单
	 * @author yjz
	 */
	public Integer finish(CyclingOrderRowDto cyclingOrderRowDto){
		CyclingOrder cyclingOrder = new CyclingOrder();
		BeanUtils.copyProperties(cyclingOrderRowDto, cyclingOrder);
		cyclingOrder.setCyclingOrderStatCode(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode());
		
		//获取骑行轨迹最后一个打点
		List<CyclingTrajectoryRecord> cyclingTrajectoryRecordList = SpringContextHelper.getBean(CyclingTrajectoryRecordSearchService.class).findByCyclingOrderIdOrderByRecordTimeDesc(cyclingOrder.getCyclingOrderId());
		if(cyclingTrajectoryRecordList.size() > 0){
			Double endLng = cyclingTrajectoryRecordList.get(0).getRecordLocationLng();
			Double endLat = cyclingTrajectoryRecordList.get(0).getRecordLocationLat();
			cyclingOrder.setEndLocationLng(endLng);
			cyclingOrder.setEndLocationLat(endLat);
		}else{
			cyclingOrder.setEndLocationLng(cyclingOrderRowDto.getStartLocationLng());
			cyclingOrder.setEndLocationLat(cyclingOrderRowDto.getStartLocationLat());
		}
		
		//更新骑行订单
		SpringContextHelper.getBean(CyclingOrderService.class).save(cyclingOrder);
		//通知app跳转页面
		WebsocketEndPoint.sendMessageToUser(cyclingOrder.getUserId(),new TextMessage("success"));
		
		return cyclingOrder.getCyclingOrderId();
	}

	public String lunxun() {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
	    if(list!=null && list.size()>0 && list.get(0).getCyclingOrderStatCode()
	    		.equals(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode())) {
	    	return "success";
	    }
		//System.out.println("轮询返回：fail");
		return "fail";
	}
}
