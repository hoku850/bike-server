package org.ccframe.sdk.bike.utils;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.Position;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.gwt.dev.jjs.Correlation.Literal;

public class PositionUtil {
	
	/**
	 * @author zjm
	 */
	public static String[] splitPos(String startPos) {
		String[] split = startPos.split(",");
		return split;
	}
	
	/**
	 * @author zjm
	 */
	/* [[113.370400,23.132515],
     [113.370800,23.132915],
     [113.370900,23.132815],
     [113.371000,23.132999]] */
	public static List<Position> splitPathsToList(String paths) {
		ArrayList<Position> list = new ArrayList<Position>();
		if(paths.length()>=3) {
			paths = paths.substring(1, paths.length()-3);
			String[] split = paths.split("],");
			
			String lngString = "";
			String latString = "";
			String[] split2 = null;
			for(int i=0; i<split.length; i++){
				split2 = split[i].split(",");
				lngString = split2[0].substring(1);
				latString = split2[1];
				
				Position position = new Position();
				position.setLng(Double.valueOf(lngString));
				position.setLat(Double.valueOf(latString));
				list.add(position);
			}
			
		}
		
		return list;
	}

	public static StringBuffer getPolylinePath(Integer orderId) {
		List<CyclingTrajectoryRecord> list = SpringContextHelper.getBean(CyclingTrajectoryRecordService.class).findByKey(CyclingTrajectoryRecord.CYCLING_ORDER_ID, orderId, new Order(Direction.ASC, CyclingTrajectoryRecord.RECORD_TIME));
		StringBuffer sBuffer = new StringBuffer("");
		if(list!=null && list.size()>0) {
			
			sBuffer.append("[");
			for(CyclingTrajectoryRecord record : list) {
				sBuffer.append("[").append(record.getRecordLocationLng()).
				append(",").append(record.getRecordLocationLat()).append("],");
			}
			sBuffer = sBuffer.deleteCharAt(sBuffer.length()-1);
			sBuffer.append("]");
		}
		return sBuffer;
	}
}
