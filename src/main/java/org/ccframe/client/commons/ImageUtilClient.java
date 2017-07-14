package org.ccframe.client.commons;

public class ImageUtilClient {
	
	private static final String[] IMAGE_EXTENTION = new String[]{"jpg","jpeg","jpe","gif","png"}; 
	
	private ImageUtilClient(){}
	public static boolean isImageExtention(String extention){
		String lowStr = extention.toLowerCase();
		for(String extentionStr: IMAGE_EXTENTION){
			if(extentionStr.equals(lowStr)){
				return true;
			}
		}
		return false;
	}

	public static String insertFileNameSuffixToUrl(String url, String suffix){
		int index = url.lastIndexOf('.');
		if(index > 0){
			StringBuilder sb = new StringBuilder(url);
			sb.insert(index, suffix);
			return sb.toString();
		}
		return url;
	}
}
