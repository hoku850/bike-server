package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.ccframe.subsys.bike.dto.SmartLockListReq;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.ccframe.subsys.bike.search.SmartLockSearchRepository;
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
			Agent org = SpringContextHelper.getBean(AgentService.class).getById(smartLock.getOrgId());
			rowRecord.setOrgNm(org.getAgentNm());
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
			rowRecord.setBikeTypeNm(bikeType.getBikeTypeNm());
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
		Page<SmartLock> smartLockPage = this.getRepository().search(boolQueryBuilder, null);
		long totalLock = smartLockPage.getTotalElements();
		return totalLock;
	}
	
	public void grant(SmartLockGrant smartLockGrant) {
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
		Page<SmartLock> smartLockPage = this.getRepository().search(boolQueryBuilder, null);
		Long totalLock = smartLockPage.getTotalElements();
		
		System.out.println("符合条件的车锁共计"+totalLock+"把"+","+"你确定发放吗？该操作将不可撤销！！");
		
		List<SmartLock> list = smartLockPage.getContent();
		for (SmartLock smartLock : list) {
			smartLock.setSmartLockStatCode(SmartLockStatCodeEnum.GRANTED.toCode());
			smartLock.setOrgId(smartLockGrant.getOrgId());
			smartLock.setBikeTypeId(smartLockGrant.getBikeTypeId());
			SpringContextHelper.getBean(SmartLockService.class).save(smartLock);
			
			Agent org = SpringContextHelper.getBean(AgentService.class).getById(smartLockGrant.getOrgId());
			String orgNm = org.getAgentNm();
			
//			Info.display("操作完成", "成功发放单车车锁 "+totalLock+" 把至运营商 "+orgNm);
			System.out.println("成功发放单车车锁 "+totalLock+" 把至运营商 "+orgNm);
		}
		
		
		
	}
}
