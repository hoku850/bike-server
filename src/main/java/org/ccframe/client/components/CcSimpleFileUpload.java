package org.ccframe.client.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.components.fileupload.CcBaseFileUpload;
import org.ccframe.client.components.fileupload.FileUploadCompleteHandler;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.vectomatic.file.File;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.error.ErrorHandler;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;

/**
 * 图片上传组件框.
 * TODO 未完成
 * @author JIM
 *
 */
public class CcSimpleFileUpload extends CcBaseFileUpload implements IsField<FileInfDto> {

	private static final int BUTTON_MIN_WIDTH = 44;
	
	private HBoxLayoutContainer managerLayerContainer;
	private TextButton uploadButton;
	private TextButton clearButton;
	private TextButton previewButton;
	private TextField infText;
	private ProgressBar uploadProgressBar;

	private ErrorHandler errorSupport;
	private Validator<FileInfDto> emptyValidator;
	private List<Validator<FileInfDto>> validators = new ArrayList<Validator<FileInfDto>>();
	private static NumberFormat percentFormat = NumberFormat.getFormat("#0.0");

	private FileUploadCompleteHandler fileUploadCompleteHandler = new FileUploadCompleteHandler(){
		@Override
		public void onUploadComplete(FileInfDto fileInfBarDto) {
			managerLayerContainer.add(clearButton);
			managerLayerContainer.add(previewButton);
			uploadButton.setWidth(BUTTON_MIN_WIDTH);
			managerLayerContainer.forceLayout();
			infText.setText(fileInfBarDto.getFileNm());
			setActiveWidget(managerLayerContainer);
			fileInfDto = fileInfBarDto;
		}

		@Override
		public void onUploadError(File workFile) {
			reset();
		}
	};
	
	private FileInfDto fileInfDto;

	public CcSimpleFileUpload() {
		this(null);
	}
	
	@UiConstructor
	public CcSimpleFileUpload(String filterGroup) {
		super(false, filterGroup);
		this.setFileUploadCompleteHandler(fileUploadCompleteHandler);
	}

	@Override
	protected void onNextFileUpload(int current, int total) {}

	@Override
	protected void onQueueUploadFinish() {}

	@Override
	protected void onStartUpload() {
		setActiveWidget(uploadProgressBar);
	}

	@Override
	protected void onUpdateProgress(double totoalLoadedBytes, long totalBytesToUpload) {
		String workFileName = getWorkFile().getName();
		uploadProgressBar.updateProgress(totoalLoadedBytes/totalBytesToUpload, "正在上传 " + (workFileName.length() <= 20 ? workFileName : (workFileName.substring(0, 20) + "...")) + " 进度： " + percentFormat.format(totoalLoadedBytes/totalBytesToUpload*100) + "%");
	}

	public void setAllowEmpty(boolean allowEmpty) {
	    if (!allowEmpty) {
	      if (emptyValidator == null) {
				emptyValidator = new Validator<FileInfDto>() {
					@Override
					public List<EditorError> validate(
							Editor<FileInfDto> editor, FileInfDto value) {
						if (StringUtils.isEmpty(value.getFilePath())) {
							List<EditorError> errors = new ArrayList<EditorError>();
							errors.add(new DefaultEditorError(editor,
									DefaultMessages.getMessages()
											.textField_blankText(), ""));
							return errors;
						}
						return null;
					}
				};
	      }
			if (!getValidators().contains(emptyValidator)) {
				getValidators().add(0, emptyValidator);
			}
		} else if (emptyValidator != null) {
			validators.remove(emptyValidator);
		}

	}
	/**
	 * Adds a validator to be invoked when {@link #validateValue(Object)} is
	 * invoked.
	 * 
	 * @param validator
	 *            the validator to add
	 */
	public void addValidator(Validator<FileInfDto> validator) {
		validators.add(validator);
	}

	/**
	 * Returns the field's validators.
	 * 
	 * @return the validators
	 */
	public List<Validator<FileInfDto>> getValidators() {
		return validators;
	}

	/**
	 * Returns whether or not the field value is currently valid.
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return isValid(false);
	}

	private Widget initManagerLayerContainer() {
		managerLayerContainer = new HBoxLayoutContainer();
		infText = new TextField();
		infText.getCell().getInputElement(infText.getElement()).getStyle().setHeight(26, Unit.PX);
		infText.setReadOnly(true);

		uploadButton = new TextButton("上传");
		uploadButton.setWidth(BUTTON_MIN_WIDTH);
		clearButton = new TextButton("清除");
		clearButton.setWidth(BUTTON_MIN_WIDTH);
		previewButton = new TextButton("预览");
		previewButton.setWidth(BUTTON_MIN_WIDTH);

		BoxLayoutData textLayoutData = new BoxLayoutData(new Margins(0, 0, 0, 5));
		textLayoutData.setFlex(1.0d);
		managerLayerContainer.add(infText, textLayoutData);
		BoxLayoutData buttonLayoutData = new BoxLayoutData(new Margins(0, 0, 0, 5));
		managerLayerContainer.add(uploadButton, buttonLayoutData);
		managerLayerContainer.add(clearButton, buttonLayoutData);
		managerLayerContainer.add(previewButton, buttonLayoutData);
		return managerLayerContainer;
	}

	@Override
	protected void initWidget() {
		uploadProgressBar = new ProgressBar();
		uploadProgressBar.setBorders(true);
		uploadProgressBar.updateText("就绪");
		this.add(uploadProgressBar);
		this.add(initManagerLayerContainer());
		this.setActiveWidget(managerLayerContainer);
		uploadButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				getFileUploadExt().click();
			}
		});
		clearButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				CcSimpleFileUpload.this.reset();
			}
		});
		previewButton.addSelectHandler(new SelectHandler(){
			@Override
			public void onSelect(SelectEvent event) {
				String fileNameString = "/" + Global.TEMP_DIR +fileInfDto.getFilePath().substring(5);
				Window.open(GWT.getHostPageBaseURL() + (GWT.getHostPageBaseURL().endsWith("/") ?  fileNameString.substring(1) : fileNameString), "", "");
			}
		});
		this.errorSupport = new SideErrorHandler(this);
	}

	@Override
	public void setValue(FileInfDto value) {
		this.fileInfDto = value;
	}

	@Override
	public FileInfDto getValue() {
		return fileInfDto;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<FileInfDto> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void clearInvalid() {
		errorSupport.clearInvalid();
		fireEvent(new ValidEvent());
	}

	@Override
	public void finishEditing() {}

	@Override
	public List<EditorError> getErrors() {
		return Collections.<EditorError> emptyList();
	}

	@Override
	public boolean isValid(boolean preventMark) {
		if (disabled) {
			return true;
		}
		return validateValue(getValue());
	}

	@Override
	public void reset() {
	    setValue(null);
	    if(clearButton.getParent() != null){
	    	clearButton.removeFromParent();
	    }
	    if(previewButton.getParent() != null){
	    	previewButton.removeFromParent();
	    }
	    uploadButton.setWidth(142);
	    managerLayerContainer.forceLayout();
	    infText.reset();
	    infText.setValue("请选择文件上传");
	    clearInvalid();
	}

	@Override
	public boolean validate(boolean preventMark) {
	    return validate(false);
	}

	/**
	 * Actual implementation of markInvalid, which bypasses recording an error
	 * in the editor peer.
	 * 
	 * @param msg
	 *            the validation message
	 */
	protected void markInvalid(List<EditorError> msg) {
		errorSupport.markInvalid(msg);
		fireEvent(new InvalidEvent(msg));
	}

	protected boolean validateValue(FileInfDto value) {
		List<EditorError> errors = new ArrayList<EditorError>();
		for (int i = 0; i < validators.size(); i++) {
			List<EditorError> temp = validators.get(i).validate(this, value);
			if (temp != null && temp.size() > 0) {
				errors.addAll(temp);
			}
		}
		if (errors.size() > 0) {
			markInvalid(errors);
			return false;
		}
		return true;
	}

}
