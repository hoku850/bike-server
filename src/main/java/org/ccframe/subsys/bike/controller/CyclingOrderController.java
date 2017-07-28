package org.ccframe.subsys.bike.controller;

import java.io.IOException;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.dto.CyclingOrderClientPage;
import org.ccframe.subsys.bike.dto.CyclingOrderListReq;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.CYCLING_ORDER_BASE)
public class CyclingOrderController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public CyclingOrderRowDto getDtoById(@PathVariable(Global.ID_BINDER_ID) Integer cyclingOrderId) {
		return SpringContextHelper.getBean(CyclingOrderService.class).getCyclingOrderDtoById(cyclingOrderId);
	}
	
	@RequestMapping(value = "/"+ ControllerMapping.CYCLING_ORDER_FINISH + Global.ID_BINDER_PATH)
	public CyclingOrderRowDto getById(@PathVariable(Global.ID_BINDER_ID) Integer cyclingOrderId) {
		return SpringContextHelper.getBean(CyclingOrderService.class).finishGetById(cyclingOrderId);
	}

	@RequestMapping(value = "/"+ ControllerMapping.CYCLING_ORDER_FINISH, method=RequestMethod.POST)
	public int finish(@RequestBody CyclingOrderRowDto cyclingOrderRowDto){
		return SpringContextHelper.getBean(CyclingOrderService.class).finish(cyclingOrderRowDto);
	}

	@RequestMapping(value = ControllerMapping.CYCLING_ORDER_LIST, method = RequestMethod.POST)
	public CyclingOrderClientPage<CyclingOrderRowDto> findBikeTypeList(@RequestBody CyclingOrderListReq cyclingOrderListReq, int offset, int limit) {
		return SpringContextHelper.getBean(CyclingOrderSearchService.class).findList(cyclingOrderListReq, offset, limit);
	}
	
	@RequestMapping(value = ControllerMapping.CYCLING_ORDER_EXPORT, method=RequestMethod.POST)
	public String doExport(@RequestBody Integer orgId) throws IOException{
		return SpringContextHelper.getBean(CyclingOrderService.class).doExport(orgId);
	}
}

