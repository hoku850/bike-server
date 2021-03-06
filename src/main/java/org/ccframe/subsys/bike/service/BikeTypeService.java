package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.cache.CacheEvictBy;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.repository.BikeTypeRepository;
import org.ccframe.subsys.core.service.ILabelValueListProvider;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BikeTypeService extends BaseService<BikeType, Integer, BikeTypeRepository> implements ILabelValueListProvider{
	
	/**
	 * 1 检测智能锁的单车类型字段有没有引用该字段，没有则删除，有则抛异常
	 * 2 删除操作需要检查没有被自行车使用才能删除，否则抛出异常
	 * @param bikeTypeId
	 */
	@Transactional
	public void deleteBikeTypeById(Integer bikeTypeId) {
		BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(bikeTypeId);
		List<SmartLock> locks = SpringContextHelper.getBean(SmartLockSearchService.class).findByKey(SmartLock.BIKE_TYPE_ID, bikeType.getBikeTypeId());
		List<CyclingOrder> orders = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByKey(CyclingOrder.BIKE_TYPE_ID, bikeType.getBikeTypeId());
		if (locks.size() == 0 && orders.size() == 0) {
			SpringContextHelper.getBean(BikeTypeService.class).delete(bikeType);
		} else {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该类型已被使用，禁止删除"});
		}
	}
	
	@Transactional
	public void saveOrUpdateBikeType(BikeType bikeType) {
		if (bikeType.getOrgId() == Global.COMBOBOX_ALL_VALUE) {
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"请选择一个运营商"});
		} else {
			SpringContextHelper.getBean(BikeTypeService.class).save(bikeType);
		}
	}

	@Transactional(readOnly = true)
	@CacheEvictBy({BikeType.class})
	@Cacheable(value=Global.EH_CACHE_AUTO_CACHE, cacheResolver = Global.EH_CACHE_RESOLVER)
	public BikeType getBikeTypeByNm(String BikeTypeNm){
        return this.getByKey(BikeType.BIKE_TYPE_NM, BikeTypeNm);
    }
	
	@Override
	@Transactional(readOnly = true)
	public List<LabelValue> getLabelValueList(String beanName, String extraParam) {
		List<LabelValue> resultList = new ArrayList<LabelValue>();
		if (extraParam == null) {
			return resultList;
		}
		// 查询所有
		if (Integer.parseInt(extraParam) == Global.COMBOBOX_ALL_VALUE) {
			for(BikeType bikeType: ((BikeTypeService)SpringContextHelper.getBean(this.getClass())).listAll()){
				resultList.add(new LabelValue(bikeType.getBikeTypeNm(), bikeType.getBikeTypeId()));
			}
		} else { // 级联查询
			for(BikeType bikeType: ((BikeTypeService)SpringContextHelper.getBean(this.getClass())).listAll()){
				if(bikeType.getOrgId() == Integer.parseInt(extraParam)){
					resultList.add(new LabelValue(bikeType.getBikeTypeNm(), bikeType.getBikeTypeId()));
				}
			}
		}
		return resultList;
	}

}
