package org.ccframe.client.components;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.components.fileupload.CcFileInfBar;
import org.ccframe.client.components.fileupload.CcFileInfBarItem;
import org.ccframe.client.components.fileupload.FileInfBarItemDoubleClickHandler;
import org.ccframe.subsys.core.dto.FileInfDto;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.axeiya.gwtckeditor.client.event.InstanceReadyEvent;
import com.axeiya.gwtckeditor.client.event.InstanceReadyHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.AdapterField;
/**
 * 
 * 根据aperp系统改装后的富文本组件
 * 
 * @author Jim
 *
 */
public class CcCkEditor extends AdapterField<String> {
	
    private CKEditor editor;
    private CKConfig ckConfig;
    private int maxLength;
    private boolean allowBlank = true;
    private String pendigValue; //待设置的值，界面初始化后才真正载入
    
    int toolbarRows = 2;
    private static final int WRAP_ROW_WIDTH = 840; //根据当前CKConfig测量得短与该px的界面都会被折行

    private CcVBoxLayoutContainer vBoxLayoutContainer;
    
    private int TOOL_BAR_PADDING = 11;
    private int TOOL_BAR_ROW_HEIGHT = 33;
    private int FILE_INF_BAR_HEIGHT = 60;
    
    private CcFileInfBar fileInfBar;
    
    /**
     * 
     */
    public CcCkEditor(){
    	super(new CcVBoxLayoutContainer());
    	vBoxLayoutContainer = (CcVBoxLayoutContainer) getWidget();
    	vBoxLayoutContainer.setAutoHeight(false);
    	vBoxLayoutContainer.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
    	ckConfig = new CKConfig();
        ckConfig.setLanguage("zh-cn");
        ckConfig.setUiColor("#6ca8d3");
        ckConfig.setUseFormPanel(false);
        ckConfig.setEnterMode("2"); //CKEDITOR.ENTER_BR
        ckConfig.setBaseFloatZIndex(1000000);
        ckConfig.setExtraAllowedContent("img[src,alt,style,width,height]");
        Toolbar t = new Toolbar();
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{ //源码
                CKConfig.TOOLBAR_OPTIONS.Source,
                CKConfig.TOOLBAR_OPTIONS.Preview,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.PasteText,
                CKConfig.TOOLBAR_OPTIONS.PasteFromWord
        }));
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{
                CKConfig.TOOLBAR_OPTIONS.Font,
                CKConfig.TOOLBAR_OPTIONS.FontSize,
                CKConfig.TOOLBAR_OPTIONS.Bold,
                CKConfig.TOOLBAR_OPTIONS.Italic,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.TextColor,
                CKConfig.TOOLBAR_OPTIONS.BGColor,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.RemoveFormat
        }));
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{
                CKConfig.TOOLBAR_OPTIONS.NumberedList,
                CKConfig.TOOLBAR_OPTIONS.BulletedList,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.JustifyLeft,
                CKConfig.TOOLBAR_OPTIONS.JustifyCenter,
                CKConfig.TOOLBAR_OPTIONS.JustifyRight
        }));
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{
                CKConfig.TOOLBAR_OPTIONS.Link,
                CKConfig.TOOLBAR_OPTIONS.Unlink,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.Table,
                CKConfig.TOOLBAR_OPTIONS.Image,
                CKConfig.TOOLBAR_OPTIONS.Flash,
                CKConfig.TOOLBAR_OPTIONS.PageBreak
        }));
        ckConfig.setToolbar(t);
        fileInfBar = new CcFileInfBar(new FileInfBarItemDoubleClickHandler(){
			@Override
			public void onDoubleClick(FileInfDto fileInfBarDto) {
				if(editor.isAttached() && editor.isReady()){
					if(CcFileInfBarItem.IMG_TYPE_EXT_LIST.contains(fileInfBarDto.getFileTypeNm().toLowerCase())){ //图片URL是插入图片
						editor.insertHTML("<img src='" + fileInfBarDto.getFileUrl() + "' style='width:100%;'/>");
					}else{ //其它是插入链接
						editor.insertHTML(" <a href='" + fileInfBarDto.getFileUrl() + "'/>"+fileInfBarDto.getFileNm()+"</a> ");
					}
				}
			}
        });
        fileInfBar.setHeight(FILE_INF_BAR_HEIGHT);
        fileInfBar.getElement().getStyle().setProperty("borderBottom", Global.DEFAULT_GWT_WIDGET_BORDER);
        fileInfBar.getElement().getStyle().setProperty("borderLeft", Global.DEFAULT_GWT_WIDGET_BORDER);
        fileInfBar.getElement().getStyle().setProperty("borderRight", Global.DEFAULT_GWT_WIDGET_BORDER);
    	vBoxLayoutContainer.add(fileInfBar);
    }

    @Override
    public void setPixelSize(int width, int height) {
    	if(editor == null){ //未创建，大小设置到config
    		if(width > 0){
    			toolbarRows = (width >= WRAP_ROW_WIDTH ? 1 : 2);
    			ckConfig.setWidth((width -2) + "px");
    		}
    		if(height > 0){
                ckConfig.setHeight((height-TOOL_BAR_PADDING-TOOL_BAR_ROW_HEIGHT*toolbarRows-FILE_INF_BAR_HEIGHT) + "px");
    		}
    	}
        fileInfBar.setWidth(width);
		super.setPixelSize(width, height);
	}

	@Override
    public void onAttach(){
    	super.onAttach();
        editor = new CKEditor(ckConfig);
        vBoxLayoutContainer.insert(editor, 0);
		vBoxLayoutContainer.forceLayout();

        editor.addHandler(new InstanceReadyHandler(){
			@Override
			public void onInstanceReady(InstanceReadyEvent event) {
		        if(pendigValue != null){
		        	editor.setHTML(pendigValue);
		        	pendigValue = null;
		        }
				Widget widget = CcCkEditor.this.getParent();
				if(widget != null){
					if(widget instanceof CcVBoxLayoutContainer && ((CcVBoxLayoutContainer) widget).isAutoHeight()){
						((CcVBoxLayoutContainer)widget).forceLayout();
					}else{
						if(widget.getParent() != null && widget.getParent() instanceof CcVBoxLayoutContainer && ((CcVBoxLayoutContainer) widget.getParent()).isAutoHeight()){
							((CcVBoxLayoutContainer)widget.getParent()).forceLayout();
						}
					}
				}
			}
        }, InstanceReadyEvent.TYPE);
    }
    
    @Override
    public void onDetach(){
    	super.onDetach();
   		editor.destory();
    	editor.removeFromParent();
    	editor = null;
    }

    @Override
	public String getValue() {
    	if(editor != null && editor.isReady()){
    		return editor.getHTML();
    	}
    	else{
    		return pendigValue == null ? "" : pendigValue;
    	}
	}

	@Override
	public void setValue(String value) {
		if(editor != null && editor.isReady()){
			editor.setHTML(value == null ? "" : value);
		}else{
			pendigValue = value;
		}
	}
    
    public CKEditor getEditor() {
        return editor;
    }

    public void setFocus(){
        editor.setFocus(true);
    }

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

    public boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

	@Override
	public void clear() {
		this.setValue("");
		fileInfBar.clear();
	}

    public void setFileInfBarDtoList(List<FileInfDto> fileInfBarDtoList){
    	fileInfBar.clear();
    	fileInfBar.addFileInfBarDtoList(fileInfBarDtoList);
    }
    
    public List<FileInfDto> getFileInfBarDtoList(){
    	return fileInfBar.getFileInfBarDtoList();
    }
}