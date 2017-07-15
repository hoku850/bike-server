
package org.ccframe.subsys.bike.controller;

import java.io.IOException;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockListReq;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.ccframe.subsys.bike.service.SmartLockSearchService;
import org.ccframe.subsys.bike.service.SmartLockService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.SMART_LOCK_BASE)
public class SmartLockController{
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public SmartLock getSmartLock(@PathVariable(Global.ID_BINDER_ID) Integer smartLockId) {
		return SpringContextHelper.getBean(SmartLockService.class).getById(smartLockId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer smartLockId){
		SpringContextHelper.getBean(SmartLockService.class).myDeleteById(smartLockId);
	}

	@RequestMapping(value = ControllerMapping.SMART_LOCK_LIST, method = RequestMethod.POST)
	public ClientPage<SmartLockRowDto> findSmartLockList(@RequestBody SmartLockListReq smartLockListReq, int offset, int limit) {
		return SpringContextHelper.getBean(SmartLockSearchService.class).findSmartLockList(smartLockListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody SmartLock smartLock){
		SpringContextHelper.getBean(SmartLockService.class).saveOrUpdateSmartLock(smartLock);
	}
	
	@RequestMapping(value = ControllerMapping.SMART_LOCK_EXPORT, method=RequestMethod.POST)
	public String doExport(@RequestBody Integer orgId) throws IOException{
		return SpringContextHelper.getBean(SmartLockService.class).doExport(orgId);
	}
	
	@RequestMapping(value = ControllerMapping.SMART_LOCK_DESERT, method=RequestMethod.POST)
	public void doDesert(@RequestBody SmartLockRowDto selectedRow){
		SpringContextHelper.getBean(SmartLockService.class).doDesert(selectedRow);
	}
}

