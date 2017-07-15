
package org.ccframe.subsys.bike.controller;

import java.io.IOException;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.dto.ChargeOrderListReq;
import org.ccframe.subsys.bike.dto.ChargeOrderRowDto;
import org.ccframe.subsys.bike.service.ChargeOrderSearchService;
import org.ccframe.subsys.bike.service.ChargeOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.CHARGE_ORDER_BASE)
public class ChargeOrderController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public ChargeOrder getById(@PathVariable(Global.ID_BINDER_ID) Integer chargeOrderId) {
		return SpringContextHelper.getBean(ChargeOrderService.class).getById(chargeOrderId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) Integer chargeOrderId){
		SpringContextHelper.getBean(ChargeOrderService.class).softDeleteById(chargeOrderId);
	}

	@RequestMapping(value = ControllerMapping.CHARGE_ORDER_LIST, method = RequestMethod.POST)
	public ClientPage<ChargeOrderRowDto> findChargeOrderList(@RequestBody ChargeOrderListReq chargeOrderListReq, int offset, int limit) {
		return SpringContextHelper.getBean(ChargeOrderSearchService.class).findChargeOrderList(chargeOrderListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody ChargeOrder chargeOrder){
		SpringContextHelper.getBean(ChargeOrderService.class).saveOrUpdateBikeType(chargeOrder);
	}
	
	@RequestMapping(value = ControllerMapping.CHARGE_ORDER_EXPORT, method=RequestMethod.POST)
	public String doExport(@RequestBody Integer orgId) throws IOException{
		return SpringContextHelper.getBean(ChargeOrderService.class).doExport(orgId);
	}
}

