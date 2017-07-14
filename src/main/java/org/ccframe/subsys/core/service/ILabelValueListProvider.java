package org.ccframe.subsys.core.service;

import java.util.List;

import org.ccframe.client.components.LabelValue;

public interface ILabelValueListProvider {
	/**
	 * @param beanName
	 * @param extraParam 用于级联等场景额外传递参数.
	 * @return
	 */
	List<LabelValue> getLabelValueList(String beanName, String extraParam);
}
