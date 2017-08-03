package org.ccframe.subsys.core.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MemberAccountListReq;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
import org.ccframe.subsys.core.search.MemberAccountSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class MemberAccountSearchService extends BaseSearchService<MemberAccount, Integer, MemberAccountSearchRepository> {
	
	public List<MemberAccount> findByUserIdAndOrgIdAndAccountTypeCode(Integer userId, Integer orgId, String code){
		if(userId!=null && orgId!=null && code!=null) {
			return this.getRepository().findByUserIdAndOrgIdAndAccountTypeCode(userId, orgId, code);
		}
		
		return null;
	}

	public ClientPage<MemberAccountRowDto> findMemberAccountList(MemberAccountListReq memberAccountListReq, int offset, int limit) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 过滤 账户类型
		if (StringUtils.isNotBlank(memberAccountListReq.getAccountTypeCode())) {
			boolQueryBuilder.must(QueryBuilders.termQuery(MemberAccount.ACCOUNT_TYPE_CODE, memberAccountListReq.getAccountTypeCode()));
		}
		
		// 过滤 登陆ID
		if (StringUtils.isNotBlank(memberAccountListReq.getSearchText())) {
			User user = SpringContextHelper.getBean(UserService.class).getByKey(User.LOGIN_ID, memberAccountListReq.getSearchText());
			if (user != null) {
				boolQueryBuilder.must(QueryBuilders.termQuery(MemberAccount.USER_ID, user.getUserId()));
			} else {
				return new ClientPage<MemberAccountRowDto>(0, offset / limit, limit, new ArrayList<MemberAccountRowDto>());
			}
		}
		
		// 过滤机构
		Integer orgId = memberAccountListReq.getOrgId();
		if (orgId != null && orgId != Global.COMBOBOX_ALL_VALUE) {
			boolQueryBuilder.must(QueryBuilders.termQuery(MemberAccount.ORG_ID, orgId));
		}
				
		// 查询
		Page<MemberAccount> cPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, MemberAccount.MEMBER_ACCOUNT_ID))
		);
		
		List<MemberAccountRowDto> resultList = new ArrayList<MemberAccountRowDto>();
		for(MemberAccount memberAccount : cPage.getContent()){
			
			MemberAccountRowDto memberAccountRowDto = new MemberAccountRowDto();
			BeanUtils.copyProperties(memberAccount, memberAccountRowDto);
			// 查询用户信息
			User user = SpringContextHelper.getBean(UserSearchService.class).getById(memberAccount.getUserId());
			if (user != null) {
				memberAccountRowDto.setUserNm(user.getUserNm());
				memberAccountRowDto.setLoginId(user.getLoginId());
			}
			resultList.add(memberAccountRowDto);
		}
		return new ClientPage<MemberAccountRowDto>((int)cPage.getTotalElements(), offset / limit, limit, resultList);
	}
	
	/**
	 * @author zjm
	 */
	public AppPageDto getChargeAmount() {
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		
		List<MemberAccount> list1 = SpringContextHelper.getBean(MemberAccountSearchService.class)
				.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.PRE_DEPOSIT.toCode());
		
		String amount = "0.00";
		String ifChargeDeposit = BoolCodeEnum.NO.toCode();
		String deposit = "0.00";
		DecimalFormat df = new DecimalFormat("#0.00");  
		if(list1 != null && list1.size()>0){
			
			amount = df.format(list1.get(0).getAccountValue()) + "";
		}
		
		
		List<MemberAccount> list2 = SpringContextHelper.getBean(MemberAccountSearchService.class)
				.findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), user.getOrgId(), AccountTypeCodeEnum.DEPOSIT.toCode());
		if(list2!= null && list2.size()>0 && list2.get(0).getAccountValue()>0){
			ifChargeDeposit = BoolCodeEnum.YES.toCode();
			
			deposit = df.format(list2.get(0).getAccountValue())+"";
		}
		
		AppPageDto appPageDto = new AppPageDto();
		
		appPageDto.setAmount(amount);
		appPageDto.setDeposit(deposit);
		appPageDto.setIfChargeDeposit(ifChargeDeposit);
		
		return appPageDto;
	}

}
