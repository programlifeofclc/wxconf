package cn.wx.web.conf.compon;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeCache {

	public static Map<String,Long> codemap = new ConcurrentHashMap<>();
	
	public static Boolean addCode(String code){
		Long currt = new Date().getTime();
		if(codemap.containsKey(code) && (currt - codemap.get(code)) < 5 * 60 * 1000){
			return false;
		}
		codemap.put(code ,new Date().getTime());
		return true;
	}
}
