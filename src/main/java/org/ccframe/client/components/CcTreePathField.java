package org.ccframe.client.components;

import java.util.List;

import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.commons.TreeRootEnum;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.form.TriggerField;

/**
 * 下拉树的combobox.
 * rootId是子树树根的主键ID。
 * @author JIM
 *
 */
public class CcTreePathField extends TriggerField<Integer> implements HasExpandHandlers, HasCollapseHandlers{

	private int rootId;
	
	private String rootText;

	/**
	 * @param rootId 指定的树根节点，例如 Global.ARTICLE_CATEGORY_TREE_ROOT
	 */
	public CcTreePathField(){
		this(new CcTriggerTreeCell());
	}
	
	public CcTreePathField(CcTriggerTreeCell cell) {
		super(cell);
		this.setEditable(false);
	}

	@Override
	public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
		return addHandler(handler, CollapseEvent.getType());
	}

	@Override
	public HandlerRegistration addExpandHandler(ExpandHandler handler) {
		return addHandler(handler, ExpandEvent.getType());
	}

	@Override
	public CcTriggerTreeCell getCell() {
		return (CcTriggerTreeCell) super.getCell();
	}

	protected void expand() {
		getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
	}
	
	@Override
	public void setValue(Integer value) {
		if(value == null){
			super.setValue(rootId);
		}else{
			super.setValue(value);
			ClientManager.getTreeNodeClient().getNamePath(value, rootId, new RestCallback<List<String>>(){
				@Override
				public void onSuccess(Method method,final List<String> response) {
					getCell().getInputElement(CcTreePathField.this.getElement()).setValue(StringUtils.join(response, ">"));
				}
			});
		}
	}

	@Override
    public void onAttach(){
    	super.onAttach();
    	if(this.getValue().equals(rootId)){
    		getCell().getInputElement(CcTreePathField.this.getElement()).setValue(rootText);
    	}
	}

	public void setRootText(String rootText) {
		this.rootText = rootText;
	}

	public void setRootId(int rootId) {
		this.rootId = rootId;
		getCell().setRootId(rootId);
	}

	public void setRootEnum(TreeRootEnum treeRootEnum) {
		this.rootId = treeRootEnum.getTreeNodeId();
		getCell().setRootId(rootId);
	}

	/**
	 * 清空树缓存
	 */
	public void clearStore(){
		getCell().clearStore();
	}

	@Override
	public void clear(){
		this.setValue(null);
	}

	@Override
	public void reset(){
		clear();
	}
}
