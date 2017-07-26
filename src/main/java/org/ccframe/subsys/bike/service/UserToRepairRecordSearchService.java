package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.dto.UserToRepairRecordListReq;
import org.ccframe.subsys.bike.dto.UserToRepairRecordRowDto;
import org.ccframe.subsys.bike.search.UserToRepairRecordSearchRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class UserToRepairRecordSearchService extends BaseSearchService<UserToRepairRecord, Integer, UserToRepairRecordSearchRepository> {

	public ClientPage<UserToRepairRecordRowDto> findUserToRepairRecordList(UserToRepairRecordListReq userToRepairRecordListReq, int offset, int limit){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder searchTextboolQueryBuilder = QueryBuilders.boolQuery();
		
		if(StringUtils.isNotBlank(userToRepairRecordListReq.getSearchText())){
			List<SmartLock> smartLockList = SpringContextHelper.getBean(SmartLockService.class).findByKey(SmartLock.LOCKER_HARDWARE_CODE, userToRepairRecordListReq.getSearchText());
			if(smartLockList.size()!=0){
				searchTextboolQueryBuilder.should(QueryBuilders.termQuery(UserToRepairRecord.SMART_LOCK_ID, smartLockList.get(0).getSmartLockId()));
			}
			searchTextboolQueryBuilder.should(QueryBuilders.termQuery(UserToRepairRecord.BIKE_PLATE_NUMBER, userToRepairRecordListReq.getSearchText().toLowerCase()));
		}
		
		boolQueryBuilder.must(searchTextboolQueryBuilder);
		
		if(userToRepairRecordListReq.getOrgId() != null && userToRepairRecordListReq.getOrgId() > 0){
			boolQueryBuilder.must(QueryBuilders.termQuery(UserToRepairRecord.ORG_ID, userToRepairRecordListReq.getOrgId()));
		}
		if(StringUtils.isNotBlank(userToRepairRecordListReq.getFixStatCode())){
			boolQueryBuilder.must(QueryBuilders.termQuery(UserToRepairRecord.IF_FINISH_FIX, userToRepairRecordListReq.getFixStatCode()));
		}
		
		Page<UserToRepairRecord> userToRepairRecordPage = this.getRepository().search(
		    boolQueryBuilder,new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, UserToRepairRecord.USER_TO_REPAIR_RECORD_ID)));
			List<UserToRepairRecordRowDto> resultList = new ArrayList<UserToRepairRecordRowDto>();
				for(UserToRepairRecord userToRepairRecord:userToRepairRecordPage.getContent()){
					UserToRepairRecordRowDto rowRecord = new UserToRepairRecordRowDto();
					
					Org org = SpringContextHelper.getBean(OrgService.class).getById(userToRepairRecord.getOrgId());
					rowRecord.setOrgNm(org.getOrgNm());
					SmartLock smartLock = SpringContextHelper.getBean(SmartLockService.class).getById(userToRepairRecord.getSmartLockId());
					rowRecord.setLockerHardwareCode(smartLock.getLockerHardwareCode());
					BeanUtils.copyProperties(userToRepairRecord, rowRecord);
					
					resultList.add(rowRecord);
				}
				return new ClientPage<UserToRepairRecordRowDto>((int)userToRepairRecordPage.getTotalElements(), offset / limit, limit, resultList);
			}

	/**
	 * @author lzh
	 */
	public UserToRepairRecord getLatestUserToRepairRecord(Integer UserId, String bikePlateNumber){
		UserToRepairRecord userToRepairRecord = null;;
		List<UserToRepairRecord> list = this.getRepository().findByUserIdAndBikePlateNumberOrderByToRepairTime(UserId, bikePlateNumber);
		if(list.isEmpty()){
			return null;
		} else {
			userToRepairRecord = list.get(0);
		}
		
		
		return userToRepairRecord;
	}
}
