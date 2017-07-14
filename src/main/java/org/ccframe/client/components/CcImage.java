package org.ccframe.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.Image;

/**
 * 弥补GWT的Image不支持contextPath的不足，以便在XML里直接设置相对路径.
 * @author JIM
 *
 */
public class CcImage extends Image{

	public void setContextUrl(String url){
		String newUrl = GWT.getHostPageBaseURL()+ (GWT.getHostPageBaseURL().endsWith("/") ? "" : "/") + url;
		setUrl(UriUtils.fromTrustedString(newUrl));
	}
}
