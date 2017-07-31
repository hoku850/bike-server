package org.ccframe.subsys.core.controller;

import java.util.List;

import javax.ws.rs.PathParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.service.RoleSearchService;
import org.ccframe.subsys.core.service.RoleService;
import org.ccframe.subsys.core.service.UserRoleRelService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ADMIN_ROLE_BASE)
public class RoleController{

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public Role getById(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer roleId){
		return SpringContextHelper.getBean(RoleService.class).getById(roleId);
	}

	/**
	 * 下传登录机构下非模板的角色.
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_LIST)
	public List<Role> findRoleList(Integer orgId) {
		//AdminUser adminUser = (AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN);
		return SpringContextHelper.getBean(RoleService.class).getOrgRoleList(orgId);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody Role role){
		AdminUser adminUser = (AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN);
		Role newRole = SpringContextHelper.getBean(RoleService.class).saveOrUpdate(role, adminUser.getOrgId());
		//必须在保证事务正常执行的情况下，才提交索引
		SpringContextHelper.getBean(RoleSearchService.class).save(newRole);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer roleId){
		SpringContextHelper.getBean(RoleService.class).deleteById(roleId);
	}
	
	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_REF_USER_COUNT)
	Integer getRefUserCount(Integer roleId){
		return SpringContextHelper.getBean(UserRoleRelService.class).findByKey(UserRoleRel.ROLE_ID, roleId).size();
	}

	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_SYS_TREE_NODE_ID_LIST)
	List<Integer> findRoleSysTreeNodeIdList(Integer roleId){
		return SpringContextHelper.getBean(UserRoleRelService.class).getRoleSysTreeNodeIdList(roleId);
	}
	
	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_SAVE_ROLE_MENU_LIST, method=RequestMethod.POST)
	void saveRoleMenuList(@RequestBody List<Integer> sysMenuResIdList, @RequestParam Integer roleId){
		SpringContextHelper.getBean(UserRoleRelService.class).saveRoleMenuList(roleId, sysMenuResIdList);
	}

	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_USER_LIST)
	List<User> findRoleUserList(Integer roleId){
		return SpringContextHelper.getBean(UserService.class).findByRoleId(roleId);
	}

	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_BATCH_DELETE_USER_ROLE_REL, method=RequestMethod.POST)
	void batchDeleteRoleUserRel(@RequestParam Integer roleId, @RequestBody List<Integer> userIdList){
		SpringContextHelper.getBean(UserRoleRelService.class).batchDeleteRoleUserRel(roleId, userIdList);
	}

	@RequestMapping(value = ControllerMapping.ADMIN_ROLE_BATCH_ADD_USER_ROLE_REL, method=RequestMethod.POST)
	List<String> batchAddRoleUserRel(@RequestParam Integer roleId,@RequestBody List<String> loginIdList){
		return SpringContextHelper.getBean(UserRoleRelService.class).batchAddRoleUserRel(roleId, loginIdList);
	}

}
