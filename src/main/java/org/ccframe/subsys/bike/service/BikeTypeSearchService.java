package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.dto.BikeTypeListReq;
import org.ccframe.subsys.bike.dto.BikeTypeRowDto;
import org.ccframe.subsys.bike.search.BikeTypeSearchRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class BikeTypeSearchService extends BaseSearchService<BikeType, Integer, BikeTypeSearchRepository> {

	public ClientPage<BikeTypeRowDto> findBikeTypeList(BikeTypeListReq bikeTypeListReq, int offset, int limit) {
	
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		//过滤运营商ID
		Integer orgId = bikeTypeListReq.getOrgId();
		if(orgId != null && orgId != 0){
			boolQueryBuilder.must(QueryBuilders.termQuery(BikeType.ORG_ID, bikeTypeListReq.getOrgId()));
		}
		
		Page<BikeType> bikeTypePage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, BikeType.ORG_ID))
		);

		List<BikeTypeRowDto> resultList = new ArrayList<BikeTypeRowDto>();
		for(BikeType bikeType : bikeTypePage.getContent()){
			
			BikeTypeRowDto bikeTypeRowDto = new BikeTypeRowDto();
			BeanUtils.copyProperties(bikeType, bikeTypeRowDto);
			// 查询出运营商的信息
			Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(bikeType.getOrgId());
			if (org != null) bikeTypeRowDto.setOrgNm(org.getOrgNm());
			resultList.add(bikeTypeRowDto); 
		}
		return new ClientPage<BikeTypeRowDto>((int)bikeTypePage.getTotalElements(), offset / limit, limit, resultList);
	}

}
