package org.ccframe.client.components.fileupload;

import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class CCBarFileUpload extends CcBaseFileUpload {

	private IconButton addButton;
	private VBoxLayoutContainer vBoxLayoutContainer;
	
	private Label progressHint;
	private ProgressBar uploadProgressBar;
	private static NumberFormat percentFormat = NumberFormat.getFormat("#0.0");

	public CCBarFileUpload(){
		super(true, null);
	}
	
	@Override
	protected void onNextFileUpload(int current, int total) {
		progressHint.setText("第" + current + "/" + total + "个");

	}

	@Override
	protected void onQueueUploadFinish() {
		setActiveWidget(addButton);
	}

	@Override
	protected void onStartUpload() {
		setActiveWidget(vBoxLayoutContainer);
	}

	@Override
	protected void onUpdateProgress(double totoalLoadedBytes, long totalBytesToUpload) {
		uploadProgressBar.updateProgress(totoalLoadedBytes/totalBytesToUpload, percentFormat.format(totoalLoadedBytes/totalBytesToUpload*100) + "%");
	}

	@Override
	protected void initWidget() {
      initButton();
      initProgressPanel();

      this.add(addButton);
      this.add(vBoxLayoutContainer);
      this.setActiveWidget(addButton);

	}

	private void initButton(){
		addButton = new IconButton(new IconConfig("cc-upload-add"));
		addButton.getElement().setMargins(4);
		addButton.setTitle("点击选择文件进行上传");
		addButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getFileUploadExt().click();
			}
		});
		addButton.setWidth(50);
		addButton.setHeight(50);
	}

	private void initProgressPanel(){
		vBoxLayoutContainer = new VBoxLayoutContainer();
		vBoxLayoutContainer.setHeight(55);
		vBoxLayoutContainer.setWidth(55);
		vBoxLayoutContainer.setBorders(true);
		vBoxLayoutContainer.getElement().getStyle().setMargin(2, Style.Unit.PX);
		vBoxLayoutContainer.getElement().getStyle().setPadding(2, Style.Unit.PX);
		progressHint = new Label();
		progressHint.setHeight("20px");
		progressHint.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
		uploadProgressBar = new ProgressBar();
		uploadProgressBar.setWidth(48);
		uploadProgressBar.setHeight(26);
		uploadProgressBar.setBorders(true);
		uploadProgressBar.updateText("就绪");
		vBoxLayoutContainer.add(progressHint);
		vBoxLayoutContainer.add(uploadProgressBar);
	}

}
