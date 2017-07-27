package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ccframe.client.ResGlobal;
import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.service.BikeTypeService;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.OrgRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgService extends BaseService<Org, Integer, OrgRepository> implements ILabelValueListProvider {

	private static final int TEMP_ORG_ID = 2;
	
	private static final String ADMIN_DEFAULT_NM = "默认管理员";
	private static final String LOGIN_NM = "admin";
	
	private static final String DEFAULT_BIKE_TYPR_NM = "标准单车";
	private static final Double DEFAULT_BIKE_TYPR_HALFHOURAMMOUNT = 0.5;
	
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
			
			// 检测是否已存在同名的运营商
			List<Org> list = SpringContextHelper.getBean(OrgService.class).findByKey(Org.ORG_NM, org.getOrgNm());
			if (list.size() != 0) {
				throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"运营商名称重复，请重新输入！"});
			}
			
			org.setIfDelete(BoolCodeEnum.NO.toCode());
			org.setRoleId(TEMP_ORG_ID);
			SpringContextHelper.getBean(this.getClass()).save(org);
			
			// 增加一个角色
			Role role = new Role();
			role.setRoleNm(ADMIN_DEFAULT_NM);
			role.setIfTemplate(BoolCodeEnum.NO.toCode());
			role.setOrgId(org.getOrgId());
			role = SpringContextHelper.getBean(RoleService.class).save(role);
			
			org.setRoleId(role.getRoleId());
			SpringContextHelper.getBean(this.getClass()).save(org);
			
			// 增加一个标准单车类型
			BikeType bikeType = new BikeType();
			bikeType.setOrgId(org.getOrgId());
			bikeType.setBikeTypeNm(DEFAULT_BIKE_TYPR_NM);
			bikeType.setHalfhourAmmount(DEFAULT_BIKE_TYPR_HALFHOURAMMOUNT);
			SpringContextHelper.getBean(BikeTypeService.class).save(bikeType);
			
			// 增加默认管理员 如果找到99都被占用，那么就不返回新建管理员信息（新增完毕不会有弹框）
			String loginId = createLoginId(org.getOrgId(), 1);
			if (loginId != null) {
				User user = new User();
				user.setLoginId(loginId);
				// 管理员名称使用运营商名称+"默认管理员"例如叫："小蓝单车默认管理员"
				user.setUserNm(org.getOrgNm() + ADMIN_DEFAULT_NM);
				user.setUserPsw("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
				user.setIfAdmin(BoolCodeEnum.YES.toCode());
				user.setCreateDate(new Date());
				user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
				SpringContextHelper.getBean(UserService.class).save(user);
				
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setUserId(user.getUserId());
				userRoleRel.setRoleId(TEMP_ORG_ID);
				SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel);
				userRoleRel = new UserRoleRel();
				userRoleRel.setUserId(user.getUserId());
				userRoleRel.setRoleId(role.getRoleId());
				SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel);
				
				return user;
			}
			return null;
			
		// 运营商信息修改
		} else { 
			SpringContextHelper.getBean(this.getClass()).save(org);
			return null;
		}
	}
	
	/**
	 * 添加机构默认的管理员采用这样的规则：admin+2位机构编号+2位机构用户序列号
	 */
	private String createLoginId(Integer orgId, Integer num) {
		String loginId = LOGIN_NM + String.format("%02d", orgId) + String.format("%02d", num);
		User user = SpringContextHelper.getBean(UserService.class).getByKey(User.LOGIN_ID, loginId);
		if (user == null) {
			return loginId;
		} else {
			num = Integer.parseInt(loginId.substring(loginId.length() - 2, loginId.length()))+ 1;
			if (num > 99) return null;
			else return createLoginId(orgId, num);
		}
	}

}
