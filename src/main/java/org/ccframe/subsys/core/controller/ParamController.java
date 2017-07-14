package org.ccframe.subsys.core.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.Param;
import org.ccframe.subsys.core.dto.ParamRowDto;
import org.ccframe.subsys.core.service.ParamService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ADMIN_PARAM_BASE)
public class ParamController{
	
	@RequestMapping(value = ControllerMapping.ADMIN_PARAM_PREFERENCE_TEXT)
	public List<String> getPreferenceText(String paramInnerCoding) {
		return Arrays.asList(SpringContextHelper.getBean(ParamService.class).getParamValue(paramInnerCoding).split(Global.PREFERENCE_TEXT_SPLIT_STR));
	}

	@RequestMapping(value = ControllerMapping.ADMIN_PARAM_PREFERENCE_TEXT, method = RequestMethod.POST)
	public void setPreferenceText(String paramInnerCoding, @RequestBody List<String> preferenceTextList) {
		SpringContextHelper.getBean(ParamService.class).setParamValue(paramInnerCoding, StringUtils.join(preferenceTextList, Global.PREFERENCE_TEXT_SPLIT_STR));
	}
	
	@RequestMapping(value = ControllerMapping.ADMIN_PARAM_LIST)
	public List<ParamRowDto> findUserList(String paramNameOrInnerCode) {
		return SpringContextHelper.getBean(ParamService.class).findUserListByNameOrInnerCode(paramNameOrInnerCode);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH)
	public Param getById(@PathVariable(Global.ID_BINDER_ID) Integer paramId){
		return SpringContextHelper.getBean(ParamService.class).getById(paramId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.POST)
	public void setParamValue(@PathVariable(Global.ID_BINDER_ID) Integer paramId, String paramValue){
		SpringContextHelper.getBean(ParamService.class).setParamValue(paramId, paramValue);
	}

}
