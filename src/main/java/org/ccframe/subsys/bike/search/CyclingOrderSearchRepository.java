package org.ccframe.subsys.bike.search;

import java.util.List;

import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

public interface CyclingOrderSearchRepository extends ElasticsearchRepository<CyclingOrder, Integer>{

	List<CyclingOrder> findByUserIdAndOrgIdOrderByEndTimeDesc(Integer userId,
			Integer orgId);

	List<CyclingOrder> findByUserIdAndOrgIdOrderByStartTimeDesc(Integer userId,
			Integer orgId);

	List<CyclingOrder> findBySmartLockIdAndCyclingOrderStatCodeOrderByStartTimeDesc(Integer smartLockId, String code);	
	
	List<CyclingOrder> findBySmartLockIdOrderByStartTimeDesc(Integer smartLockId);

	List<CyclingOrder> findByUserIdAndOrgIdAndCyclingOrderStatCode(Integer userId, Integer orgId, String code);	
	
	@Query(value="SELECT SUM(c.orderAmmount) FROM CyclingOrder c WHERE c.orgId=:orgId and c.cyclingOrderStatCode=:cyclingOrderStatCode")
	Double countByOrgId(@Param("orgId") Integer orgId, @Param("cyclingOrderStatCode") String cyclingOrderStatCode);
	
	Integer countByOrgIdAndCyclingOrderStatCode(Integer orgId, String cyclingOrderStatCode);
}
