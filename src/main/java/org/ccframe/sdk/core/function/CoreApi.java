package org.ccframe.sdk.core.function;

import java.util.List;

import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.EnumFromCodeUtil;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.ccframe.subsys.core.service.OrgService;
import org.ccframe.subsys.core.service.ParamService;
import org.ccframe.subsys.core.service.TreeNodeSearchService;

public class CoreApi {

    public static List<TreeNode> getParentNodes(Integer treeNodeId) throws BusinessException {
//    	return SpringContextHelper.getBean(TreeNodeService.class).getParentNodes(treeNodeId);
		//TODO 待修复
    	return null;
    }
    
    //chuqin:根据id获取一个树节点
    public static TreeNodeTree getTree(int treeNodeId){
    	return SpringContextHelper.getBean(TreeNodeSearchService.class).getTree(treeNodeId, null);
    }

    public static String getParamValue(String paramInnerCoding) throws BusinessException {
    	return SpringContextHelper.getBean(ParamService.class).getParamValue(paramInnerCoding);
    }
    
    public static Enum enumFromCode(String enumClassName, String code){
    	return EnumFromCodeUtil.enumFromCode(enumClassName, code);
    }

    public static List<Org> getOrgList(){
    	return SpringContextHelper.getBean(OrgService.class).listAll();
    }
}
