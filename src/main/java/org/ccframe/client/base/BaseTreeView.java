package org.ccframe.client.base;

import java.util.List;

import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeUtil;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.TreeNodeTypeCodeEnum;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * 基础树结构.
 * 
 * @author JIM
 *
 * @param <T>
 */
public abstract class BaseTreeView implements ICcModule{

	public Widget widget; 

	@UiField
	public TextButton addButton;

	@UiField
	public TextButton editButton;

	@UiField
	public TextButton deleteButton;

	private Window simpleAddModifyWin;
	
	private CcTextField treeNodeNm;

	private IntegerField treeNodePosition;

	private TextButton simpleAddModifyButton;
	
	private Integer selectedId;
	
	@UiField(provided = true)
	public TreeStore<TreeNodeTree> treeStore = new TreeStore<TreeNodeTree>(TreeUtil.treeNodeTreeKeyProvider);	
	
	@UiField
	public Tree<TreeNodeTree, String> tree;

	@UiFactory
	public ValueProvider<TreeNodeTree, String> createValueProvider() {
		return TreeUtil.treeValueProvider;
	}

	@UiHandler({"addButton","editButton"})
	public void handleAddClick(SelectEvent e){
		TreeNodeTree selectItem = tree.getSelectionModel().getSelectedItem();
		if(selectItem == null){
			ViewUtil.error("系统信息", "请先选择父节点");
			return;
		}
		boolean isAdd = (e.getSource() == addButton);
		simpleAddModifyWin.setHeadingText(isAdd ? "新增": "修改" + tree.getTitle()); //例如 新增文章分类
		
		if(isAdd){
			treeNodeNm.clear();
			treeNodePosition.setValue(100); //默认是100
			selectedId = null;
		}else{
			treeNodeNm.setValue(selectItem.getTreeNodeNm());
			treeNodePosition.setValue(selectItem.getTreeNodePosition());
			selectedId = selectItem.getTreeNodeId();
		}

		simpleAddModifyWin.show();
		simpleAddModifyWin.center();
	}

	@UiHandler("deleteButton")
	public void handleDeleteClick(SelectEvent e){
		final TreeNodeTree selectItem = tree.getSelectionModel().getSelectedItem();
		if(selectItem == null){
			ViewUtil.error("系统信息", "请先选择节点");
			return;
		}
		if(BoolCodeEnum.fromCode(selectItem.getIfSysReserve()).boolValue()){
			ViewUtil.error("系统保留节点", "系统保留节点，禁止删除");
			return;
		}
		ViewUtil.confirm("系统信息", "您确定要删除节点 " + selectItem.getTreeNodeNm() + " 吗？删除后将不可恢复", new Runnable(){
			@Override
			public void run() {
				ClientManager.getTreeNodeClient().delete(selectItem.getTreeNodeId(), getCheckBeans(), new RestCallback<Void>(){
					@Override
					public void onSuccess(Method method, Void response) {
						reloadTree();
					}
				});
				
			}
		});
	}
	
	abstract protected Widget bindUi();

	protected abstract Integer getTreeRootId();
	
	protected abstract TreeNodeTypeCodeEnum getTreeNodeTypeCodeEnum();
	
	protected abstract SelectionHandler<TreeNodeTree> getTreeSelectionHandler();
	
	protected abstract String getCheckBeans();
	
	public BaseTreeView(){}

	private void reloadTree(){
		ClientManager.getTreeNodeClient().getSubTree(getTreeRootId(), new RestCallback<List<TreeNodeTree>>(){
			@Override
			public void onSuccess(Method method, List<TreeNodeTree> response) {
				TreeNodeTree root = treeStore.getRootItems().get(0);
				treeStore.removeChildren(root);
				TreeUtil.loadTreeStoreSubNodes(treeStore, root, response);
				new Timer() {
		            @Override
		            public void run() {
		            	tree.expandAll();
		            	tree.getSelectionModel().select(treeStore.getChild(0), true);
		            }
		        }.schedule(100);
			}
		});
	}
	
	@Override
	public void onModuleReload(BodyContentEvent event) {
		if(treeStore.getRootCount() != 0){
			reloadTree();
		}
	}

	@Override
	public Widget asWidget() {
		if(widget == null){
			widget = bindUi();
			tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			tree.getSelectionModel().addSelectionHandler(getTreeSelectionHandler());
			tree.getSelectionModel().addSelectionHandler(new SelectionHandler<TreeNodeTree>(){
				@Override
				public void onSelection(SelectionEvent<TreeNodeTree> selection) {
					boolean isRoot = selection.getSelectedItem().equals(treeStore.getChild(0));
					editButton.setEnabled(!isRoot); //非根节点可编辑
					deleteButton.setEnabled(selection.getSelectedItem().getSubNodeTree().isEmpty() && !isRoot); //非根的叶节点可删除
				}
			});
			ClientManager.getTreeNodeClient().getTree(getTreeRootId(), new RestCallback<TreeNodeTree>(){
				@Override
				public void onSuccess(Method method, TreeNodeTree response) {
					TreeUtil.loadTreeStore(treeStore, response, true);
					tree.setTitle(response.getTreeNodeNm()); //重设标题，例如根节点为“文章分类”，则标题为“文章分类”，增改窗体为“增加文章分类”或“修改文章分类”
					new Timer() {
			            @Override
			            public void run() {
			            	tree.expandAll();
			            	tree.getSelectionModel().select(treeStore.getChild(0), true);
			            }
			        }.schedule(100);
				}
			});
			treeNodeNm = new CcTextField();
			treeNodeNm.setWidth(100);
			treeNodeNm.setAllowBlank(false);
			treeNodeNm.setMaxLength(10);
			
			treeNodePosition = new IntegerField();

			FieldLabel treeNodePositionRow = new FieldLabel(treeNodePosition, "节点序号");

			
			FieldLabel treeNodeNmRow = new FieldLabel(treeNodeNm, "节点名称");
			
			simpleAddModifyButton = new TextButton("保存", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					if(!treeNodeNm.validate() || !treeNodePosition.validate()){
						return;
					}

					final TreeNodeTree treeNodeTree = new TreeNodeTree();
					treeNodeTree.setTreeNodeId(selectedId);
					treeNodeTree.setTreeNodeNm(treeNodeNm.getValue());
					treeNodeTree.setTreeNodeTypeCode(getTreeNodeTypeCodeEnum().toCode());
					treeNodeTree.setTreeNodePosition(treeNodePosition.getValue());
					treeNodeTree.setTreeNodeTypeCode(getTreeNodeTypeCodeEnum().toCode());
					TreeNodeTree selectionNode = tree.getSelectionModel().getSelectedItem();
					
					treeNodeTree.setIfSysReserve(selectedId == null ? BoolCodeEnum.NO.toCode() : selectionNode.getIfSysReserve());
					treeNodeTree.setUpperTreeNodeId(selectedId == null ? selectionNode.getTreeNodeId() : selectionNode.getUpperTreeNodeId());
					ClientManager.getTreeNodeClient().addModify(treeNodeTree, new RestCallback<Void>(){
						@Override
						public void onSuccess(Method method, Void response) {
							Info.display("保存节点", "保存成功！");
							simpleAddModifyWin.hide();
							if(selectedId == null){
								reloadTree();
							}else{
								treeStore.update(treeNodeTree);
							}
						}
					});
				}
			});

			simpleAddModifyWin = new Window();
			simpleAddModifyWin.setWidth(300);
			simpleAddModifyWin.setModal(true);
			CcVBoxLayoutContainer vBoxLayoutContainer = new CcVBoxLayoutContainer();
			vBoxLayoutContainer.add(treeNodeNmRow, new BoxLayoutData(new Margins(10, 10, 5, 10)));
			vBoxLayoutContainer.add(treeNodePositionRow, new BoxLayoutData(new Margins(0, 10, 5, 10)));
			simpleAddModifyWin.setWidget(vBoxLayoutContainer);
			simpleAddModifyWin.addButton(simpleAddModifyButton);
			simpleAddModifyWin.addButton(new TextButton("取消", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					simpleAddModifyWin.hide();
				}
			}));
			simpleAddModifyWin.setResizable(false);
			simpleAddModifyWin.setButtonAlign(BoxLayoutPack.CENTER);
			
		}
		return widget;
	}

	
}
