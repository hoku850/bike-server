package org.ccframe.client.module.core.view;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeUtil;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.RoleSelectEvent;
import org.ccframe.subsys.core.domain.entity.Role;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

@Singleton
public class AdminRoleMenuView implements ICcModule{

	public Widget widget; 

	interface AdminRoleMenuUiBinder extends UiBinder<Component, AdminRoleMenuView> {}
	private static AdminRoleMenuUiBinder uiBinder = GWT.create(AdminRoleMenuUiBinder.class);
	
	@UiField(provided = true)
	public TreeStore<TreeNodeTree> treeStore = new TreeStore<TreeNodeTree>(TreeUtil.treeNodeTreeKeyProvider);	
	
	@UiField
	public Tree<TreeNodeTree, String> tree;

	private Role role;
	
	private int currentMenuTreeOrdId;
	
	@UiFactory
	public ValueProvider<TreeNodeTree, String> createValueProvider() {
		return TreeUtil.treeValueProvider;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		currentMenuTreeOrdId = -1;
	}

	private void reloadMenuAndCheck(){
		ClientManager.getTreeNodeClient().getOrgMenuTree(role.getOrgId(), new RestCallback<TreeNodeTree>(){
			@Override
			public void onSuccess(Method method, TreeNodeTree response) {
				TreeUtil.loadTreeStore(treeStore, response, true);
				tree.focus();
				tree.expandAll();
				currentMenuTreeOrdId = role.getOrgId();
				reloadCheck();
			}
			protected void afterFailure(){
				tree.unmask();
			}
		});
	}
	
	private void reloadCheck(){
		ClientManager.getAdminRoleClient().findRoleSysTreeNodeIdList(role.getRoleId(), new RestCallback<List<Integer>>(){
			@Override
			public void onSuccess(Method method, List<Integer> sysTreeNodeIdList) {
				List<TreeNodeTree> toCheckList = new ArrayList<TreeNodeTree>(); 
				for(Integer sysTreeNodeId: sysTreeNodeIdList){
					toCheckList.add(treeStore.findModelWithKey(sysTreeNodeId.toString()));
				}
				tree.setCheckedSelection(toCheckList);
				tree.unmask();
			}
			protected void afterFailure(){
				tree.unmask();
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		if(widget == null){
			widget = uiBinder.createAndBindUi(this);
			EventBusUtil.addHandler(RoleSelectEvent.TYPE, new BaseHandler<RoleSelectEvent>(){
				@Override
				public void action(RoleSelectEvent event) {
					role = event.getObject();
					tree.mask();
					if(currentMenuTreeOrdId != role.getOrgId()){ //机构改变了，重新加载树和勾选
						reloadMenuAndCheck();
					}else{ //机构没改变，只加载勾选即可
						reloadCheck();
					}
				}
			});
			tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
		return widget;
	}

	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		List<Integer> sysMenuResIdList = new ArrayList<Integer>();
		for(TreeNodeTree treeNodeTree: tree.getCheckedSelection()){
			sysMenuResIdList.add(treeNodeTree.getSysObjectId());
		}
		ClientManager.getAdminRoleClient().saveRoleMenuList(sysMenuResIdList, role.getRoleId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "角色授权已更新");
			}
		});
	}
	
	@UiHandler("checkUncheckSubAllButton")
	public void handleCheckUncheckSubAll(SelectEvent e){
		TreeNodeTree treeNodeTree = tree.getSelectionModel().getSelectedItem();
		if(treeNodeTree == null){
			ViewUtil.error("错误", "请线选择一个节点");
		}
		CheckState checkState = tree.getChecked(treeNodeTree);
		if(checkState == CheckState.CHECKED){ //取消选中下级所有
			checkStateSubAll(treeNodeTree, CheckState.UNCHECKED);
		}
		if(checkState == CheckState.UNCHECKED){ //选中下级所有
			checkStateSubAll(treeNodeTree, CheckState.CHECKED);
		}
	}
	
	private void checkStateSubAll(TreeNodeTree treeNodeTree, CheckState checkState){
		tree.setChecked(treeNodeTree, checkState);
		for(TreeNodeTree subNodeTree: treeNodeTree.getSubNodeTree()){
			checkStateSubAll(subNodeTree, checkState);
		}
	}
}
