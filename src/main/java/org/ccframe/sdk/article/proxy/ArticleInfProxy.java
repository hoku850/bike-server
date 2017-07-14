package org.ccframe.sdk.article.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.ccframe.subsys.article.service.ArticleInfSearchService;
import org.ccframe.subsys.article.service.ArticleInfService;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.TreeNodeSearchService;
import org.ccframe.subsys.core.service.TreeNodeService;
import org.ccframe.subsys.core.service.UserService;

import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;


public class ArticleInfProxy extends ArticleInf{
	private static final long serialVersionUID = 6084742952845341539L;

	public User getReleaseUser(){
		return SpringContextHelper.getBean(UserService.class).getById(this.getReleaseUserId());
	}
	
	public List<ArticleInfRowDto> getArticleInfRowDtoList(int topN){
		return SpringContextHelper.getBean(ArticleInfSearchService.class).findArticleInfByArticleCategoryIdTopN(this.getArticleCategoryId(), topN);
	}
	
	//楚钦：treeNode.getTreeNodeNm().equals("文章分类")写死了，可能有问题
	public List<TreeNodeTree> getArticleInfPath(){
		List<TreeNodeTree> path = new ArrayList<TreeNodeTree>();
		int upperTreeNodeId = this.getArticleCategoryId();
		while(true){
			TreeNodeTree treeNode = SpringContextHelper.getBean(TreeNodeSearchService.class).getTree(upperTreeNodeId, null);
			if(treeNode == null){
				break;
			}
			if(treeNode.getTreeNodeNm().equals("文章分类")){
				break;
			}
			path.add(treeNode);
			upperTreeNodeId = treeNode.getUpperTreeNodeId();
		}
		Collections.reverse(path);
		return path;
	}
	
	public String getReleaseTimeYYYY_MM_DDStr() {
		return this.getReleaseTime() == null ? null : UtilDateTimeClient.convertDateToString(this.getReleaseTime());
	}
}
