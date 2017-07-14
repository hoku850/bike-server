package org.ccframe.client.components;

import java.text.ParseException;
import java.util.List;

import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeUtil;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * TODO 不需要每次都reload，只有在重新打开窗体或页面才需要reload
 * 
 * @author JIM
 *
 */
public class CcTriggerTreeCell extends TriggerFieldCell<Integer>{

	private Tree<TreeNodeTree, String> tree;
	private TreeStore<TreeNodeTree> treeStore;	

	private Menu menu;
	private boolean expanded;

	private int rootId;
	private Integer selecionTreeNodeId;
	
	private static CcTreeCellPropertyEditor ccTreeCellPropertyEditor;
	
	public CcTriggerTreeCell(){
		super();
		if(ccTreeCellPropertyEditor == null){
			ccTreeCellPropertyEditor = new CcTreeCellPropertyEditor(); 
		}
		this.setPropertyEditor(ccTreeCellPropertyEditor);
	}
	
	private void createTree(){
		if(tree == null){
			treeStore = new TreeStore<TreeNodeTree>(TreeUtil.treeNodeTreeKeyProvider);
			tree = new Tree<TreeNodeTree, String>(treeStore, TreeUtil.treeValueProvider);
			tree.setHeight(200);
			menu = new Menu();
			menu.setBorders(true);
			menu.addHideHandler(new HideHandler() {
				@Override
				public void onHide(HideEvent event) {
					collapse(lastContext, lastParent);
				}
			});
			tree.getSelectionModel().addSelectionHandler(new SelectionHandler<TreeNodeTree>(){

				@Override
				public void onSelection(SelectionEvent<TreeNodeTree> event) {
					selecionTreeNodeId = event.getSelectedItem().getTreeNodeId();
					List<String> nodePathList = TreeUtil.getNamePath(selecionTreeNodeId, treeStore.getRootItems().get(0));
					CcTriggerTreeCell.this.updateText(StringUtils.join(nodePathList, ">"));
					collapse(lastContext, lastParent);
				}
				
			});
			menu.add(tree);
		}
	}

	private class CcTreeCellPropertyEditor extends PropertyEditor<Integer> {

		@Override
		public Integer parse(CharSequence text) throws ParseException {
			return selecionTreeNodeId;
		}

		@Override
		public String render(Integer object) {
			return getInputElement(lastParent).getValue();
		}

	}
	
	public void setRootId(int rootId) {
		this.rootId = rootId;
	}

	public void updateText(String text){
		getInputElement(lastParent).setValue(text);
	}
	
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * 按向下键展开树.
	 * @param context
	 * @param parent
	 * @param value
	 * @param event
	 * @param valueUpdater
	 */
	@Override
	protected void onNavigationKey(Context context, Element parent, Integer value, NativeEvent event, ValueUpdater<Integer> valueUpdater) {
		if (event.getKeyCode() == KeyCodes.KEY_DOWN && !isExpanded()) {
			event.stopPropagation();
			event.preventDefault();
			onTriggerClick(context, parent.<XElement> cast(), event, value, valueUpdater);
		}
	}
	
	@Override
	protected void onTriggerClick(Context context, XElement parent, NativeEvent event, Integer value, ValueUpdater<Integer> updater) {
		super.onTriggerClick(context, parent, event, value, updater);
		if (!isReadOnly() && !isDisabled()) {
			// blur is firing after the expand so context info on expand is
			// being cleared
			// when value change fires lastContext and lastParent are null
			// without this code
			if ((GXT.isWebKit()) && lastParent != null && lastParent != parent) {
				getInputElement(lastParent).blur();
			}
			onFocus(context, parent, value, event, updater);
			expand(context, parent, value, updater);
		}
	}

	public void collapse(final Context context, final XElement parent) {
		if (!expanded) {
			return;
		}

		expanded = false;

		menu.hide();
		fireEvent(context, new CollapseEvent(context));
	}

	/**
	 * 下拉树默认是有加载缓存，如果要强制刷新，请调用此方法
	 */
	public void clearStore(){
		treeStore.clear();
	}
	
	public void expand(final Context context, final XElement parent,	Integer value, ValueUpdater<Integer> valueUpdater) {
		if (expanded) {
			return;
		}

		this.expanded = true;

		// expand may be called without the cell being focused
		// saveContext sets focusedCell so we clear if cell
		// not currently focused
		boolean focused = focusedCell != null;
		saveContext(context, parent, null, valueUpdater, value);
		if (!focused) {
			focusedCell = null;
		}

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				createTree();
				tree.setWidth(parent.getWidth(true) - 2); 
				menu.setWidth(parent.getWidth(true));
				menu.show(parent, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true));
				if(treeStore.getRootCount() == 0){ //只有在清空的状态才会reload，如果要强制刷新，请在窗体或表单重新加载时调用clearStore
					tree.mask();
					ClientManager.getTreeNodeClient().getTree(rootId, new RestCallback<TreeNodeTree>(){
						@Override
						public void onSuccess(Method method, TreeNodeTree response) {
							TreeUtil.loadTreeStore(treeStore, response, true);
							tree.focus();
							tree.expandAll();
							tree.unmask();
							fireEvent(context, new ExpandEvent(context));
						}
						protected void afterFailure(){
							tree.unmask();
						}
					});
				}else{
					fireEvent(context, new ExpandEvent(context));
				}
			}
		});
	}

	@Override
	protected boolean isFocusedWithTarget(Element parent, Element target) {
		boolean result = parent.isOrHasChild(target)
				|| (menu != null && (menu.getElement().isOrHasChild(target) || tree.getElement().isOrHasChild(target)));
		return result;
	}

	@Override
	protected void triggerBlur(Context context, XElement parent, Integer value,	ValueUpdater<Integer> valueUpdater) {
		super.triggerBlur(context, parent, value, valueUpdater);
		collapse(context, parent);
	}
}
