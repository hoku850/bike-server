package org.ccframe.sdk.bike.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.dto.Position;
import org.ccframe.subsys.bike.domain.entity.CyclingTrajectoryRecord;
import org.ccframe.subsys.bike.service.CyclingTrajectoryRecordService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.google.gwt.dev.jjs.Correlation.Literal;

/**
 * @author zjm
 */
public class PositionUtil {

	/**
	 * 计算地球上任意两点(经纬度)距离
	 *
	 * @param long1 第一点经度
	 * @param lat1  第一点纬度
	 * @param long2 第二点经度
	 * @param lat2  第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double getDistanceByLongNLat(double long1, double lat1, double long2, double lat2) {
	    double a, b, R;
	    R = 6378137;//地球半径
	    lat1 = lat1 * Math.PI / 180.0;
	    lat2 = lat2 * Math.PI / 180.0;
	    a = lat1 - lat2;
	    b = (long1 - long2) * Math.PI / 180.0;
	    double d;
	    double sa2, sb2;
	    sa2 = Math.sin(a / 2.0);
	    sb2 = Math.sin(b / 2.0);
	    d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
	    return d;
	}

	/**
	 * 根据经纬度和半径计算经纬度范围
	 *
	 * @param raidus 单位米
	 * @return minLat, minLng, maxLat, maxLng
	 */
	public static double[] getAround(double lat, double lon, int raidus) {

	    Double latitude = lat;
	    Double longitude = lon;

	    Double degree = (24901 * 1609) / 360.0;
	    double raidusMile = raidus;

	    Double dpmLat = 1 / degree;
	    Double radiusLat = dpmLat * raidusMile;
	    Double minLat = latitude - radiusLat;
	    Double maxLat = latitude + radiusLat;

	    Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
	    Double dpmLng = 1 / mpdLng;
	    Double radiusLng = dpmLng * raidusMile;
	    Double minLng = longitude - radiusLng;
	    Double maxLng = longitude + radiusLng;
	    return new double[]{minLat, minLng, maxLat, maxLng};
	}
	
	/**
	 * @author zjm
	 */
	public static String[] splitPos(String startPos) {
		String[] split = new String[2];
		if(StringUtils.isNotBlank(startPos)) {
			split = startPos.split(",");
			return split;
		}
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
