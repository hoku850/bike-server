package org.ccframe.sdk.bike.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import antlr.collections.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;


@RestController
@RequestMapping(ControllerMapping.MEMBER_INDEX_BASE)
public class IndexMenuController {
	@RequestMapping(value = "getMenuData")
	@ResponseBody
	public Map<String, String> gerMenuData(Integer orgId){
		User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
//		User user = new User();
//		user.setUserId(50005);
		
		return SpringContextHelper.getBean(CyclingOrderService.class).getMenuData(user, orgId);
	}
}
