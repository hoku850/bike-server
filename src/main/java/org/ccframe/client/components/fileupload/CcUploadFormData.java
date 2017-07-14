package org.ccframe.client.components.fileupload;

import org.vectomatic.file.File;

import com.google.gwt.core.client.JavaScriptObject;

public class CcUploadFormData extends JavaScriptObject {
	
	protected CcUploadFormData(){}
	
	public final native void append(String name, File file) /*-{
		this.append(name, file);
	}-*/;
	
	public static native CcUploadFormData create() /*-{
		return new FormData();
	}-*/;

	public final native void append(String name, String value) /*-{
		this.append(name, value);
	}-*/;
}
