package org.ccframe.subsys.core.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.UserListReq;
import org.ccframe.subsys.core.dto.UserRowDto;
import org.ccframe.subsys.core.service.UserSearchService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ADMIN_USER_BASE)
public class AdminUserController{

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public User getUser(@PathVariable(Global.ID_BINDER_ID) Integer userId) {
		User user = SpringContextHelper.getBean(UserService.class).getById(userId);
		user.setUserPsw(Global.FAKE_PASSWORD);
		return user;
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer userId){
		SpringContextHelper.getBean(UserService.class).softDeleteById(userId);
	}

	@RequestMapping(value = ControllerMapping.ADMIN_USER_LIST, method = RequestMethod.POST)
	public ClientPage<UserRowDto> findUserList(@RequestBody UserListReq userListReq, int offset, int limit) {
		return SpringContextHelper.getBean(UserSearchService.class).findUserList(userListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody User adminUser){
		SpringContextHelper.getBean(UserService.class).saveOrUpdateAdminUser(adminUser);
	}
	
}
