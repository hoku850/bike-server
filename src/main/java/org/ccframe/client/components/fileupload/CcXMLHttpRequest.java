package org.ccframe.client.components.fileupload;

import com.google.gwt.xhr.client.XMLHttpRequest;

public class CcXMLHttpRequest extends XMLHttpRequest {
	
	protected CcXMLHttpRequest(){}
	
	public final native void send(CcUploadFormData data) /*-{
		this.send(data);
	}-*/;
}
