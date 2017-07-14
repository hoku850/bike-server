package org.ccframe.commons.util;

import java.io.File;

import org.ccframe.client.Global;

import sun.misc.LRUCache;

import com.ipip.IP;

/**
 * IP对应地址库，LRU缓存大小1000.
 * @author JIM
 *
 */
public class IpZoneUtils {
	
	//17monipdb.dat IP地址库位置
	private static final String IP_CITY_DATA_PATH =  File.separator + "WEB-INF" + File.separator + "17monipdb.dat";
	private static final int CACHE_CAPACITY = 1000;

	private static boolean loaded = false;
	
	private static LRUCache<String, String[]> lruCache = new LRUCache<String,String[]>(CACHE_CAPACITY) {
		@Override
		protected String[] create(String ipStr) {
			return IP.find(ipStr);
		}
		@Override
        protected boolean hasName(String[] p, String s) {
            return p.equals(s);
        }
    };

	
	public static void init(){
		IP.load(System.getProperty(Global.WEB_ROOT_PROPERTY) + IP_CITY_DATA_PATH);
		loaded = true;
	}
	
	public static String[] getZone(String ip){
		if(!loaded){
			init();
		}
		return lruCache.forName(ip);
	}
	
}
