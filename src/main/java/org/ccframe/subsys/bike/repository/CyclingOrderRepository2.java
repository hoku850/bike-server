package org.ccframe.subsys.bike.repository;

import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CyclingOrderRepository2 extends JpaRepository<CyclingOrder,java.lang.Integer>{
	
	@Query(value="SELECT SUM(c.orderAmmount) FROM CyclingOrder c WHERE c.orgId=:orgId and c.cyclingOrderStatCode=:cyclingOrderStatCode")
	Double countByOrgId(@Param("orgId") Integer orgId, @Param("cyclingOrderStatCode") String cyclingOrderStatCode);
	
}
