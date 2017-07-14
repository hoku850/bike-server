package org.ccframe.client.components.fileupload;

import java.util.Arrays;
import java.util.List;

import org.ccframe.subsys.core.dto.FileInfDto;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

/**
 * 文件条的图片选择块.
 * 如果需要按组来切换状态，请加入到ToggleGroup
 * Value用于标记选中的状态.
 * @author JIM
 *
 */
public class CcFileInfBarItem extends SimpleContainer implements HasValue<Boolean>{
	
	private static final String TRANSPARENT_IMG_GIF_1PX_DATA = "data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg==";
	
	private final Image image; 
	private FileInfDto fileInfBarDto;
	private boolean value;
	
	public static final List<String> IMG_TYPE_EXT_LIST = Arrays.asList(new String[]{"png","gif","jpg"});
	private static final List<String> ICON_TYPE_EXT_LIST = Arrays.asList(new String[]{"xls","xlsx","pdf","rar","ppt","pptx","doc","docx","zip"});
	
	private static final String DEFAULT_BORDER_COLOR = "#5c97c0";
	
	public FileInfDto getFileInfBarDto() {
		return fileInfBarDto;
	}

	public CcFileInfBarItem(FileInfDto fileInfBarDto,final FileInfBarItemDoubleClickHandler fileInfBarItemDoubleClickHandler){
		this.setSize("58px", "58px");
		this.setResize(false);
		image = new Image();
		image.setPixelSize(50, 50);
		Style imageStyle = image.getElement().getStyle();
		imageStyle.setMarginLeft(4, Style.Unit.PX);
		imageStyle.setMarginTop(3, Style.Unit.PX);
		imageStyle.setBorderStyle(Style.BorderStyle.SOLID);
		imageStyle.setBorderWidth(1, Style.Unit.PX);
		imageStyle.setBorderColor(DEFAULT_BORDER_COLOR);

		this.fileInfBarDto = fileInfBarDto;
		String fileTypeNm = fileInfBarDto.getFileTypeNm().toLowerCase();
		if(IMG_TYPE_EXT_LIST.contains(fileTypeNm)){
			image.setUrl(fileInfBarDto.getFileUrl()); //TODO 使用48X48小图
		}else if(ICON_TYPE_EXT_LIST.contains(fileTypeNm)){
			image.setUrl(TRANSPARENT_IMG_GIF_1PX_DATA);
			image.addStyleName("cc-file cc-file-"+fileTypeNm);
		}else{
			image.setUrl(TRANSPARENT_IMG_GIF_1PX_DATA);
			image.addStyleName("cc-file cc-file-other");
		}
		image.setTitle(fileInfBarDto.getFileNm());
		image.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				CcFileInfBarItem.this.setValue(true);
			}
		});
		image.addDoubleClickHandler(new DoubleClickHandler(){
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(fileInfBarItemDoubleClickHandler != null){
					fileInfBarItemDoubleClickHandler.onDoubleClick(CcFileInfBarItem.this.fileInfBarDto);
				}
			}
		});
		this.add(image);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public void setValue(Boolean value) {
		setValue(value, true);
	}

	@Override
	public void setValue(Boolean value, boolean fireEvents) {
		Boolean oldValue = getValue();
		if (value != oldValue && (oldValue == null || !oldValue.equals(value))) {
			this.value = value;
			image.getElement().getStyle().setBorderColor(value ? "#FF0000" : DEFAULT_BORDER_COLOR);
			if (fireEvents) {
				ValueChangeEvent.fire(this, value);
			}
		}
	}
}
