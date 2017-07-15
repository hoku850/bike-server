package org.ccframe.subsys.core.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.commons.filter.AdminLoginFilter;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MainFrameResp;
import org.ccframe.subsys.core.service.MenuResService;
import org.ccframe.subsys.core.service.MenuResUserHitService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.MAIN_FRAME_BASE)
public class MainFrameController{

	/**
	 * 管理员登录.
	 * @param orgId 登录的机构
	 * @param loginId
	 * @param userPsw
	 * @param validateCode
	 * @return 必须返回对象，否则jquey的json会出错
	 */
	@RequestMapping(value = ControllerMapping.MAIN_FRAME_DO_LOGIN, method = RequestMethod.POST)
	public BoolCodeEnum doLogin(Integer orgId, String loginId, String userPsw, String validateCode) {
		//检验验证码
        Object sessionCode = WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_VALIDATE_CODE);
        if(validateCode == null) {
        	throw new BusinessException(ResGlobal.ERRORS_LOGIN_VALIDATE_CODE, true);
        }
//        if(!validateCode.equals(sessionCode)){
//    		WebContextHolder.getSessionContextStore().removeServerValue(Global.SESSION_VALIDATE_CODE);
//        	throw new BusinessException(ResGlobal.ERRORS_LOGIN_VALIDATE_CODE, true);
//        }
        User user = SpringContextHelper.getBean(UserService.class).getByMultiLoginIdUserPswOrgId(loginId, userPsw, orgId);
		if(!BoolCodeEnum.fromCode(user.getIfAdmin()).boolValue()){ //管理员登录需判断用户必须是管理员
			throw new BusinessException(ResGlobal.ERRORS_LOGIN_PASSWORD, true);
		}
		AdminUser adminUser = AdminUser.create(user, orgId); 
		WebContextHolder.getSessionContextStore().setServerValue(Global.SESSION_LOGIN_ADMIN, adminUser);
		WebContextHolder.getSessionContextStore().removeServerValue(Global.SESSION_VALIDATE_CODE);
		return BoolCodeEnum.YES;
	}

	@RequestMapping(value = ControllerMapping.MAIN_FRAME_DO_LOGOUT)
	public String doLogout() throws IOException {
		WebContextHolder.getSessionContextStore().removeServerValue(Global.SESSION_LOGIN_ADMIN);
        return "\"" + AdminLoginFilter.getBackendLoginUri() + "\"";
	}

	@RequestMapping(value = ControllerMapping.MAIN_FRAME_ADMIN_MENU)
	public MainFrameResp mainFrame() {
		AdminUser adminUser = (AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN);
		MainFrameResp mainFrameDto = new MainFrameResp();
		mainFrameDto.setAdminUser(adminUser);
//		mainFrameDto.setTreeNodeTree(SpringContextHelper.getBean(UserService.class).getUserRoleMenuTree(adminUser.getUserId(), adminUser.getOrgId()));

		//医疗系统始终搜索总平台角色的菜单树
		mainFrameDto.setTreeNodeTree(SpringContextHelper.getBean(UserService.class).getUserRoleMenuTree(adminUser.getUserId(), Global.PLATFORM_ORG_ID));

//		List<MenuRes> fastMenuResList = SpringContextHelper.getBean(UserService.class).findFastMenuRes(adminUser.getUserId(), adminUser.getOrgId());
		//医疗系统始终搜索总平台角色的快速菜单
		List<MenuRes> fastMenuResList = SpringContextHelper.getBean(UserService.class).findFastMenuRes(adminUser.getUserId(), Global.PLATFORM_ORG_ID);
		Collections.sort(fastMenuResList, new Comparator<MenuRes>(){

			@Override
			public int compare(MenuRes o1, MenuRes o2) {
				return o1.getMenuResId() - o2.getMenuResId();
			}
			
		});
		mainFrameDto.setFastMenuRes(fastMenuResList);
	
		return mainFrameDto;
	}

	@RequestMapping(value = ControllerMapping.MAIN_FRAME_GET_MENU_RES)
	public MenuRes getMenuRes(Integer menuResId) {
		return SpringContextHelper.getBean(MenuResService.class).getById(menuResId);
	}

	@RequestMapping(value = ControllerMapping.MAIN_FRAME_UPDATE_PASSWORD)
	public void updatePassword(Integer userId, String userPsw, HttpServletRequest request) {
		SpringContextHelper.getBean(UserService.class).updatePassword(userId, userPsw);
	}

	@RequestMapping(value = ControllerMapping.MAIN_FRAME_MENU_HIT)
	public void menuHit(String viewResValue, HttpServletRequest request) {
		AdminUser adminUser = (AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN);
		SpringContextHelper.getBean(MenuResUserHitService.class).menuHit(adminUser.getUserId(), viewResValue);
	}

}

