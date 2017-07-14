package org.ccframe.client.components.fileupload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ccframe.client.commons.ViewUtil;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.vectomatic.file.File;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * TODO
 * 1.超出容器范围 支持左右滚动，不显示滚动BAR
 * @author JIM
 */
public class CcFileInfBar extends HBoxLayoutContainer{

	private static final String BAR_TOOL_COLOR = "#5c97c0";
	
	private List<FileInfDto> fileInfBarDtoList = new ArrayList<>();

	private HBoxLayoutContainer fileBoxContainer;
	private VBoxLayoutContainer fileButtonContainer;
	
	private ToggleGroup toggle = new ToggleGroup();
	
	private CCBarFileUpload fileUpload;
	
	private FileInfBarItemDoubleClickHandler fileInfBarItemDoubleClickHandler; 

	public CcFileInfBar(FileInfBarItemDoubleClickHandler fileInfBarItemDoubleClickHandler){
		this.fileInfBarItemDoubleClickHandler = fileInfBarItemDoubleClickHandler;
		Label fjLabel = new Label("附件");
		Style fjStyle = fjLabel.getElement().getStyle();
		fjStyle.setDisplay(Style.Display.INLINE_BLOCK);
		fjStyle.setColor("#FFFFFF");
		fjStyle.setWidth(12, Style.Unit.PX);
		fjStyle.setHeight(48, Style.Unit.PX);
		fjStyle.setBackgroundColor(BAR_TOOL_COLOR);
		fjStyle.setPaddingTop(11, Style.Unit.PX);
		fjStyle.setPaddingLeft(5, Style.Unit.PX);
		fjStyle.setPaddingRight(5, Style.Unit.PX);
		this.add(fjLabel);
		//TODO 左右滚动按钮
		fileBoxContainer = new HBoxLayoutContainer();
		
		BoxLayoutData flexData = new BoxLayoutData();
		flexData.setFlex(1.0);
		this.add(fileBoxContainer, flexData);
		
		//TODO 左右滚动按钮和操作按钮
		fileUpload = new CCBarFileUpload();
		fileBoxContainer.add(fileUpload);
		fileUpload.setFileUploadCompleteHandler(new FileUploadCompleteHandler(){
			@Override
			public void onUploadComplete(final FileInfDto fileInfBarDto) {
				CcFileInfBarItem ccFileInfBarItem = new CcFileInfBarItem(fileInfBarDto, CcFileInfBar.this.fileInfBarItemDoubleClickHandler);
				toggle.add(ccFileInfBarItem);
				int fileInfId = -(int)(new Date().getTime());
				ccFileInfBarItem.setItemId(Integer.toString(fileInfId));
				fileBoxContainer.insert(ccFileInfBarItem, fileInfBarDtoList.size());
				fileInfBarDto.setFileInfId(fileInfId); //临时的文件数据ID都是负数
				fileInfBarDtoList.add(fileInfBarDto);
				fileBoxContainer.forceLayout();
			}

			@Override
			public void onUploadError(File workFile) {
				
			}
		});
		fileButtonContainer = new VBoxLayoutContainer();
		fileButtonContainer.setHeight(60);
		fileButtonContainer.setWidth(31);
		fileButtonContainer.getElement().getStyle().setPaddingLeft(2, Style.Unit.PX);
		Style fcStyle = fileButtonContainer.getElement().getStyle();
		fcStyle.setBackgroundColor(BAR_TOOL_COLOR);
		IconButton insertButton = new IconButton("cc-upload-insert");
		insertButton.setTitle("插入到文章正文");
		insertButton.getElement().getStyle().setMargin(4, Style.Unit.PX);
		insertButton.setPixelSize(22,22);
		insertButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				CcFileInfBarItem item = (CcFileInfBarItem)toggle.getValue();
				if(item == null || item.getParent() == null){
					ViewUtil.error("错误", "请选择一个附件进行插入");
				}else{
					CcFileInfBar.this.fileInfBarItemDoubleClickHandler.onDoubleClick(item.getFileInfBarDto());
				}
			}
		});
		fileButtonContainer.add(insertButton, new BoxLayoutData(new Margins(3)));
		IconButton delButton = new IconButton("cc-upload-del");
		delButton.setTitle("删除文章附件");
		delButton.getElement().getStyle().setMargin(4, Style.Unit.PX);
		delButton.setPixelSize(22,22);
		delButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				final CcFileInfBarItem item = (CcFileInfBarItem)toggle.getValue();
				if(item == null){
					ViewUtil.error("错误", "请选择一个附件进行删除");
				}else{
					ViewUtil.confirm("确认删除", "您确认删除该附件吗？该附件将标记为删除并在系统空闲的时间被清理。", new Runnable(){
						@Override
						public void run() {
							fileInfBarDtoList.remove(item.getFileInfBarDto());
							fileBoxContainer.remove(item);
							toggle.remove(item);
							fileBoxContainer.forceLayout();
						}
					});
				}
			}
		});
		fileButtonContainer.add(delButton, new BoxLayoutData(new Margins(3)));
		this.add(fileButtonContainer);
	}

	public void clear(){
		for(FileInfDto fileInfBarDto: fileInfBarDtoList){
			fileBoxContainer.getItemByItemId(Integer.toString(fileInfBarDto.getFileInfId())).removeFromParent();
		}
		fileInfBarDtoList.clear();
	}
	
	public void addFileInfBarDtoList(List<FileInfDto> fileInfBarDtoList){
		for(FileInfDto fileInfBarDto: fileInfBarDtoList){
			CcFileInfBarItem ccFileInfBarItem = new CcFileInfBarItem(fileInfBarDto, fileInfBarItemDoubleClickHandler);
			ccFileInfBarItem.setItemId(Integer.toString(fileInfBarDto.getFileInfId()));
			toggle.add(ccFileInfBarItem);
			fileBoxContainer.insert(ccFileInfBarItem, this.fileInfBarDtoList.size());
			this.fileInfBarDtoList.add(fileInfBarDto);
		}
		fileBoxContainer.forceLayout();
	}

	public List<FileInfDto> getFileInfBarDtoList() {
		return fileInfBarDtoList;
	}
}
