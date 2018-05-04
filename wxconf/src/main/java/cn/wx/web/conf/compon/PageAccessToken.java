package cn.wx.web.conf.compon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;

/**
 * access_token获取类
 * @author clc
 */
public class PageAccessToken {
	
	private static Logger logger = LogManager.getLogger(PageAccessToken.class);
	
	private static Map<String,Entry> map = new HashMap<>();
	
	public synchronized static void addEntry(String access_token, String refresh_token, long expires, String openid) {
		Entry entry = new Entry(access_token, refresh_token, expires);
		map.put(openid, entry);
	}
	
	public static String getAccToken(String openid) {
		Entry entry = map.get(openid);
		return entry.getAccessToken();
	}
	
	public static class Entry{
		
		private String access_token;

		private String refresh_token;

		private long expires;
		
		private long startTime;
		
		public Entry(String access_token, String refresh_token, long expires) {
			this.access_token = access_token;
			this.refresh_token = refresh_token;
			this.expires = expires;
			this.startTime = (new Date()).getTime();
		}
		
		/**
		 * 获取访问token
		 * 
		 * @return
		 */
		public synchronized String getAccessToken() {
			if (access_token == null) {
				goWxGetPageToken();
			}
			if ((new Date()).getTime() - startTime > expires) {
				goWxGetPageToken();
			}
			return access_token;
		}


		private void goWxGetPageToken() {
			String url = KV.PAGE_REF_TOKEN_URL 
							+ "?appid=" + KV.APP_ID 
							+ "&grant_type=refresh_token" 
							+ "&refresh_token=" + refresh_token;
			String access_json = HttpClient4.doGet(url);
			JSONObject json = JSONObject.parseObject(access_json);
			if (!json.containsKey("errcode")) {
				access_token = json.getString("access_token");
				expires = json.getLong("expires_in") * 1000;
				startTime = (new Date()).getTime();
				logger.info("微信取page-token成功：" + json);
			} else {
				logger.info("微信取page-token错误：" + json);
			}
		}
	}

}
