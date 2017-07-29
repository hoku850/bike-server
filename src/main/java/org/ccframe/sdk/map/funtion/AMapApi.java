package org.ccframe.sdk.map.funtion;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.sdk.map.domain.AMapData;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.ccframe.subsys.bike.service.UserToRepairRecordSearchService;

public class AMapApi {
	
	private static final String BRACKET_LEFT = "[";
	private static final String BRACKET_RIGHT = "]";
	private static final String BIEJING = BRACKET_LEFT + Global.BIEJING_LNG + Global.COMMA + Global.BIEJING_LAT + BRACKET_RIGHT;
	private static final Integer ZOOM = 14;
	
	/**
	 * 返回天安门的数据
	 * @return
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
	
	public static AMapData getAMapDataByCyclingOrderId(Integer id){
		AMapData aMapData = new AMapData();
		List<CyclingTrajectoryRecord> list = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, id);
		if (list != null && list.size() != 0) {
			StringBuilder lineArr = new StringBuilder(BRACKET_LEFT);
			for (CyclingTrajectoryRecord cyclingTrajectoryRecord : list) {
				lineArr.append(BRACKET_LEFT);
				lineArr.append(cyclingTrajectoryRecord.getRecordLocationLng());
				lineArr.append(Global.COMMA);
				lineArr.append(cyclingTrajectoryRecord.getRecordLocationLat());
				lineArr.append(BRACKET_RIGHT);
				lineArr.append(Global.COMMA);
			}
			lineArr.deleteCharAt(lineArr.length() - 1);
			lineArr.append(BRACKET_RIGHT);
			aMapData.setLineArr(lineArr.toString());
			
			aMapData.setStartPosition(lngLatFormat(list.get(0).getRecordLocationLng(), list.get(0).getRecordLocationLat()));
			aMapData.setEndPosition(lngLatFormat(list.get(list.size()-1).getRecordLocationLng(), list.get(list.size()-1).getRecordLocationLat()));
			
			CyclingTrajectoryRecord record = list.get(list.size() / 2);
			aMapData.setCenterPosition(lngLatFormat(record.getRecordLocationLng(), record.getRecordLocationLat()));
		} else {
			CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderSearchService.class).getById(id);
			String startPosition = lngLatFormat(cyclingOrder.getStartLocationLng(), cyclingOrder.getStartLocationLat());
			String endPosition = lngLatFormat(cyclingOrder.getEndLocationLng(), cyclingOrder.getEndLocationLat());
			
			aMapData.setLineArr(BRACKET_LEFT + startPosition + Global.COMMA + endPosition + BRACKET_RIGHT);
			aMapData.setStartPosition(startPosition);
			aMapData.setEndPosition(endPosition);
			
			// 起终点算出中间点
			Double lng = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLng(), cyclingOrder.getEndLocationLng()), 2);
			Double lat = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLat(), cyclingOrder.getEndLocationLat()), 2);
			aMapData.setCenterPosition(lngLatFormat(lng, lat));
		}
		aMapData.setZoom(ZOOM);
		return aMapData;
	}
	
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
	
	private static String lngLatFormat(Double lng, Double lat ){
		return BRACKET_LEFT + lng + Global.COMMA + lat + BRACKET_RIGHT;
	}
}