package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.dto.CyclingOrderClientPage;
import org.ccframe.subsys.bike.dto.CyclingOrderListReq;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.ccframe.subsys.bike.search.CyclingOrderSearchRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.ccframe.subsys.core.service.UserSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class CyclingOrderSearchService extends BaseSearchService<CyclingOrder, Integer, CyclingOrderSearchRepository> {

	public CyclingOrderClientPage<CyclingOrderRowDto> findList(CyclingOrderListReq cyclingOrderListReq, int offset, int limit) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 过滤运营商
		Integer orgId = cyclingOrderListReq.getOrgId();
		if(orgId != null && orgId != Global.COMBOBOX_ALL_VALUE){
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.ORG_ID, orgId));
		}
		
		// 过滤单车类型 -> 找出该单车类型的智能锁，过滤掉
		Integer typeId = cyclingOrderListReq.getBikeTypeId();
		if(typeId != null && typeId != Global.COMBOBOX_ALL_VALUE){
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.BIKE_TYPE_ID, typeId));
		}
		
		// 过滤状态
		if(StringUtils.isNotBlank(cyclingOrderListReq.getOrderState())){
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.CYCLING_ORDER_STAT_CODE, cyclingOrderListReq.getOrderState()));
		}
		
		// 过滤时间  GWT设有默认时间，不会为空
		RangeQueryBuilder builder = QueryBuilders.rangeQuery(CyclingOrder.END_TIME);
		builder.gte(cyclingOrderListReq.getStartTime() == null ? Global.MIN_SEARCH_DATE : cyclingOrderListReq.getStartTime());
		builder.lte(cyclingOrderListReq.getEndTime() == null ? Global.MAX_SEARCH_DATE : cyclingOrderListReq.getEndTime());
		boolQueryBuilder.must(builder);

		// 过滤[登陆ID/硬件编号/车牌号]s
		BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
		if (StringUtils.isNotBlank(cyclingOrderListReq.getSearchField())) {
			// 登陆ID
			User user = SpringContextHelper.getBean(UserSearchService.class).getByKey(User.LOGIN_ID, cyclingOrderListReq.getSearchField());
			if (user != null) shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.USER_ID, user.getUserId()));
			// 硬件编号s
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getByKey(SmartLock.LOCKER_HARDWARE_CODE, cyclingOrderListReq.getSearchField());
			if (smartLock != null) shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.SMART_LOCK_ID, smartLock.getSmartLockId()));
			
			shouldQueryBuilder.should(QueryBuilders.termQuery(CyclingOrder.BIKE_PLATE_NUMBER, cyclingOrderListReq.getSearchField()));
			boolQueryBuilder.must(shouldQueryBuilder);
		}
		
		// 统计骑行总金额
		Double totalAmount = SpringContextHelper.getBean(CyclingOrderSearchService.class).sumQuery(boolQueryBuilder, CyclingOrder.ORDER_AMMOUNT);
		
		// 查询
		Page<CyclingOrder> cPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, CyclingOrder.END_TIME))
		);

		List<CyclingOrderRowDto> resultList = new ArrayList<CyclingOrderRowDto>();
		for(CyclingOrder cyclingOrder : cPage.getContent()){
			
			CyclingOrderRowDto cyclingOrderRowDto = new CyclingOrderRowDto();
			BeanUtils.copyProperties(cyclingOrder, cyclingOrderRowDto);
			// 查询出登陆ID
			User user = SpringContextHelper.getBean(UserSearchService.class).getById(cyclingOrder.getUserId());
			if (user != null) {
				cyclingOrderRowDto.setLoginId(user.getLoginId());
			}
			// 查询出运营商的信息
			Org org = SpringContextHelper.getBean(OrgSearchService.class).getById(cyclingOrder.getOrgId());
			if (org!=null) {
				cyclingOrderRowDto.setOrgNm(org.getOrgNm());
			}
			// 查询出智能锁硬件编号
			SmartLock smartLock = SpringContextHelper.getBean(SmartLockSearchService.class).getById(cyclingOrder.getSmartLockId());
			if (smartLock != null) {
				cyclingOrderRowDto.setLockerHardwareCode(smartLock.getLockerHardwareCode());
			}
			// 查询出单车类型
			BikeType bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(cyclingOrder.getBikeTypeId());
			if (bikeType != null) {
				cyclingOrderRowDto.setBikeTypeNm(bikeType.getBikeTypeNm());
			}
			resultList.add(cyclingOrderRowDto);
		}
		CyclingOrderClientPage<CyclingOrderRowDto> clientPage = new CyclingOrderClientPage<CyclingOrderRowDto>((int)cPage.getTotalElements(), offset / limit, limit, resultList);
		clientPage.setCyclingOrderTotalAmount(totalAmount);
		return clientPage;
	}

	/**
	 * @author zjm
	 */
	public List<CyclingOrder> findByUserIdAndOrgIdOrderByEndTimeDesc(
			Integer userId, Integer orgId) {

		if (userId != null && orgId != null) {
			return this.getRepository().findByUserIdAndOrgIdOrderByEndTimeDesc(
					userId, orgId);
		}
		return null;

	}

	/**
	 * @author yjz
	 */
	public List<CyclingOrder> findBySmartLockIdOrderByStartTimeDesc(
			Integer smartLockId) {
		return this.getRepository().findBySmartLockIdOrderByStartTimeDesc(
				smartLockId);
	}

	/**
	 * @author zjm
	 */

	public List<CyclingOrder> findByUserIdAndOrgIdOrderByStartTimeDesc(
			Integer userId, Integer orgId) {
		if (userId != null && orgId != null) {

			return this.getRepository()
					.findByUserIdAndOrgIdOrderByStartTimeDesc(userId, orgId);
		}
		return null;

	}

	/**
	 * @author lzh
	 */
	public Map<String, String> getOrderPayDetail(MemberUser memberUser) {
		final int TO_REPAIR_WAIT_TIME = 60 * 1000 * 3;// 3分钟转换成毫秒
		Map<String, String> map = new HashMap<String, String>();
		// 查询未完成的唯一订单
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.USER_ID,
				memberUser.getUserId()));
		boolQueryBuilder.must(QueryBuilders.termQuery(
				CyclingOrder.CYCLING_ORDER_STAT_CODE,
				CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode()));
		boolQueryBuilder.must(QueryBuilders.termQuery(
				CyclingOrder.ORG_ID, 
				memberUser.getOrgId()));
		Iterable<CyclingOrder> content = this.getRepository().search(
				boolQueryBuilder);
		CyclingOrder cyclingOrder;
		if (content.iterator().hasNext()) {
			cyclingOrder = content.iterator().next();
		} else {
			return null;
		}

		HashMap<String, Double> discount = new HashMap<String, Double>();
		if ((cyclingOrder.getEndTime().getTime() - cyclingOrder.getStartTime()
				.getTime()) < TO_REPAIR_WAIT_TIME) {
			UserToRepairRecord userToRepairRecord = SpringContextHelper
					.getBean(UserToRepairRecordSearchService.class)
					.getLatestUserToRepairRecord(memberUser.getUserId(),
							cyclingOrder.getBikePlateNumber());
			if (userToRepairRecord != null
					&& userToRepairRecord.getToRepairTime().getTime() > cyclingOrder
							.getStartTime().getTime()) {
				Double subAmmount = cyclingOrder.getOrderAmmount() * -1.0;

				discount.put(AppConstant.TO_REPAIR_DREE, subAmmount);

				cyclingOrder.setOrderAmmount(0.00);
				this.save(cyclingOrder);

			}

		}

		// 查询单车类型
		BikeType bikeType;
		bikeType = SpringContextHelper.getBean(BikeTypeSearchService.class).getById(cyclingOrder.getBikeTypeId());
		map.put("time", cyclingOrder.getCyclingContinousSec().toString());
		map.put("price", cyclingOrder.getOrderAmmount().toString());
		map.put("pricePerHalfHour", bikeType.getHalfhourAmmount().toString());
		map.put("cyclingOriderId", cyclingOrder.getCyclingOrderId().toString());
		map.put("discount", discount.toString());
		return map;
	}

	public List<CyclingOrder> findBySmartLockIdAndCyclingOrderStatCodeOrderByStartTimeDesc(
			Integer smartLockId, String code) {
		if (smartLockId != null && code != null) {

			return this
					.getRepository()
					.findBySmartLockIdAndCyclingOrderStatCodeOrderByStartTimeDesc(
							smartLockId, code);
		}
		return null;
	}

	public List<CyclingOrder> findByUserIdAndOrgIdAndCyclingOrderStatCode(Integer userId, Integer orgId,
			String code) {
		if(userId!=null && orgId!=null && code!=null) {
			return this
					.getRepository().findByUserIdAndOrgIdAndCyclingOrderStatCode(userId, orgId, code);
		}
			
		return null;
	}
	
	public Integer countByOrgIdAndCyclingOrderStatCode(Integer orgId, String cyclingOrderStatCode){
		return this.getRepository().countByOrgIdAndCyclingOrderStatCode(orgId, cyclingOrderStatCode);
	}

}
