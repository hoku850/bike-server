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
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.ListExcelWriter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.commons.util.JsonBinder;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.sdk.bike.utils.DateDistanceUtil;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.sdk.bike.websocket.WebsocketEndPoint;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
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
import org.ccframe.subsys.core.service.OrgService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

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
	
	//获取需要强制结束的骑行订单 
	@Transactional(readOnly=true)
	public CyclingOrderRowDto finishGetById(Integer cyclingOrderId){
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderService.class).getById(cyclingOrderId);
		CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
		BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
		User user = SpringContextHelper.getBean(UserService.class).getById(cyclingOrder.getUserId());
		Org org = SpringContextHelper.getBean(OrgService.class).getById(cyclingOrder.getOrgId());
		SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId());
		BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
		cyclingOrderRowDto.setOrgNm(org.getOrgNm());
		cyclingOrderRowDto.setLoginId(user.getLoginId());
		cyclingOrderRowDto.setLockerHardwareCode(smartLock.getLockerHardwareCode());
		cyclingOrderRowDto.setBikeTypeNm(bikeType.getBikeTypeNm());
		
		return cyclingOrderRowDto;
	}

	// 返回骑行轨迹的窗口
	@Transactional(readOnly=true)
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
	
	@Transactional(readOnly=true)
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
			data.put(CyclingOrder.CYCLING_ORDER_ID, cyclingOrder.getCyclingOrderId());
			data.put(CyclingOrder.USER_ID, cyclingOrder.getUserId());
			
			Org org = SpringContextHelper.getBean(OrgService.class).getById(cyclingOrder.getOrgId());
			if (org != null) {
				data.put(CyclingOrder.ORG_ID, org.getOrgNm());
			}
			data.put(CyclingOrder.SMART_LOCK_ID, cyclingOrder.getSmartLockId());
			data.put(CyclingOrder.BIKE_PLATE_NUMBER, cyclingOrder.getBikePlateNumber());
			data.put(CyclingOrder.START_TIME_STR, cyclingOrder.getStartTimeStr());
			data.put(CyclingOrder.START_LOCATION_LNG, cyclingOrder.getStartLocationLng());
			data.put(CyclingOrder.START_LOCATION_LAT, cyclingOrder.getStartLocationLat());
			data.put(CyclingOrder.END_TIME_STR, cyclingOrder.getEndTimeStr());
			data.put(CyclingOrder.END_LOCATION_LNG, cyclingOrder.getEndLocationLng());
			data.put(CyclingOrder.END_LOCATION_LAT, cyclingOrder.getEndLocationLat());
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
			data.put(CyclingOrder.CYCLING_CONTINOUS_SEC, cyclingOrder.getCyclingContinousSec());
			data.put(CyclingOrder.CYCLING_DISTANCE_METER, cyclingOrder.getCyclingDistanceMeter());
			data.put(CyclingOrder.ORDER_AMMOUNT, cyclingOrder.getOrderAmmount());
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
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByEndTimeDesc(user.getUserId(), user.getOrgId());
		List<CyclingOrder> list2 = null;
		if(list != null && list.size()>0){
			list2 = new ArrayList<CyclingOrder>();
			for(CyclingOrder cyclingOrder : list){
				String code = cyclingOrder.getCyclingOrderStatCode();
				if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
					cyclingOrder.setCyclingOrderStatCode(AppConstant.Travel_Pay);
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
					cyclingOrder.setCyclingOrderStatCode(AppConstant.ORDER_FIXED);
					
					BigDecimal result = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(AppConstant.EVERY_MIN), MathContext.DECIMAL32);
					Double min = result.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
					//用来保存骑行时长（分钟）
					cyclingOrder.setBikePlateNumber(min+"");
					list2.add(cyclingOrder);
				} else if(code.equals(CyclingOrderStatCodeEnum.PAY_FINISH.toCode())){
					cyclingOrder.setCyclingOrderStatCode(AppConstant.ORDER_FINISHED);
					
					BigDecimal result = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(AppConstant.EVERY_MIN), MathContext.DECIMAL32);
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
		BigDecimal result = new BigDecimal(cyclingOrder.getCyclingDistanceMeter()).divide(new BigDecimal(AppConstant.EVERY_KM), MathContext.DECIMAL32);
		Double km = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal result2 = new BigDecimal(cyclingOrder.getCyclingContinousSec()).divide(new BigDecimal(AppConstant.EVERY_MIN), MathContext.DECIMAL32);
		Double min = result2.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
		
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
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AppConstant.LIST, polylinePath);
		map.put(AppConstant.KM, km+"");
		map.put(AppConstant.MIN, min+"");
		map.put(AppConstant.PAY_MONEY, orderAmmount+"");
		map.put(AppConstant.START_POS, startPos);
		map.put(AppConstant.END_POS, endPos);
		map.put(AppConstant.BIKE_NUMBER, bikeNumber);

		return map;
	}

	/**
	 * @author zjm
	 */
	@Transactional
	public Map<String, Object> getUsingBikeData() {
		MemberUser user = (MemberUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(list!=null && list.size()>0) {
			
			CyclingOrder cyclingOrder = list.get(0);
			long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder.getStartTime(), new Date());
			long min = sec / AppConstant.EVERY_MIN;
			System.out.println("开始时间" + cyclingOrder.getStartTime());
			System.out.println("已骑行" + min + "分钟");
			//写死 每半小时支付0.5元
			Integer count = (int) (min/AppConstant.EVERY_HALF_HOUR) + 1;
			//Integer count = (int) (31/30.0);
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(smartLock.getBikeTypeId());
			
			Double halfHourAmmount = bikeType.getHalfhourAmmount();
			
			BigDecimal result = new BigDecimal(halfHourAmmount).multiply(new BigDecimal(count), MathContext.DECIMAL32);
			Double payMoney = result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			//更新骑行距离
			/*Integer meter = this.countMeter(cyclingOrder.getCyclingOrderId());
			cyclingOrder.setCyclingDistanceMeter(meter);*/

			map.put(AppConstant.MIN, min+"");
			map.put(AppConstant.BIKE_NUMBER, cyclingOrder.getBikePlateNumber());
			map.put(AppConstant.PAY_MONEY, payMoney+""); 
		} 


		return map;
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
			long min = sec / AppConstant.EVERY_MIN;
			System.out.println("已骑行" + min + "分钟");
			//写死 每半小时支付0.5元
			Integer count = (int) (min/AppConstant.EVERY_HALF_HOUR) + 1;
			//Integer count = (int) (31/30.0);
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(smartLock.getBikeTypeId());
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
			
		return AppConstant.SUCCESS;
	}
	
	

	/**
	 * @author zjm
	 */
	@Transactional
	public Map<String, Object> newCyclingOrder(String nowPos) {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		Map<String, Object> map = new HashMap<String, Object>();
		if(list!=null && list.size()>0 && list.get(0).getCyclingOrderStatCode().equals(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode())) {
			
				//用车中显示在前台时执行
				CyclingOrder cyclingOrder2 = list.get(0);
				
				long sec = DateDistanceUtil.getDistanceTimes(cyclingOrder2.getStartTime(), new Date());
				long min = sec / AppConstant.EVERY_MIN;
				//System.out.println("已骑行" + min + "分钟");
				//写死 每半小时支付0.5元
				Integer count = (int) (min/AppConstant.EVERY_HALF_HOUR) + 1;
				//Integer count = (int) (31/30.0);
				SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder2.getSmartLockId());
				BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(smartLock.getBikeTypeId());
				
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
				
				
				map.put(AppConstant.MIN, min+"");
				map.put(AppConstant.BIKE_NUMBER, cyclingOrder2.getBikePlateNumber());
				map.put(AppConstant.PAY_MONEY, payMoney+"");
				map.put(AppConstant.FIRST_POS, firstPos);
				map.put(AppConstant.POLYLINE_PATH, polylinePath+"");
				System.out.println("firstPos:"+firstPos+" polylinePath:"+polylinePath);
	
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
		return map;
	}

	/**
	 * @author lzh
	 */
	public Map<String, String> getMenuData(MemberUser user){
		Map<String, String> map = new HashMap<String, String>();
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class)
			.findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		Integer cyclingDistace = 0;
		for (CyclingOrder cyclingOrder : list) { //TODO Fly 1.更改为数据库运算？ 2.米的问题
			cyclingDistace += cyclingOrder.getCyclingDistanceMeter();
		}
		map.put("item0", cyclingDistace.toString());
		map.put("item1", Double.toString((cyclingDistace / 16) * 413.27));
		map.put("item2", SpringContextHelper.getBean(MemberAccountSearchService.class).findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.PRE_DEPOSIT.toCode()).get(0).getAccountValue().toString());
		map.put("item3", Integer.toString(list.size()));
		map.put("item4", "0");
		
		return map;
	}
	/**
	 * 骑行
	 * @author lzh
	 */
	public String orderPay(Integer orderId, MemberUser user){
		CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderService.class).getById(orderId);
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
				.findBySmartLockIdAndCyclingOrderStatCodeOrderByStartTimeDesc(smartLockId, CyclingOrderStatCodeEnum.ON_THE_WAY.toCode());
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
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(smartLockId);
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
			
			//更新智能锁状态表记录
			List<SmartLockStat> statList = SpringContextHelper.getBean(SmartLockStatService.class).findByKey(SmartLockStat.SMART_LOCK_ID, cyclingOrder.getSmartLockId());
			SmartLockStat smartLockStat = statList.get(0);
			
			smartLockStat.setLockLng(lng);
			smartLockStat.setLockLat(lat);
			smartLockStat.setLockSwitchStatCode(LockSwitchStatCodeEnum.CLOCK.toCode());
			smartLockStat.setLastLocationUpdTime(nowDate);
			smartLockStat.setLockBattery(lockBattery);
			
			SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
			
			//通知app跳转页面
			WebsocketEndPoint.sendMessageToUser(cyclingOrder.getUserId(),new TextMessage("success"));
		}
		
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
		if(cyclingTrajectoryRecordList != null){
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
}
