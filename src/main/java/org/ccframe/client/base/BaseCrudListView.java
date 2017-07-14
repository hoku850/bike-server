package org.ccframe.client.base;

import org.ccframe.client.commons.ViewUtil;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * 列表基类.
 * 封装了列表的基础按钮逻辑结构，子类可以扩展其它按钮.
 * 
 * @author JIM
 *
 * @param <T>
 */
public abstract class BaseCrudListView<T> extends BasePagingListView<T>{

	@UiField
	public TextButton addButton;

	@UiField
	public TextButton editButton;

	@UiField
	public TextButton deleteButton;

	/**
	 * 是否双击进行编辑
	 */
	private boolean doubleClickEdit = true;
	
	public void setDoubleClickEdit(boolean doubleClickEdit) {
		this.doubleClickEdit = doubleClickEdit;
	}

	@UiHandler("addButton")
	public abstract void handleAddClick(SelectEvent e);

	@UiHandler("editButton")
	public void handleEditClick(SelectEvent e){
		if(grid.getSelectionModel().getSelectedItems().size() != 1){
			ViewUtil.error("系统信息", "请选择一条记录进行编辑");
			return;
		}
		onEditClick(e);
	}

	protected abstract void onEditClick(SelectEvent e);
	
	@Override
	protected void bindOther(){
		if(doubleClickEdit){
			grid.addCellDoubleClickHandler(new CellDoubleClickHandler(){
				@Override
				public void onCellClick(CellDoubleClickEvent event) {
					editButton.fireEvent(new SelectEvent());
				}
			});
		}
		super.bindOther();
	}

	@UiHandler("deleteButton")
	public void handleDeleteClick(final SelectEvent e){
		int selectedCount = grid.getSelectionModel().getSelectedItems().size();
		if(selectedCount == 0){
			ViewUtil.error("系统信息", "请选择记录进行删除");
			return;
		}
		ViewUtil.confirm("提示信息", "您确定删除这" + (selectedCount == 1 ? "" : (" " + Integer.toString(selectedCount) + " ")) + "条记录吗？", new Runnable(){
			@Override
			public void run() {
				doDeleteClick(e);
			}
		});
	}

	protected abstract void doDeleteClick(SelectEvent e);
	
}
