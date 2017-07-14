package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.repository.AgentRepository;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.service.ILabelValueListProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService extends BaseService<Agent,java.lang.Integer, AgentRepository> implements ILabelValueListProvider {

	@Override
	@Transactional(readOnly = true)
	public List<LabelValue> getLabelValueList(String beanName, String extraParam) {
		List<LabelValue> resultList = new ArrayList<LabelValue>();
		for(Agent agent: ((AgentService)SpringContextHelper.getBean(this.getClass())).listAll()){ //需要用spring容器方法以利用缓存
			resultList.add(new LabelValue(agent.getAgentNm(), agent.getAgentId()));
		}
		return resultList;
	}
	
	@Transactional
	public void saveOrUpdateAgent(Agent agent) {
		agent.setIfDelete(BoolCodeEnum.NO.toCode());
		agent.setRoleId(2); // TODO 先设管理员 后期再修改
		SpringContextHelper.getBean(this.getClass()).save(agent);
	}
}
