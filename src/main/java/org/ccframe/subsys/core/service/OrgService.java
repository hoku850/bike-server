package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
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
import org.ccframe.subsys.core.domain.entity.OrgUserRel;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.OrgRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgService extends BaseService<Org, Integer, OrgRepository> implements ILabelValueListProvider {

	private static final int TEMP_ORG_ID = 2;
	
	private static final String ADMIN_DEFAULT_NM = "默认管理员";
	private static final String LOGIN_NM = "admin";
	private static final String LOGIN_PASSWORD = "c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec"; //admin的sha-256
	
	private static final String DEFAULT_BIKE_TYPR_NM = "标准单车";
	private static final Double DEFAULT_BIKE_TYPR_HALFHOURAMMOUNT = 0.5;
	
	List<Integer> orgDefaultMenuRes = new ArrayList<Integer>();	
	
    @Value("${app.data.orgDefaultMenu:}")
    private void setOrgDefaultMenu(String orgDefaultMenu){
		for(String rowString: orgDefaultMenu.split(",")){
	    	if(StringUtils.isNotBlank(orgDefaultMenu)){
	    		orgDefaultMenuRes.add(Integer.parseInt(rowString.trim()));
	    	}
		}
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
		
		//TODO JIM 做成事件扩展的方式
		
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
			Role defaultRole = new Role();
			defaultRole.setRoleNm(ADMIN_DEFAULT_NM);
			defaultRole.setIfTemplate(BoolCodeEnum.NO.toCode());
			defaultRole.setOrgId(org.getOrgId());
			SpringContextHelper.getBean(RoleService.class).save(defaultRole);

			//创建角色默认菜单权限
			RoleMenuResRelService roleMenuResRelService = SpringContextHelper.getBean(RoleMenuResRelService.class);
			for(Integer menuResId: orgDefaultMenuRes){
				RoleMenuResRel roleMenuResRel = new RoleMenuResRel();
				roleMenuResRel.setRoleId(defaultRole.getRoleId());
				roleMenuResRel.setMenuResId(menuResId);
				roleMenuResRelService.save(roleMenuResRel);
			}

			//增加机构模板角色
			Role orgRole = new Role();
			orgRole.setRoleNm(org.getOrgNm());
			orgRole.setIfTemplate(BoolCodeEnum.YES.toCode());
			orgRole.setOrgId(org.getOrgId());
			SpringContextHelper.getBean(RoleService.class).save(orgRole);
			
			//创建机构默认菜单
			for(Integer menuResId: orgDefaultMenuRes){
				RoleMenuResRel roleMenuResRel = new RoleMenuResRel();
				roleMenuResRel.setRoleId(orgRole.getRoleId());
				roleMenuResRel.setMenuResId(menuResId);
				roleMenuResRelService.save(roleMenuResRel);
			}

			org.setRoleId(orgRole.getRoleId());
			SpringContextHelper.getBean(this.getClass()).save(org); //设置机构模板
			
			// 增加一个标准单车类型
			BikeType bikeType = new BikeType();
			bikeType.setOrgId(org.getOrgId());
			bikeType.setBikeTypeNm(DEFAULT_BIKE_TYPR_NM);
			bikeType.setHalfhourAmmount(DEFAULT_BIKE_TYPR_HALFHOURAMMOUNT);
			SpringContextHelper.getBean(BikeTypeService.class).save(bikeType);
			
			// 增加默认管理员 如果找到99都被占用，那么就不返回新建管理员信息（新增完毕不会有弹框）
			String loginId = createLoginId(org.getOrgId(), Global.TRY_START_SEQ);
			if (loginId != null) {
				User user = new User();
				user.setLoginId(loginId);
				// 管理员名称使用运营商名称+"默认管理员"例如叫："小蓝单车默认管理员"
				user.setUserNm(org.getOrgNm() + ADMIN_DEFAULT_NM);
				user.setUserPsw(LOGIN_PASSWORD);
				user.setIfAdmin(BoolCodeEnum.YES.toCode());
				user.setCreateDate(new Date());
				user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
				SpringContextHelper.getBean(UserService.class).save(user);
				
				// 用户机构关系
				OrgUserRel orgUserRel = new OrgUserRel();
				orgUserRel.setOrgId(org.getOrgId());
				orgUserRel.setUserId(user.getUserId());
				SpringContextHelper.getBean(OrgUserRelService.class).save(orgUserRel);

				// 用户角色关系
				UserRoleRel userRoleRel = new UserRoleRel();
				userRoleRel.setRoleId(defaultRole.getRoleId());
				userRoleRel.setUserId(user.getUserId());
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
		String loginId = LOGIN_NM + String.format("%02d%02d", orgId, num);
		User user = SpringContextHelper.getBean(UserService.class).getByKey(User.LOGIN_ID, loginId);
		if (user == null) {
			return loginId;
		} else {
			num = Integer.parseInt(loginId.substring(loginId.length() - 2, loginId.length()))+ 1;
			if (num > Global.TRY_END_SEQ) return null;
			else return createLoginId(orgId, num);
		}
	}

}
