package org.ccframe.sdk.bike.utils;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.sdk.bike.dto.Position;

public class PositionUtil {
	
	/**
	 * @author zjm
	 */
	public static String[] splitPos(String startPos) {
		startPos = startPos.substring(1, startPos.length()-2);
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
		paths = paths.substring(1, paths.length()-3);
		String[] split = paths.split("],");
		ArrayList<Position> list = new ArrayList<Position>();
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
		
		return list;
	}
}
