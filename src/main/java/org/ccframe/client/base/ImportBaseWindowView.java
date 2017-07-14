package org.ccframe.client.base;

import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.components.CcSimpleFileUpload;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;


/**
 * 导入功能模板窗体.
 * 
 * @author JIM
 *
 * @param <T>
 */
public abstract class ImportBaseWindowView extends BaseWindowView<Void, Void>{

	private static NumberFormat percentFormat = NumberFormat.getFormat("#0.0");

	@UiField
	public CcVBoxLayoutContainer vBoxLayoutContainer;

	@UiField
	public CcSimpleFileUpload simpleFileUpload;
	
	@UiField
	public Anchor templateLink;

	@UiField
	public HTML uploadResult;
	
	@UiField
	public ProgressBar processProgressBar;

	@UiField
	public CardLayoutContainer processCardLayoutContainer;
	
	@UiField
	public TextButton importButton;

	@UiHandler("importButton")
	public void handleSaveClick(SelectEvent e){
		if(simpleFileUpload.getValue() == null){
			ViewUtil.error("错误", "请先上传文件");
		}
		startImport(simpleFileUpload.getValue());
		importButton.setEnabled(false);
	}

	/**
	 * 需要在init后再调用一次reset
	 */
	protected void reset(boolean includeUploadResult) {
		templateLink.setHref(GWT.getHostPageBaseURL() + Global.EXCEL_TEMPLATE_DIR + "/" + getTemplateFileName());
		if(includeUploadResult){
			processCardLayoutContainer.setActiveWidget(uploadResult);
			uploadResult.setHTML("请上传文件后点击“导入”按钮开始");
			uploadResult.getElement().getStyle().setColor("red");
		}
		simpleFileUpload.reset();
		importButton.setEnabled(true);
	}
	
	protected void reset() {
		reset(true);
	}
	
	protected abstract String getTemplateFileName();

	protected abstract String getUploadDataProviderBeanName();
	
	@Override
	protected void onLoadData(Void reqType) {
		reset();
	}

	private void queryNext(final String tempFilePath){
		ClientManager.getFileInfClient().queryImport(tempFilePath, getUploadDataProviderBeanName(), new RestCallback<Double>(){
			@Override
			public void onSuccess(Method method, Double response) {
				boolean nextCheck = true;
				if(response >= 0 && response <= 1){
					processCardLayoutContainer.setActiveWidget(processProgressBar);
					processProgressBar.updateProgress(response, percentFormat.format(response*100) + "%");
				}else{
					processCardLayoutContainer.setActiveWidget(uploadResult);
				}
				if(response == Global.IMPORT_ERROR){
					uploadResult.setHTML("<span style='color:red'>导入失败！</span>");
					reset(false);
					nextCheck = false;
				}
				if(response == Global.IMPORT_SUCCESS_WITH_ERROR){
					uploadResult.setHTML("<span style='color:red'>部分导入成功！具体错误信息请查看 <a href='" + GWT.getHostPageBaseURL() + tempFilePath.replaceAll("\\\\", "/") + "' style='color:blue'>错误文件</a>，建议整理后重新导入</span>");
					reset(false);
					nextCheck = false;
				}
				if(response == Global.IMPORT_SUCCESS_ALL){
					uploadResult.setHTML("<span style='color:green'>导入成功！</span>");
					reset(false);
					nextCheck = false;
				}

				if(nextCheck){
					Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
						@Override
						public boolean execute() {
							if(asWidget().isAttached()){
								queryNext(tempFilePath);
							}
							return false; //窗体关闭则停止执行
						}
					}, 1000); //每隔1秒检查一次
				}
			}
		});
	}
	
	private void startImport(final FileInfDto fileInfDto) {
		ClientManager.getFileInfClient().startImport(fileInfDto.getFilePath(), getUploadDataProviderBeanName(), new RestCallback<String>(){
			@Override
			public void onSuccess(Method method, String response) {
				queryNext(fileInfDto.getFilePath());
			}
		});
	}

}
