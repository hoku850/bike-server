package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.LockSwitchStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.repository.SmartLockStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockStatService extends BaseService<SmartLockStat,java.lang.Integer, SmartLockStatRepository>{
	
	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public Map<String, Object> getBikeLocation(String position) {
		List<String> bikeLocationList = new ArrayList<String>();
		
	    String[] splitPos = PositionUtil.splitPos(position);
	    Double lng = Double.valueOf(splitPos[0]);
	    Double lat = Double.valueOf(splitPos[1]);
	    //半径为500米
	    double[] around = PositionUtil.getAround(lat, lng, 500);
		
		List<SmartLockStat> list = SpringContextHelper.getBean(SmartLockStatSearchService.class).
		findByLockSwitchStatCodeAndIfRepairIngAndLockLatBetweenAndLockLngBetween(
				LockSwitchStatCodeEnum.CLOCK.toCode(),"N",around[0],around[2],
				around[1],around[3]);
		
		if(list!=null && list.size()>0) {
			StringBuffer posString = new StringBuffer("");
			for(SmartLockStat smartLockStat : list) {
				double distance = PositionUtil.getDistanceByLongNLat(lng, lat, smartLockStat.getLockLng(), smartLockStat.getLockLat());
				if(distance <= 500) {
					posString = posString.append("[").append(smartLockStat.getLockLng())
							.append(",").append(smartLockStat.getLockLat()).append("]");
					bikeLocationList.add(posString.toString());
				}
			}
		}
		
		/*bikeLocationList.add("[113.370900,23.132915]");
		bikeLocationList.add("[113.370650,23.133000]");
		bikeLocationList.add("[113.370700,23.133100]");*/
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bikeLocationList", bikeLocationList);
		
		return map;
	}
	
}
