package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;


/**
 * 树节点类型.
 * @author wj
 *
 */
public enum TreeNodeTypeCodeEnum  implements ICodeEnum{
	/**
	 * 虚拟根 0
	 */
	VIRTUAL_ROOT,
	/**
	 * 机构 1
	 */
	SYS_ORG,
	/**
	 * 菜单 2
	 */
	SYS_MENU_RES,
    /**
	 * 文章分类  3
	 */
	ARTICLE_CATEGORY,
    /**
	 * 地域  4
	 */
	ZONE;
	
	public static TreeNodeTypeCodeEnum fromCode(String code){
		try{
			return values()[Integer.parseInt(code)];
		}catch(Exception e){
			return null;
		}
	}
	
	public String toCode(){
		return Integer.toString(this.ordinal());
	}

	@Override
	public List<ICodeEnum> valueList() {
		List<ICodeEnum> result = new ArrayList<ICodeEnum>();
		for(TreeNodeTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}

}
