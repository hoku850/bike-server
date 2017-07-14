
package org.ccframe.subsys.core.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.dto.MemberAccountListReq;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
import org.ccframe.subsys.core.service.MemberAccountSearchService;
import org.ccframe.subsys.core.service.MemberAccountService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_ACCOUNT_BASE)
public class MemberAccountController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public MemberAccountRowDto getById(@PathVariable(Global.ID_BINDER_ID) Integer memberAccountId) {
		return SpringContextHelper.getBean(MemberAccountService.class).getMemberAccountRowDto(memberAccountId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) Integer memberAccountId){
		SpringContextHelper.getBean(MemberAccountService.class).softDeleteById(memberAccountId);
	}

	@RequestMapping(value = ControllerMapping.MEMBER_ACCOUNT_LIST, method = RequestMethod.POST)
	public ClientPage<MemberAccountRowDto> findList(@RequestBody MemberAccountListReq memberAccountListReq, int offset, int limit) {
		return SpringContextHelper.getBean(MemberAccountSearchService.class).findMemberAccountList(memberAccountListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody MemberAccountRowDto memberAccountRowDto){
		SpringContextHelper.getBean(MemberAccountService.class).saveOrUpdateMemberAccount(memberAccountRowDto);
	}
}

