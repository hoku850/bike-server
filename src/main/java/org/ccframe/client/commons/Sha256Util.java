package org.ccframe.client.commons;


/**
 * 使用jquery的
 * @author JIM
 *
 */
public class Sha256Util {
	
	public static native String encode(String data) /*-{
		return $wnd.jQuery.sha512(data);
	}-*/;
}
