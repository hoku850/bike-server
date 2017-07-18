package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.CyclingOrderListReq;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.ccframe.subsys.bike.search.CyclingOrderSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class CyclingOrderSearchService extends BaseSearchService<CyclingOrder, Integer, CyclingOrderSearchRepository>{

	public ClientPage<CyclingOrderRowDto> findList(CyclingOrderListReq cyclingOrderListReq, int offset, int limit) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 过滤运营商
		Integer orgId = cyclingOrderListReq.getOrgId();
		if(orgId != null && orgId != 0){
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.ORG_ID, orgId));
		}
		// 过滤单车类型 -> 找出该单车类型的智能锁，过滤掉
		Integer typeId = cyclingOrderListReq.getBikeTypeId();
		if(typeId != null && typeId != 0){
			List<SmartLock> locks = SpringContextHelper.getBean(SmartLockService.class).findByKey(SmartLock.BIKE_TYPE_ID, typeId);
			for (SmartLock smartLock : locks) {
				boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.SMART_LOCK_ID, smartLock.getSmartLockId()));
			}
			// TODO 有bug
			if (locks.size() == 0) {
				boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.ORG_ID, 0));
//				throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"没有该类型单车的骑行订单"});
			}
		}
		// 过滤状态
		if(StringUtils.isNotBlank(cyclingOrderListReq.getOrderState())){
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.CYCLING_ORDER_STAT_CODE, cyclingOrderListReq.getOrderState()));
		}
		// 过滤时间  GWT设有默认时间，不会为空
		RangeQueryBuilder builder = QueryBuilders.rangeQuery(CyclingOrder.START_TIME);
		builder.gte(cyclingOrderListReq.getStartTime() == null ? Global.MIN_SEARCH_DATE : cyclingOrderListReq.getStartTime());
		builder.lte(cyclingOrderListReq.getEndTime() == null ? Global.MAX_SEARCH_DATE : cyclingOrderListReq.getEndTime());
		boolQueryBuilder.must(builder);

		// 过滤[登陆ID/硬件编号/车牌号]
		BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(cyclingOrderListReq.getSearchField())) {
			shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.USER_ID, cyclingOrderListReq.getSearchField()));
			shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.SMART_LOCK_ID, cyclingOrderListReq.getSearchField()));
			shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.BIKE_PLATE_NUMBER, cyclingOrderListReq.getSearchField()));
			boolQueryBuilder.must(shouldQueryBuilder);
		}
		
		// 查询
		Page<CyclingOrder> cPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, CyclingOrder.END_TIME))
		);

		List<CyclingOrderRowDto> resultList = new ArrayList<CyclingOrderRowDto>();
		for(CyclingOrder cyclingOrder : cPage.getContent()){
			
			CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
			BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
			// 查询出运营商的信息
			Agent org = SpringContextHelper.getBean(AgentService.class).getById(cyclingOrder.getOrgId());
			if (org!=null) {
				cyclingOrderRowDto.setOrgNm(org.getAgentNm());
			}
			// 查询出单车类型
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(cyclingOrder.getSmartLockId());
			if (smartLock != null) {
				BikeType bikeType = SpringContextHelper.getBean(BikeTypeService.class).getById(smartLock.getBikeTypeId());
				if (bikeType != null) {
					cyclingOrderRowDto.setBikeTypeNm(bikeType.getBikeTypeNm());
				}
			}
			resultList.add(cyclingOrderRowDto); 
		}
		return new ClientPage<CyclingOrderRowDto>((int)cPage.getTotalElements(), offset / limit, limit, resultList);
	}

	/**
	 * @author zjm
	 */
	public List<CyclingOrder> findByUserIdAndOrgIdOrderByEndTimeDesc(
			Integer userId, Integer orgId) {
		return this.getRepository().findByUserIdAndOrgIdOrderByEndTimeDesc(userId, orgId);
	}
	
	/**
	 * @author zjm
	 */
	public List<CyclingOrder> findByUserIdAndOrgIdOrderByStartTimeDesc(Integer userId, Integer orgId) {
		return this.getRepository().findByUserIdAndOrgIdOrderByEndTimeDesc(userId, orgId);
	}


}
