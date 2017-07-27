package org.ccframe.sdk.bike.controller;

import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.ToRepairReason;
import org.ccframe.subsys.bike.service.ToRepairReasonService;
import org.ccframe.subsys.bike.service.UserToRepairRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sencha.gxt.legacy.client.mvc.Controller;

@RestController
@RequestMapping(ControllerMapping.MEMBER_FIXREPORT_BASE)
public class FixReportController{
	
	@RequestMapping(value = ControllerMapping.GET_FIX_REASON)
	@ResponseBody
	public List<ToRepairReason> getFixReason(String posCode) {
		return SpringContextHelper.getBean(ToRepairReasonService.class).getFixReason(posCode);
	}
	
	@RequestMapping(value = ControllerMapping.SAVE_REPAIR_RECORD)
	@ResponseBody
	public String saveRepairRecord(String posCode, Integer reasonID, String position) {
 
		return SpringContextHelper.getBean(UserToRepairRecordService.class).saveRepairRecord(posCode, reasonID, position);
	}
}
