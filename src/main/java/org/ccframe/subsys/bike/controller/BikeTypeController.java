
package org.ccframe.subsys.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.dto.BikeTypeListReq;
import org.ccframe.subsys.bike.dto.BikeTypeRowDto;
import org.ccframe.subsys.bike.service.BikeTypeSearchService;
import org.ccframe.subsys.bike.service.BikeTypeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.BIKE_TYPE_BASE)
public class BikeTypeController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public BikeType getById(@PathVariable(Global.ID_BINDER_ID) Integer bikeTypeId) {
		return SpringContextHelper.getBean(BikeTypeSearchService.class).getById(bikeTypeId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) Integer bikeTypeId){
		SpringContextHelper.getBean(BikeTypeService.class).deleteBikeTypeById(bikeTypeId);
	}

	@RequestMapping(value = ControllerMapping.BIKE_TYPE_LIST, method = RequestMethod.POST)
	public ClientPage<BikeTypeRowDto> findBikeTypeList(@RequestBody BikeTypeListReq bikeTypeListReq, int offset, int limit) {
		return SpringContextHelper.getBean(BikeTypeSearchService.class).findBikeTypeList(bikeTypeListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody BikeType bikeType){
		AdminUser adminUser = WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN);
		if(adminUser.getOrgId() != Global.PLATFORM_ORG_ID && bikeType.getOrgId() != adminUser.getOrgId()){ //非平台只允许允许修改自己的单车价格定义
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{""}); //权限异常
		}

		SpringContextHelper.getBean(BikeTypeService.class).saveOrUpdateBikeType(bikeType);
	}
}