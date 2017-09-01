package org.ccframe.sdk.map.funtion;

import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.sdk.map.domain.AMapData;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.UserToRepairRecordSearchService;

public class AMapApi {
	
	private static final String BRACKET_LEFT = "[";
	private static final String BRACKET_RIGHT = "]";
	private static final String BIEJING = BRACKET_LEFT + Global.BEIJING_LNG + Global.COMMA + Global.BEIJING_LAT + BRACKET_RIGHT;
	private static final Integer ZOOM = 14;
	
	/**
	 * 返回天安门的数据
	 */
	@SuppressWarnings("unused")
	private static AMapData getDefaultData(){
		AMapData aMapData = new AMapData();
		
		aMapData.setCenterPosition(BIEJING);
		aMapData.setZoom(ZOOM);
		aMapData.setLineArr("[[116.368904, 39.913423],[116.382122, 39.901176],[116.387271, 39.912501],[116.398258, 39.904600]]");
		aMapData.setIcon("'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'");
		aMapData.setIconPosition("[116.405467, 39.907761]");
		return aMapData;
	}
	
	/**
	 * 骑行轨迹
	 */
	public static AMapData getAMapDataByCyclingOrderId(Integer id){
		AMapData aMapData = new AMapData();
		
		AppPageDto detail = SpringContextHelper.getBean(CyclingOrderService.class).getTravelDetail(id);
		String polylinePath = (detail == null) ? null : detail.getList();
		//查询骑行记录表是否有该订单，存在该订单
		if (polylinePath != null && polylinePath.length() >= 3) {
			//绘制骑行轨迹
			aMapData.setLineArr(polylinePath);
			//设置图标
			aMapData.setStartPosition(detail.getStartPos());
			aMapData.setEndPosition(detail.getEndPos());
			//设置中心点
			String startPos = detail.getList().substring(1, detail.getList().indexOf(BRACKET_RIGHT)+1);
			aMapData.setCenterPosition(startPos);
		}
		//如果不存在，起点和终点直接连线[注：现改为天安门]
		else {
			aMapData.setLineArr(BRACKET_LEFT + BIEJING + Global.COMMA + BIEJING + BRACKET_RIGHT);
			aMapData.setStartPosition(BIEJING);
			aMapData.setEndPosition(BIEJING);
			aMapData.setCenterPosition(BIEJING);
		}
		
		
//		List<CyclingTrajectoryRecord> recordList = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, id);
//		//查询骑行记录表是否有该订单，存在该订单
//		if (recordList != null && recordList.size() != 0) {
//			//绘制骑行轨迹
//			StringBuilder lineArr = new StringBuilder(BRACKET_LEFT);
//			for (CyclingTrajectoryRecord cyclingTrajectoryRecord : recordList) {
//				lineArr.append(BRACKET_LEFT);
//				lineArr.append(cyclingTrajectoryRecord.getRecordLocationLng());
//				lineArr.append(Global.COMMA);
//				lineArr.append(cyclingTrajectoryRecord.getRecordLocationLat());
//				lineArr.append(BRACKET_RIGHT);
//				lineArr.append(Global.COMMA);
//			}
//			lineArr.deleteCharAt(lineArr.length() - 1);
//			lineArr.append(BRACKET_RIGHT);
//			aMapData.setLineArr(lineArr.toString());
//			
//			//设置图标
//			aMapData.setStartPosition(lngLatFormat(recordList.get(0).getRecordLocationLng(), recordList.get(0).getRecordLocationLat()));
//			aMapData.setEndPosition(lngLatFormat(recordList.get(recordList.size()-1).getRecordLocationLng(), recordList.get(recordList.size()-1).getRecordLocationLat()));
//
//			//设置中心的
//			CyclingTrajectoryRecord record = recordList.get(recordList.size() / 2);
//			aMapData.setCenterPosition(lngLatFormat(record.getRecordLocationLng(), record.getRecordLocationLat()));
//		}
//		//如果不存在，起点和终点直接连线
//		else {
//			CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(id);
//			String startPosition = lngLatFormat(cyclingOrder.getStartLocationLng(), cyclingOrder.getStartLocationLat());
//			String endPosition = lngLatFormat(cyclingOrder.getEndLocationLng(), cyclingOrder.getEndLocationLat());
//			
//			aMapData.setLineArr(BRACKET_LEFT + startPosition + Global.COMMA + endPosition + BRACKET_RIGHT);
//			aMapData.setStartPosition(startPosition);
//			aMapData.setEndPosition(endPosition);
//			
//			// 起终点算出中间点
//			Double lng = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLng(), cyclingOrder.getEndLocationLng()), 2);
//			Double lat = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLat(), cyclingOrder.getEndLocationLat()), 2);
//			aMapData.setCenterPosition(lngLatFormat(lng, lat));
//		}
		
		aMapData.setZoom(ZOOM);
		return aMapData;
	}
	
	/**
	 * 报修记录
	 */
	public static AMapData getAMapDataByUserToRepairRecordId(Integer id){
		AMapData aMapData = new AMapData();
		UserToRepairRecord userToRepairRecord = SpringContextHelper.getBean(UserToRepairRecordSearchService.class).getById(id);
		if (userToRepairRecord != null) {
			String iconPosition = lngLatFormat(userToRepairRecord.getToRepairLocationLng(), userToRepairRecord.getToRepairLocationLat());
			aMapData.setIconPosition(iconPosition);
			aMapData.setCenterPosition(iconPosition);
		} else {
			aMapData.setIconPosition(BIEJING);
			aMapData.setCenterPosition(BIEJING);
		}
		aMapData.setZoom(ZOOM);
		return aMapData;
	}
	
	/**
	 * 格式化经纬度
	 */
	private static String lngLatFormat(Double lng, Double lat ){
		return BRACKET_LEFT + lng + Global.COMMA + lat + BRACKET_RIGHT;
	}
	
}