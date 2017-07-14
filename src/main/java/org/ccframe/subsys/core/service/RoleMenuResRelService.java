package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Criterion;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.repository.RoleMenuResRelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleMenuResRelService extends BaseService<RoleMenuResRel, Integer, RoleMenuResRelRepository> {

//	@Transactional(readOnly = true)
//	public List<RoleMenuResRel> findByRoleIdList(Collection<Integer> roleIdList) {
//		List<Criterion> orList = new ArrayList<Criterion>();
//		for(Integer roleId: roleIdList){
//			orList.add(Restrictions.eq(RoleMenuResRel.ROLE_ID, roleId));
//		}
//		return this.getRepository().findAll(new Criteria<RoleMenuResRel>().add(Restrictions.or(orList.toArray(new Criterion[orList.size()]))));
//	}

}
