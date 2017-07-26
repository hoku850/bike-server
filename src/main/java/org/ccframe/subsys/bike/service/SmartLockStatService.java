package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.utils.PositionUtil;
import org.ccframe.subsys.bike.domain.code.LockSwitchStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.repository.SmartLockStatRepository;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmartLockStatService extends BaseService<SmartLockStat,java.lang.Integer, SmartLockStatRepository>{

	/**
	 * @author lqz
	 */
	@Transactional
	public SmartLockStat saveOrUpdateSmartLock(SmartLock smartLock, Map<DataBlockTypeEnum, Object> requestDataMap){

		SmartLockStatService smartLockStatService = SpringContextHelper.getBean(SmartLockStatService.class);
		
		SmartLockStat smartLockStat = smartLockStatService.getByKey(SmartLockStat.SMART_LOCK_ID, smartLock.getSmartLockId());
		if (smartLockStat == null) {
			smartLockStat = new SmartLockStat();
			smartLockStat.setSmartLockId(smartLock.getSmartLockId());
			smartLockStat.setOrgId(smartLock.getOrgId());
			smartLockStat.setIfRepairIng(BoolCodeEnum.NO.toCode());
		}
		smartLockStat.setLockSwitchStatCode(Byte.toString((byte)requestDataMap.get(DataBlockTypeEnum.LOCK_STATUS)));
		smartLockStat.setLockBattery((int)(byte)requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
		smartLockStat.setLockLat((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LNG));
		smartLockStat.setLockLng((Double)requestDataMap.get(DataBlockTypeEnum.LOCK_LAT));
		smartLockStat.setLastLocationUpdTime((Date)requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
		
		smartLockStatService.save(smartLockStat);
		
		return smartLockStat;
	}
	
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
