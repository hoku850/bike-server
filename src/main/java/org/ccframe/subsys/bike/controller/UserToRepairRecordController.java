package org.ccframe.subsys.bike.controller;
import org.ccframe.client.ControllerMapping;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.dto.UserToRepairRecordListReq;
import org.ccframe.subsys.bike.dto.UserToRepairRecordRowDto;
import org.ccframe.subsys.bike.service.UserToRepairRecordSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.USER_TO_REPAIR_RECORD_BASE)
public class UserToRepairRecordController{
	
	@RequestMapping(value = ControllerMapping.USER_TO_REPAIR_RECORD_LIST, method = RequestMethod.POST)
	public ClientPage<UserToRepairRecordRowDto> findUserToRepairRecordList(@RequestBody UserToRepairRecordListReq userToRepairRecordListReq, int offset, int limit) {
		return SpringContextHelper.getBean(UserToRepairRecordSearchService.class).findUserToRepairRecordList(userToRepairRecordListReq, offset, limit);
	}
}

