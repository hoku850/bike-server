package org.ccframe.subsys.core.repository;

import java.util.List;

import org.ccframe.commons.base.BaseRepository;
import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TreeNodeRepository extends BaseRepository<TreeNode, Integer> {
	
	@Query("select t." + TreeNode.TREE_NODE_ID + " from TreeNode t where " + TreeNode.SYS_OBJECT_ID + " in :sysObjectIdList and treeNodeTypeCode = :treeNodeTypeCode")
	List<Integer> findTreeNodeIdListBySysObjectIdList(@Param("sysObjectIdList") List<Integer> sysObjectIdList, @Param("treeNodeTypeCode") String treeNodeTypeCode);

}
