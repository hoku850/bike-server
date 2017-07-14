package org.ccframe.commons.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class ImageProxy extends HashMap<String, String> {
    private static Logger logger = Logger.getLogger(ImageProxy.class);
    private String fileId;

    public ImageProxy(String fileId) {
        this.fileId = fileId;
    }

    /**
     * 通过图片规格取得图片地址，如在jstl中使用 ${imageProxy['100X100']} 则返回 http://www.xxx.com/upload/2011/01/02/21554-5-2-1-55-a255_100X100.jpg
     * 如果要取原图，就传一个空串即可 ${imageProxy['']} -> http://www.xxx.com/upload/2011/01/02/21554-5-2-1-55-a255.jpg
     *
     * @param key
     * @return
     */
    public String get(Object key) {
    	//TODO 使用ImageUtils生成规格图片大小.
    	return "";
    }

    public String toString(){
        return get("");
    }
}
