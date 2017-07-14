package org.ccframe.client.components;

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
 * 只处理简单文本HTML格式用的ckeditor
 * 
 * @author Jim
 *
 */
public class CcCkTextEditor extends AdapterField<String> {
	
    private CKEditor editor;
    private CKConfig ckConfig;
    private int maxLength;
    private boolean allowBlank = true;
    private String pendigValue; //待设置的值，界面初始化后才真正载入
    
    int toolbarRows = 2;

    private CcVBoxLayoutContainer vBoxLayoutContainer;
    
    private int TOOL_BAR_PADDING = 11;
    private int TOOL_BAR_ROW_HEIGHT = 33;
    
    /**
     * 
     */
    public CcCkTextEditor(){
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
        }));
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{
                CKConfig.TOOLBAR_OPTIONS.JustifyLeft,
                CKConfig.TOOLBAR_OPTIONS.JustifyCenter,
                CKConfig.TOOLBAR_OPTIONS.JustifyRight,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.Link,
                CKConfig.TOOLBAR_OPTIONS.Unlink,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.Table
        }));
        t.add(new ToolbarLine(new CKConfig.TOOLBAR_OPTIONS[]{
                CKConfig.TOOLBAR_OPTIONS.Font,
                CKConfig.TOOLBAR_OPTIONS.FontSize,
                CKConfig.TOOLBAR_OPTIONS.Bold,
                CKConfig.TOOLBAR_OPTIONS.Italic,
                CKConfig.TOOLBAR_OPTIONS.TextColor,
                CKConfig.TOOLBAR_OPTIONS._,
                CKConfig.TOOLBAR_OPTIONS.RemoveFormat
        }));
        ckConfig.setToolbar(t);
    }

    public void setToolbarRows(int toolbarRows) {
		this.toolbarRows = toolbarRows;
	}

	@Override
    public void setPixelSize(int width, int height) {
    	if(editor == null){ //未创建，大小设置到config
    		if(width > 0){
    			ckConfig.setWidth((width -2) + "px");
    		}
    		if(height > 0){
                ckConfig.setHeight((height-TOOL_BAR_PADDING-TOOL_BAR_ROW_HEIGHT*toolbarRows) + "px");
    		}
    	}
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
				Widget widget = CcCkTextEditor.this.getParent();
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
	}

}