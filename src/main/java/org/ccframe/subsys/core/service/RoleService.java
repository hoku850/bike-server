package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService extends BaseService<Role, Integer, RoleRepository> {

	@Override
	@Transactional
	public void delete(Role data) { //删除时，将删除全部的角色菜单关系及角色用户关系
		UserRoleRelService userRoleRelService = SpringContextHelper.getBean(UserRoleRelService.class);
		RoleMenuResRelService roleMenuResRelService = SpringContextHelper.getBean(RoleMenuResRelService.class);

		for(UserRoleRel userRoleRel: userRoleRelService.findByKey(UserRoleRel.ROLE_ID, data.getRoleId())){
			userRoleRelService.delete(userRoleRel);
		}

		
		for(RoleMenuResRel roleMenuResRel: roleMenuResRelService.findByKey(RoleMenuResRel.ROLE_ID, data.getRoleId()) ){
			roleMenuResRelService.delete(roleMenuResRel);
		}
		
		super.delete(data);
	}

	/**
	 * 提醒用户，该角色已经被XX个管理员使用，您确定要删除吗.
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public int getRefUserCount(Integer id){
		return SpringContextHelper.getBean(UserRoleRelService.class).findByKey(UserRoleRel.ROLE_ID, id).size();
	}

	@Transactional
	public Role saveOrUpdate(Role data, int sessionOrgId) {
		if(data.getIfTemplate() == null){
			data.setIfTemplate(BoolCodeEnum.NO.toCode());
		}
		if(data.getOrgId() == null){
			data.setOrgId(sessionOrgId);
		}
		return SpringContextHelper.getBean(this.getClass()).save(data);
	}

	@Override
	@Transactional
	public void deleteById(Integer roleId) {
		UserRoleRelService userRoleRelService = SpringContextHelper.getBean(UserRoleRelService.class);
		List<UserRoleRel> userRoleRelList = userRoleRelService.findByKey(UserRoleRel.ROLE_ID, roleId);
		for(UserRoleRel userRoleRel: userRoleRelList){
			userRoleRelService.delete(userRoleRel);
		}
		
		RoleMenuResRelService roleMenuResRelService = SpringContextHelper.getBean(RoleMenuResRelService.class);
		List<RoleMenuResRel> roleMenuResRelList = roleMenuResRelService.findByKey(RoleMenuResRel.ROLE_ID, roleId);
		for(RoleMenuResRel roleMenuResRel: roleMenuResRelList){
			roleMenuResRelService.delete(roleMenuResRel);
		}

		super.deleteById(roleId);
	}

	@Transactional(readOnly = true)
	public List<Role> getOrgRoleList(Integer orgId) {
		List<Role> roleList = SpringContextHelper.getBean(RoleService.class).findByKey(Role.ORG_ID, orgId);
		List<Role> resultList = new ArrayList<Role>();
		for(Role role: roleList){
			if(!BoolCodeEnum.fromCode(role.getIfTemplate()).boolValue()){
				resultList.add(role);
			}
		}
		return resultList;
	}
}
