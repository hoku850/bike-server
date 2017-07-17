package org.ccframe.sdk.map.funtion;

import java.util.List;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.map.domain.AMapData;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.ccframe.subsys.bike.service.UserToRepairRecordService;

public class AMapApi {
	
	private static AMapData getDefaultData(){
		AMapData aMapData = new AMapData();
		aMapData.setCenterPosition("[116.397428, 39.90923]");
		aMapData.setZoom(13);
		aMapData.setLineArr("[ [116.368904, 39.913423],[116.382122, 39.901176],[116.387271, 39.912501],[116.398258, 39.904600] ]");
		aMapData.setIcon("'http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png'");
		aMapData.setIconPosition("[116.405467, 39.907761]");
		return aMapData;
	}
	
	public static AMapData getAMapDataByCyclingOrderId(Integer id){
		AMapData aMapData = new AMapData();
		
		List<CyclingTrajectoryRecord> findByKey = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, id);
		StringBuilder lineArr = new StringBuilder("[ ");
		for (CyclingTrajectoryRecord cyclingTrajectoryRecord : findByKey) {
			String LngLat = "[" + cyclingTrajectoryRecord.getRecordLocationLng() + ", " + cyclingTrajectoryRecord.getRecordLocationLat() + "], ";
			lineArr.append(LngLat);
		}
		lineArr.deleteCharAt(lineArr.length() - 1);
		lineArr.append(" ]");
		aMapData.setLineArr(lineArr.toString());
		
		CyclingTrajectoryRecord record = findByKey.get(findByKey.size() / 2);
		String centerPosition = "[" + record.getRecordLocationLng() + ", " + record.getRecordLocationLat() + "]";
		aMapData.setCenterPosition(centerPosition);
		
		aMapData.setZoom(17);
		return aMapData;
	}
	
	public static AMapData getAMapDataByUserToRepairRecordId(Integer id){
		AMapData aMapData = getDefaultData();
		
		UserToRepairRecord userToRepairRecord = SpringContextHelper.getBean(UserToRepairRecordService.class).getById(id);
		String iconPosition = "[" + userToRepairRecord.getToRepairLocationLng() + ", " + userToRepairRecord.getToRepairLocationLat() + "]";
		aMapData.setIconPosition(iconPosition);
		aMapData.setCenterPosition(iconPosition);
		aMapData.setZoom(17);
		return aMapData;
	}
}
