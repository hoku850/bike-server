package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.cache.CacheEvictBy;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.repository.OrgRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrgService extends BaseService<Org, Integer, OrgRepository> implements ILabelValueListProvider {

	@Transactional(readOnly = true)
	@CacheEvictBy({Org.class})
	@Cacheable(value=Global.EH_CACHE_AUTO_CACHE, cacheResolver = Global.EH_CACHE_RESOLVER)
	public Org getOrgByNm(String orgNm){
        return this.getByKey(Org.ORG_NM, orgNm);
    }

	@Override
	@Transactional(readOnly = true)
	public List<LabelValue> getLabelValueList(String beanName, String extraParam) {
		List<LabelValue> resultList = new ArrayList<LabelValue>();
		for(Org org: ((OrgService)SpringContextHelper.getBean(this.getClass())).listAll()){ //需要用spring容器方法以利用缓存
			resultList.add(new LabelValue(org.getOrgNm(), org.getOrgId()));
		}
		return resultList;
	}

}
