package org.ccframe.subsys.core.controller;

import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.service.ILabelValueListProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.SIMPLE_LABEL_VALUE_BASE)
public class SimpleLabelValueController {
	
	@RequestMapping
	public List<LabelValue> getLabelValueList(String beanName, String extraParam){
		return ((ILabelValueListProvider)SpringContextHelper.getBean(beanName)).getLabelValueList(beanName, extraParam);
	}
}
