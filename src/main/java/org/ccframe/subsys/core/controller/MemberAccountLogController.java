
package org.ccframe.subsys.core.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.dto.MemberAccountLogListReq;
import org.ccframe.subsys.core.dto.MemberAccountLogRowDto;
import org.ccframe.subsys.core.service.MemberAccountLogSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_ACCOUNT_LOG_BASE)
public class MemberAccountLogController{
	
	@RequestMapping(value = ControllerMapping.MEMBER_ACCOUNT_LIST, method = RequestMethod.POST)
	public ClientPage<MemberAccountLogRowDto> findList(@RequestBody MemberAccountLogListReq memberAccountLogListReq, int offset, int limit) {
		return SpringContextHelper.getBean(MemberAccountLogSearchService.class).findMemberAccountLogList(memberAccountLogListReq, offset, limit);
	}
}

