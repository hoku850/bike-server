package org.ccframe.client.components.fileupload;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.subsys.core.domain.entity.FileInf;
import org.ccframe.subsys.core.dto.FileInfDto;
import org.vectomatic.file.File;
import org.vectomatic.file.FileUploadExt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.CcRequest;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestPermissionException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

/**
 * HTML5文件上传组件基类.
 * 支持批量上传、文件名过滤。
 * @author JIM
 *
 */
public abstract class CcBaseFileUpload extends CardLayoutContainer{

	private static final long MAX_FILE_SIZE = 50l*1024*1024; //默认限制大小为50M
	private static final String UPLOAD_URL = GWT.getHostPageBaseURL() + ControllerMapping.ADMIN_FILE_INF_BASE + "/" + ControllerMapping.ADMIN_FILE_INF_UPLOAD + Global.REST_REQUEST_URL_SUFFIX;

	public static final String FILTER_IMAGE = ".gif,.jpg,.jpeg,.png,image/gif,image/jpeg,image/png";
	public static final String FILTER_PDF = ".pdf,application/pdf";
	public static final String FILTER_OFFICE_EXCEL = ".xls,.xlsx,application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; //EXCEL数据上传比较特殊，要单独出来
	public static final String FILTER_CSV = ".csv";
	public static final String FILTER_OFFICE_WORD = "application/vnd.ms-powerpoint,application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static final String FILTER_OFFICE_PPT = "application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String FILTER_TEXT = "text/*"; //纯文本上传
	public static final String FILTER_COMPRESS_FILE = ".zip,.7z,.rar,aplication/zip,application/rar,application/x-7z-compressed,application/octet-stream"; //压缩数据上传用
	
	private CcXMLHttpRequest xmlHttpRequest = XMLHttpRequest.create().cast();

	private FileUploadExt fileUploadExt;
	private LinkedList<File> fileQueue = new LinkedList<File>();
	private File workFile;
	private int totalFileCount;
	private long totalBytesToUpload; //队列文件全部大小
	private long queueLoadBase; //之前完成上传的文件大小，用于计算整理比例

	private FileUploadCompleteHandler fileUploadCompleteHandler;
	
	public void setFileUploadCompleteHandler(FileUploadCompleteHandler fileUploadCompleteHandler){
		this.fileUploadCompleteHandler = fileUploadCompleteHandler;
	}

	protected abstract void onNextFileUpload(int current, int total);
	protected abstract void onQueueUploadFinish();
	protected abstract void onStartUpload();
	protected abstract void onUpdateProgress(double totoalLoadedBytes, long totalBytesToUpload);
	protected abstract void initWidget();

	public CcBaseFileUpload(){
		this(false, null);
	}

	private String getGroupFilter(String filterGroup){
		if(filterGroup != null){
			List<String> filterList = new ArrayList<String>();
			for(String group: filterGroup.split("\\|")){
				if("FILTER_IMAGE".equals(group)){
					filterList.add(FILTER_IMAGE);
				}else if("FILTER_PDF".equals(group)){
					filterList.add(FILTER_PDF);
				}else if("FILTER_OFFICE_EXCEL".equals(group)){
					filterList.add(FILTER_OFFICE_EXCEL);
				}else if("FILTER_OFFICE_WORD".equals(group)){
					filterList.add(FILTER_OFFICE_WORD);
				}else if("FILTER_CSV".equals(group)){
					filterList.add(FILTER_CSV);
				}else if("FILTER_OFFICE_PPT".equals(group)){
					filterList.add(FILTER_OFFICE_PPT);
				}else if("FILTER_TEXT".equals(group)){
					filterList.add(FILTER_TEXT);
				}else if("FILTER_COMPRESS_FILE".equals(group)){
					filterList.add(FILTER_COMPRESS_FILE);
				}else{
					filterList.add(group); //可以直接指定输出
				}
			}
			return StringUtils.join(filterList, ",");
		}else{
			return "*.*";
		}
		
	}
	
	public CcBaseFileUpload(final boolean multiple, String filterGroup){
		
		
		getElement().getStyle().setPosition(Style.Position.RELATIVE);
		fileUploadExt = new FileUploadExt();
	    fileUploadExt.setWidth("0px");
	    fileUploadExt.setVisible(false);
	    fileUploadExt.setMultiple(multiple);
	    fileUploadExt.getElement().setAttribute("accept", getGroupFilter(filterGroup));
	    this.add(fileUploadExt);

	    fileUploadExt.setName("file");
	    fileUploadExt.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				totalBytesToUpload = 0;
				queueLoadBase = 0;
				for (File file :fileUploadExt.getFiles()){
					if(file.getSize() < MAX_FILE_SIZE){
						fileQueue.add(file);
						totalBytesToUpload += file.getSize();
					}else if(multiple == false){
						ViewUtil.error("错误", "上传文件超过限制：" + MAX_FILE_SIZE/1024/1024 + "M");
						fileUploadCompleteHandler.onUploadError(workFile);
						return;
					}
				}
				totalFileCount = fileQueue.size();

				Scheduler.get().scheduleFixedDelay(new RepeatingCommand(){
					@Override
					public boolean execute() {
						if(!CcBaseFileUpload.this.isAttached() || fileQueue.isEmpty()){ //终止传输条件，关闭或者队列传输完成
							return false;
						}
						if(workFile != null){ //retry条件：一个传输完成，等待下一个传输
							return true;
						}
						workFile = fileQueue.remove();
						onNextFileUpload((totalFileCount - fileQueue.size()), totalFileCount);
						try{
							CcUploadFormData ccUploadFormData = CcUploadFormData.create();
							ccUploadFormData.append("file", workFile);
							xmlHttpRequest.open("POST", UPLOAD_URL);

							final RequestCallback callback = new RequestCallback(){
								@Override
								public void onResponseReceived(Request request, Response response) {
									switch(response.getStatusCode()){
										case 404:
											break;
										case 500:
											JSONObject object500 = JSONParser.parseStrict(response.getText()).isObject();
											if(object500 != null){
												JSONValue errorObjectResp = object500.get("errorObjectResp");
												if(errorObjectResp != null){
													String errorText = errorObjectResp.isObject().get("errorText").isString().stringValue();
													ViewUtil.error("错误", errorText.contains("SizeLimitExceededException") ? "上传文件超过限制" : errorText);
												}
												fileUploadCompleteHandler.onUploadError(workFile);
											}
											break;
										case 200:
											JSONObject object200 = JSONParser.parseStrict(response.getText()).isObject();
											if(object200 != null){
												FileInfDto fileInfBarDto = new FileInfDto();
												fileInfBarDto.setFileNm(object200.get(FileInf.FILE_NM).isString().stringValue());
												fileInfBarDto.setFilePath(object200.get(FileInf.FILE_PATH).isString().stringValue());
												fileInfBarDto.setFileTypeNm(object200.get(FileInf.FILE_TYPE_NM).isString().stringValue());
												fileInfBarDto.setFileUrl(object200.get(FileInfDto.FILE_URL).isString().stringValue());
												if(fileUploadCompleteHandler != null){
													fileUploadCompleteHandler.onUploadComplete(fileInfBarDto);
												}
											}
											break;
									}
									if(fileQueue.isEmpty()){
										onQueueUploadFinish();
									}
								}

								@Override
								public void onError(Request request, Throwable exception) {
									ViewUtil.error("上传失败", exception.toString());
								}
						    };
						    final CcRequest request = new CcRequest(xmlHttpRequest, 1800*1000, callback); //传输限制30分钟
						    xmlHttpRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
						      public void onReadyStateChange(XMLHttpRequest xhr) {
						        if (xhr.getReadyState() == XMLHttpRequest.DONE) {
						          xhr.clearOnReadyStateChange();
						          request.fireOnResponseReceived(callback);
						        }
						      }
						    });

							xmlHttpRequest.send(ccUploadFormData);
						}catch (JavaScriptException e) {
							RequestPermissionException requestPermissionException = new RequestPermissionException(UPLOAD_URL);
							requestPermissionException.initCause(new RequestException(e.getMessage()));
							GWT.log(e.getMessage());
					    }
						return !fileQueue.isEmpty();
					}
				}, 500);
				
				onStartUpload();
			}
	    });
        addProgressHandler(new ProgressCallback(){
			@Override
			public void onProcess(boolean lengthComputable, double loaded, double total) {
				if(lengthComputable){
					double totoalLoadedBytes = queueLoadBase + loaded;
					onUpdateProgress(totoalLoadedBytes, totalBytesToUpload);
				}
			}
        });
        addCompleteHandler(new Runnable(){
			@Override
			public void run() { //上传完毕，但是等服务器处理请求.
				queueLoadBase += workFile.getSize();
				onUpdateProgress((double)queueLoadBase, totalBytesToUpload);
				workFile = null;
			}
        });

        initWidget();
    }
	
	protected FileUploadExt getFileUploadExt(){
		return fileUploadExt;
	}
	
	protected File getWorkFile(){
		return workFile;
	}
	
	private native void addProgressHandler(ProgressCallback progressCallback)/*-{
		var listener = function(event){
			progressCallback.@org.ccframe.client.components.fileupload.ProgressCallback::onProcess(ZDD)(event.lengthComputable,event.loaded,event.total);
		};
		this.@org.ccframe.client.components.fileupload.CcBaseFileUpload::xmlHttpRequest.upload.addEventListener("progress", listener, false);
	}-*/;

	private native void addCompleteHandler(Runnable callback)/*-{
		var listener = function(event){
			callback.@java.lang.Runnable::run()();
		};
		this.@org.ccframe.client.components.fileupload.CcBaseFileUpload::xmlHttpRequest.upload.addEventListener("load", listener, false);
	}-*/;

	private native void addCanceledHandler()/*-{
		var listener = function(event){
			alert('abort');
		};
		this.@org.ccframe.client.components.fileupload.CcBaseFileUpload::xmlHttpRequest.upload.addEventListener("abort", listener, false);
	}-*/;

}
