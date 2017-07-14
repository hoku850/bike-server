package org.ccframe.client.commons;

import org.ccframe.client.ResGlobal;
import org.ccframe.subsys.core.dto.ErrorObjectResp;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;

public abstract class RestCallback<T> implements MethodCallback<T> {

	private static final String ERROR_OBJECT_RESP = "errorObjectResp";
	
	@Override
	public void onFailure(final Method method, Throwable exception) {
		if(method.getResponse().getStatusCode() == Response.SC_INTERNAL_SERVER_ERROR){
			GWT.log(method.getResponse().getText());
			JSONObject errorObject = ((JSONObject) JSONParser.parseStrict(method.getResponse().getText())).get(ERROR_OBJECT_RESP).isObject();
			final String errorCode = errorObject.get(ErrorObjectResp.ERROR_CODE).isString().stringValue();
			final String errorText = errorObject.get(ErrorObjectResp.ERROR_TEXT).isString().stringValue();
			ViewUtil.messageBox(ResGlobal.ERRORS_EXCEPTION.equals(errorCode) ? "系统错误" : "系统信息", errorText,new Runnable(){
				@Override
				public void run() {
					afterFailure();
				}
			});
		}else if(method.getResponse().getStatusCode() == Response.SC_FORBIDDEN){
			ViewUtil.messageBox("系统信息", "操作超时，您需要重新登录", new Runnable(){
				@Override
				public void run() {
					redirect(GWT.getHostPageBaseURL() + method.getResponse().getText() + Window.Location.getQueryString());
				}
			});
		}else{
			ViewUtil.error("错误", exception.getMessage());
		}
	}
	
	protected void afterFailure(){
		
	}

	//redirect the browser to the given url
	public static native void redirect(String url)/*-{
		$wnd.location = url;
	}-*/;
}
