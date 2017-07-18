package org.ccframe.sdk.map.funtion;

import java.util.List;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BigDecimalUtil;
import org.ccframe.sdk.map.domain.AMapData;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.ccframe.subsys.bike.service.UserToRepairRecordService;

public class AMapApi {
	
	private static String biejing = "[116.397428, 39.90923]";
	
	private static AMapData getDefaultData(){
		AMapData aMapData = new AMapData();
		
		aMapData.setCenterPosition(biejing);
		aMapData.setZoom(13);
		aMapData.setLineArr("[ [116.368904, 39.913423],[116.382122, 39.901176],[116.387271, 39.912501],[116.398258, 39.904600] ]");
		aMapData.setIcon("'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'");
		aMapData.setIconPosition("[116.405467, 39.907761]");
		return aMapData;
	}
	
	public static AMapData getAMapDataByCyclingOrderId(Integer id){
		AMapData aMapData = new AMapData();
		
		List<CyclingTrajectoryRecord> list = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, id);
		if (list.size() != 0) {
			StringBuilder lineArr = new StringBuilder("[ ");
			for (CyclingTrajectoryRecord cyclingTrajectoryRecord : list) {
				String LngLat = "[" + cyclingTrajectoryRecord.getRecordLocationLng() + ", " + cyclingTrajectoryRecord.getRecordLocationLat() + "], ";
				lineArr.append(LngLat);
			}
			lineArr.deleteCharAt(lineArr.length() - 1);
			lineArr.append(" ]");
			aMapData.setLineArr(lineArr.toString());
			
			aMapData.setStartPosition("[" + list.get(0).getRecordLocationLng() + ", " + list.get(0).getRecordLocationLat() + "]");
			aMapData.setEndPosition("[" + list.get(list.size()-1).getRecordLocationLng() + ", " + list.get(list.size()-1).getRecordLocationLat() + "]");
			
			CyclingTrajectoryRecord record = list.get(list.size() / 2);
			String centerPosition = "[" + record.getRecordLocationLng() + ", " + record.getRecordLocationLat() + "]";
			aMapData.setCenterPosition(centerPosition);
		} else {
			CyclingOrder cyclingOrder = SpringContextHelper.getBean(CyclingOrderService.class).getById(id);
			String startPosition = "[" + cyclingOrder.getStartLocationLng() + ", " + cyclingOrder.getStartLocationLat() + "]";
			String endPosition = "[" + cyclingOrder.getEndLocationLng() + ", " + cyclingOrder.getEndLocationLat() + "]";
			
			aMapData.setLineArr("[" + startPosition + ", " + endPosition + "]");
			aMapData.setStartPosition(startPosition);
			aMapData.setEndPosition(endPosition);
			
			// 起终点算出中间点
			Double lng = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLng(), cyclingOrder.getEndLocationLng()), 2);
			Double lat = BigDecimalUtil.divide(BigDecimalUtil.add(cyclingOrder.getStartLocationLat(), cyclingOrder.getEndLocationLat()), 2);
			aMapData.setCenterPosition("[" + lng + ", " + lat + "]");
		}
		
		aMapData.setZoom(16);
		return aMapData;
	}
	
	public static AMapData getAMapDataByUserToRepairRecordId(Integer id){
		AMapData aMapData = new AMapData();
		
		UserToRepairRecord userToRepairRecord = SpringContextHelper.getBean(UserToRepairRecordService.class).getById(id);
		
		if (userToRepairRecord != null) {
			String iconPosition = "[" + userToRepairRecord.getToRepairLocationLng() + ", " + userToRepairRecord.getToRepairLocationLat() + "]";
			aMapData.setIconPosition(iconPosition);
			aMapData.setCenterPosition(iconPosition);
		} else {
			aMapData.setIconPosition(biejing);
			aMapData.setCenterPosition(biejing);
		}
		aMapData.setZoom(16);
		return aMapData;
	}
}
