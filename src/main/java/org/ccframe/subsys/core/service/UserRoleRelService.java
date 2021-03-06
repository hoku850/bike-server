package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.TreeNodeTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.OrgUserRel;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.UserRoleRelRepository;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.aria.client.Role;

@Service
public class UserRoleRelService extends BaseService<UserRoleRel, Integer, UserRoleRelRepository> {

	@Transactional(readOnly = true)
	public List<Integer> getRoleSysTreeNodeIdList(Integer roleId) {
		List<RoleMenuResRel> roleMenuResRelList = SpringContextHelper.getBean(RoleMenuResRelService.class).findByKey(RoleMenuResRel.ROLE_ID, roleId);
		List<Integer> menuResIdList = new ArrayList<Integer>();
		for(RoleMenuResRel roleMenuResRel: roleMenuResRelList){
			menuResIdList.add(roleMenuResRel.getMenuResId());
		}
		return SpringContextHelper.getBean(TreeNodeService.class).findTreeNodeIdListBySysObjectIdList(menuResIdList, TreeNodeTypeCodeEnum.SYS_MENU_RES);
	}

	@Transactional
	public void saveRoleMenuList(Integer roleId, List<Integer> sysMenuResIdList) {
		RoleMenuResRelService roleMenuResRelService = SpringContextHelper.getBean(RoleMenuResRelService.class);
		
		List<RoleMenuResRel> roleMenuResRelList = roleMenuResRelService.findByKey(RoleMenuResRel.ROLE_ID, roleId);
		List<Integer> oldMenuResList = new ArrayList<Integer>();
		for(RoleMenuResRel roleMenuResRel: roleMenuResRelList){
			oldMenuResList.add(roleMenuResRel.getMenuResId());
		}
		//需要删除的为 原集合-新集合
		Collection<Integer> deleteList = CollectionUtils.subtract(oldMenuResList, sysMenuResIdList);
		//需要添加的为 新集合-原集合
		Collection<Integer> addList = CollectionUtils.subtract(sysMenuResIdList, oldMenuResList);
		//删除
		for(Integer sysMenuResId: deleteList){
			List<RoleMenuResRel> deleteRoleMenuResRelList = roleMenuResRelService.findByKey(RoleMenuResRel.MENU_RES_ID, sysMenuResId);
			for(RoleMenuResRel roleMenuResRel: deleteRoleMenuResRelList){
//				if(SpringContextHelper.getBean(RoleService.class).getById(roleMenuResRel.getRoleId()).getIfTemplate().equals(BoolCodeEnum.NO.toCode())) {
				if(roleMenuResRel.getRoleId() == roleId) {
					roleMenuResRelService.delete(roleMenuResRel);
				}
			}
		}
		//添加
		for(Integer addResId: addList){
			RoleMenuResRel roleMenuResRel = new RoleMenuResRel();
			roleMenuResRel.setMenuResId(addResId);
			roleMenuResRel.setRoleId(roleId);
			roleMenuResRelService.save(roleMenuResRel);
		}
	}

	@Transactional
	public void batchDeleteRoleUserRel(Integer roleId, List<Integer> userIdList) {
		List<UserRoleRel> userRoleRelList = findByKey(UserRoleRel.ROLE_ID, roleId);
		for(UserRoleRel userRoleRel: userRoleRelList){
			if(userIdList.contains(userRoleRel.getUserId())){
				SpringContextHelper.getBean(UserRoleRelService.class).delete(userRoleRel);
			}
		}
	}

	@Transactional
	public List<String> batchAddRoleUserRel(Integer roleId, List<String> loginIdList) {
		List<UserRoleRel> userRoleRelList = findByKey(UserRoleRel.ROLE_ID, roleId);
		List<Integer> roleUserIdList = new ArrayList<Integer>();
		for(UserRoleRel userRoleRel: userRoleRelList){
			roleUserIdList.add(userRoleRel.getUserId());
		}

		List<String> failList = new ArrayList<String>();
		UserService userService = SpringContextHelper.getBean(UserService.class);
		OrgUserRelSearchService orgUserRelSearchService = SpringContextHelper.getBean(OrgUserRelSearchService.class);
		for(String loginId: loginIdList){
			User user = userService.getByLoginId(loginId);
			if(user == null || !BoolCodeEnum.fromCode(user.getIfAdmin()).boolValue()){ //不是管理员
				failList.add(loginId);
			}else{
				Set<Integer> ordIdSet = new HashSet<Integer>();
				for(OrgUserRel orgUserRel: orgUserRelSearchService.findByKey(OrgUserRel.USER_ID, user.getUserId(), new Order[]{})){
					ordIdSet.add(orgUserRel.getOrgId());
				}
				if(!ordIdSet.contains(SpringContextHelper.getBean(RoleService.class).getById(roleId).getOrgId())){ //如果角色的机构用户无法访问
					failList.add(loginId); //就是不能把别人的管理员添加到本机构，除非该管理员支持多机构登录
				}else{
					if(!roleUserIdList.contains(user.getUserId())){ //未添加关联，待添加
						UserRoleRel userRoleRel = new UserRoleRel();
						userRoleRel.setRoleId(roleId);
						userRoleRel.setUserId(user.getUserId());
						save(userRoleRel);
					}
				}
			}
		}
		return failList;
	}
}
