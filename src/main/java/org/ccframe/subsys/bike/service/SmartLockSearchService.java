package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.code.LockSwitchStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.SmartLockStat;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.ccframe.subsys.bike.dto.SmartLockGrantDto;
import org.ccframe.subsys.bike.dto.SmartLockListReq;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.ccframe.subsys.bike.search.SmartLockSearchRepository;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class SmartLockSearchService extends BaseSearchService<SmartLock, Integer, SmartLockSearchRepository>{

	public ClientPage<SmartLockRowDto> findSmartLockList(SmartLockListReq smartLockListReq, int offset, int limit) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder searchTextboolQueryBuilder = QueryBuilders.boolQuery();
		if(StringUtils.isNotBlank(smartLockListReq.getSearchText())){
			searchTextboolQueryBuilder.should(QueryBuilders.termQuery(SmartLock.MAC_ADDRESS, smartLockListReq.getSearchText().toLowerCase()));
			searchTextboolQueryBuilder.should(QueryBuilders.termQuery(SmartLock.BIKE_PLATE_NUMBER, smartLockListReq.getSearchText().toLowerCase()));
			searchTextboolQueryBuilder.should(QueryBuilders.termQuery(SmartLock.LOCKER_HARDWARE_CODE, smartLockListReq.getSearchText().toLowerCase()));
			searchTextboolQueryBuilder.should(QueryBuilders.termQuery(SmartLock.IMEI_CODE, smartLockListReq.getSearchText().toLowerCase()));
		}
		
		boolQueryBuilder.must(searchTextboolQueryBuilder);
		if(smartLockListReq.getOrgId() != null && smartLockListReq.getOrgId() > 0){
			boolQueryBuilder.must(QueryBuilders.termQuery(SmartLock.ORG_ID, smartLockListReq.getOrgId()));
		}
		if(StringUtils.isNotBlank(smartLockListReq.getSmartLockStatCode())){
			boolQueryBuilder.must(QueryBuilders.termQuery(SmartLock.SMART_LOCK_STAT_CODE, smartLockListReq.getSmartLockStatCode()));
		}
		
		Page<SmartLock> smartLockPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, SmartLock.SMART_LOCK_ID))
		);
		List<SmartLockRowDto> resultList = new ArrayList<SmartLockRowDto>();
		for(SmartLock smartLock:smartLockPage.getContent()){
			SmartLockRowDto rowRecord = new SmartLockRowDto();
			Org org = SpringContextHelper.getBean(OrgService.class).getById(smartLock.getOrgId());
			if (org != null) {
				rowRecord.setOrgNm(org.getOrgNm());
			}
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
			if (bikeType != null) {
				rowRecord.setBikeTypeNm(bikeType.getBikeTypeNm());
			}
			BeanUtils.copyProperties(smartLock, rowRecord);
			resultList.add(rowRecord);
		}
		return new ClientPage<SmartLockRowDto>((int)smartLockPage.getTotalElements(), offset / limit, limit, resultList);
	}
	
	public long grantSearch(SmartLockGrant smartLockGrant) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		RangeQueryBuilder rangeQuerybuilder = QueryBuilders.rangeQuery(SmartLock.LOCKER_HARDWARE_CODE);
		if(StringUtils.isNotBlank(smartLockGrant.getStartLockerHardwareCode())){
			rangeQuerybuilder.from(smartLockGrant.getStartLockerHardwareCode());
		}
		if(StringUtils.isNotBlank(smartLockGrant.getEndLockerHardwareCode())){
			rangeQuerybuilder.to(smartLockGrant.getEndLockerHardwareCode());
		}
		boolQueryBuilder.must(rangeQuerybuilder);
		if(StringUtils.isNotBlank(smartLockGrant.getBikePlateNumberPrefixText())){
			PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(SmartLock.BIKE_PLATE_NUMBER, smartLockGrant.getBikePlateNumberPrefixText());
			boolQueryBuilder.must(prefixQueryBuilder);
		}
		boolQueryBuilder.mustNot(QueryBuilders.termQuery(SmartLock.SMART_LOCK_STAT_CODE, SmartLockStatCodeEnum.UNPRODUCE.toCode()));
		Page<SmartLock> smartLockPage = this.getRepository().search(boolQueryBuilder, null);
		long totalLock = 0;
		if(smartLockPage != null){
			totalLock = smartLockPage.getTotalElements();
		}
		return totalLock;
	}
	
	public SmartLockGrantDto grant(SmartLockGrant smartLockGrant) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		RangeQueryBuilder rangeQuerybuilder = QueryBuilders.rangeQuery(SmartLock.LOCKER_HARDWARE_CODE);
		
		SmartLockGrantDto smartLockGrantDto = new SmartLockGrantDto();
		
		if(StringUtils.isNotBlank(smartLockGrant.getStartLockerHardwareCode())){
			rangeQuerybuilder.from(smartLockGrant.getStartLockerHardwareCode());
		}
		if(StringUtils.isNotBlank(smartLockGrant.getEndLockerHardwareCode())){
			rangeQuerybuilder.to(smartLockGrant.getEndLockerHardwareCode());
		}
		boolQueryBuilder.must(rangeQuerybuilder);
		if(StringUtils.isNotBlank(smartLockGrant.getBikePlateNumberPrefixText())){
			PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery(SmartLock.BIKE_PLATE_NUMBER, smartLockGrant.getBikePlateNumberPrefixText().toLowerCase());
			boolQueryBuilder.must(prefixQueryBuilder);
		}
		boolQueryBuilder.mustNot(QueryBuilders.termQuery(SmartLock.SMART_LOCK_STAT_CODE, SmartLockStatCodeEnum.UNPRODUCE.toCode()));
		
		Page<SmartLock> smartLockPage = this.getRepository().search(boolQueryBuilder, null);
		smartLockGrantDto.setTotalLock(smartLockPage.getTotalElements());
		
		List<SmartLock> list = smartLockPage.getContent();
		for (SmartLock smartLock : list) {
			smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.GRANTED.toCode());
			smartLock.setOrgId(smartLockGrant.getOrgId());
			smartLock.setBikeTypeId(smartLockGrant.getBikeTypeId());
			if(smartLock != null){
				SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
				
				SmartLockStat smartLockStat = new SmartLockStat();
				smartLockStat.setSmartLockId(smartLock.getSmartLockId());
				smartLockStat.setOrgId(smartLock.getOrgId());
				smartLockStat.setLockLng(39.54);//天安门经纬度
				smartLockStat.setLockLat(116.23);
//				smartLockStat.setLockBattery(lockLng);
				smartLockStat.setLockSwitchStatCode(LockSwitchStatCodeEnum.NO_USE.toCode());
				smartLockStat.setIfRepairIng(BoolCodeEnum.NO.toCode());
//				smartLockStat.setLastLocationUpdTime(lockLng);
				SpringContextHelper.getBean(SmartLockStatService.class).save(smartLockStat);
			}

			Org org = SpringContextHelper.getBean(OrgService.class).getById(smartLockGrant.getOrgId());
			smartLockGrantDto.setOrgNm(org.getOrgNm());
		}
		return smartLockGrantDto;
	}
}
