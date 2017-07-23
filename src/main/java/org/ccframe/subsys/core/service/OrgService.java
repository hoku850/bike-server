package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.cache.CacheEvictBy;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.service.BikeTypeService;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.OrgRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgService extends BaseService<Org, Integer, OrgRepository> implements ILabelValueListProvider {

	@Transactional(readOnly = true)
	@CacheEvictBy({Org.class})
	@Cacheable(value=Global.EH_CACHE_AUTO_CACHE, cacheResolver = Global.EH_CACHE_RESOLVER)
	public Org getOrgByNm(String orgNm){
        return this.getByKey(Org.ORG_NM, orgNm);
    }

	@Override
	@Transactional(readOnly = true)
	public List<LabelValue> getLabelValueList(String beanName, String extraParam) {
		List<LabelValue> resultList = new ArrayList<LabelValue>();
		for(Org org: ((OrgService)SpringContextHelper.getBean(this.getClass())).listAll()){ //需要用spring容器方法以利用缓存
			resultList.add(new LabelValue(org.getOrgNm(), org.getOrgId()));
		}
		return resultList;
	}

	/**
	 * 
		任务描述
		1）新增运营商的时候要新增一个管理员，管理员登录ID要考虑重复的情况
		2）新增运营商要添加对应的默认单车类型
		3）新增运营商的管理员要有运营商的菜单（看AXURE）
	 * @param org
	 */
	@Transactional
	public User saveOrUpdateOrg(Org org) {
		
		// 新增操作下 同时新增 角色， 用户， 角色用户关系， 单车类型
		if (org.getOrgId() == null) {
			
			org.setIfDelete(BoolCodeEnum.NO.toCode());
			org.setRoleId(2); // TODO 先设管理员 后期再修改
			SpringContextHelper.getBean(this.getClass()).save(org);
			
			Role role = new Role();
			role.setRoleNm("系统管理员");
			role.setIfTemplate(BoolCodeEnum.NO.toCode());
			role.setOrgId(org.getOrgId());
			Role saveRole = SpringContextHelper.getBean(RoleService.class).save(role);
			
			org.setRoleId(saveRole.getRoleId());
			SpringContextHelper.getBean(this.getClass()).save(org);
			
			User user = new User();
			// 登陆ID的规则：  机构ID + 00X
			user.setLoginId(getLoginId("admin" + "001"));
			user.setUserNm("系统管理员");
			user.setUserPsw("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
			user.setIfAdmin(BoolCodeEnum.YES.toCode());
			user.setCreateDate(new Date());
			user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
			SpringContextHelper.getBean(UserService.class).save(user);
			
			UserRoleRel userRoleRel = new UserRoleRel();
			userRoleRel.setUserId(user.getUserId());
			userRoleRel.setRoleId(2);
			SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel);
			UserRoleRel userRoleRel2 = new UserRoleRel();
			userRoleRel2.setUserId(user.getUserId());
			userRoleRel2.setRoleId(role.getRoleId());
			SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel2);
			
			BikeType bikeType = new BikeType();
			bikeType.setOrgId(org.getOrgId());
			bikeType.setBikeTypeNm("标准单车");
			bikeType.setHalfhourAmmount(1.0);
			SpringContextHelper.getBean(BikeTypeService.class).save(bikeType);
			
			return user;
			
		} else {
			SpringContextHelper.getBean(this.getClass()).save(org);
			return null;
		}
	}
	
	public String getLoginId(String loginId){
		List<User> findByKey = SpringContextHelper.getBean(UserService.class).findByKey(User.LOGIN_ID, loginId);
		if (findByKey.size() == 0) {
			return loginId;
		} else {
			Integer id = Integer.parseInt(loginId.substring(loginId.length() - 3, loginId.length()))+ 1;
			if (id < 10) {
				loginId = "admin00" + id;
			} else if (id < 100) {
				loginId = "admin0" + id;
			} else if (id < 1000) {
				loginId = "admin" + id;
			}
			return getLoginId(loginId);
		}
	}

}
