package org.ccframe.client.components.fileupload;

import java.util.List;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.IsField;

/**
 * 图片上传组件框.
 * @author JIM
 *
 */
public class CcImgFileUpload extends CcBaseFileUpload implements IsField<String>{

	private VBoxLayoutContainer managerLayerContainer;
	private TextButton uploadButton;
	private TextButton clearButton;
	private TextButton previewButton;

	public CcImgFileUpload(){
		super(false, CcBaseFileUpload.FILTER_IMAGE);
		this.setPixelSize(210, 150 + 10 + 30);
	}

	@Override
	protected void onNextFileUpload(int current, int total) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onQueueUploadFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStartUpload() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdateProgress(double totoalLoadedBytes,
			long totalBytesToUpload) {
		// TODO Auto-generated method stub

	}

	private Widget initManagerLayerContainer(){
		managerLayerContainer = new VBoxLayoutContainer();
		Image img = new Image();
		//img.setUrl();
		return managerLayerContainer;
	}
	
	@Override
	protected void initWidget() {
//		initButton();
//		initProgressPanel();
//		
//		this.add(addButton);
//		this.add(initManagerLayerContainer());
//		this.setActiveWidget(addButton);
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearInvalid() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishEditing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<EditorError> getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(boolean preventMark) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean validate(boolean preventMark) {
		// TODO Auto-generated method stub
		return false;
	}

}
