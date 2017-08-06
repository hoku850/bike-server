package org.ccframe.sdk.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MEMBER_INDEX_BASE)
public class IndexMenuController {
	@RequestMapping(value = ControllerMapping.GET_MENU_DATA)
	@ResponseBody
	public AppPageDto gerMenuData(){
		MemberUser user = (MemberUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);

		return SpringContextHelper.getBean(CyclingOrderService.class).getMenuData(user);
	}
}
