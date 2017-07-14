package com.google.gwt.http.client;

import com.google.gwt.xhr.client.XMLHttpRequest;

/**
 * @author JIM
 *
 */
public class CcRequest extends Request {
	public CcRequest(XMLHttpRequest xmlHttpRequest, int timeoutMillis, final RequestCallback callback) {
		super(xmlHttpRequest, timeoutMillis, callback);
	}

	public void fireOnResponseReceived(RequestCallback callback) {
		super.fireOnResponseReceived(callback);
	}
}
