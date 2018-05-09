package cn.wx.web.conf.compon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;
import cn.wx.web.util.Tools;

/**
 * access_token获取类
 * @author clc
 */
public class PageAccessToken {
	
	private static Logger logger = LogManager.getLogger(PageAccessToken.class);
	
	private static Map<String,Entry> map = new HashMap<>();
	
	/**
	 * 获得pagetoken
	 * @param code
	 * @return
	 */
	public static String getPageToken(String code){
		//页面token获取
		String url = KV.PAGE_TOKEN_URL + "?appid=" + KV.APP_ID + "&secret=" + KV.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
		logger.info("url:" + url);
		String access_json = HttpClient4.doGet(url);
		logger.info("pagetoken:" + access_json);
		JSONObject json = JSON.parseObject(access_json);
		if (!json.containsKey("errcode")) {
			String access_token = json.getString("access_token");
			String refresh_token = json.getString("refresh_token");
			Long expires = json.getLong("expires_in") * 1000;
			String openid = json.getString("openid");
			return addEntry(access_token, refresh_token, expires, openid);
		}
		return null;
	}
	
	private synchronized static String addEntry(String access_token, String refresh_token, long expires, String openid) {
		Entry entry = new Entry(access_token, refresh_token, expires, openid);
		String openidMd5 = Tools.getMd5(openid);
		map.put(openidMd5, entry);
		return openidMd5;
	}
	
	public static String getOpenIdByIdMd5(String openIdMd5) {
		if(haveAccToken(openIdMd5)){
			Entry entry = map.get(openIdMd5);
			logger.info("openid:" + entry.getOpenid());
			return entry.getOpenid();
		}
		return null;
	}
	
	public static JSONObject getAccToken(String openIdMd5) {
		if(haveAccToken(openIdMd5)){
			Entry entry = map.get(openIdMd5);
			entry.refreshAccessToken();
			JSONObject jo = (JSONObject)JSONObject.toJSON(entry);
			logger.info("acctoken:" + jo);
			return jo;
		}
		return null;
	}
	
	
	public static Boolean haveAccToken(String openid) {
		return map.containsKey(openid);
	}
	
	
	public static class Entry{
		
		private String access_token;

		private String refresh_token;

		private long expires;
		
		private long startTime;
		
		private String openid;
		
		public Entry(String access_token, String refresh_token, long expires ,String openid) {
			this.access_token = access_token;
			this.refresh_token = refresh_token;
			this.expires = expires - 600 * 1000;
			this.openid = openid;
			this.startTime = (new Date()).getTime();
		}
		
		/**
		 *刷新token
		 */
		public synchronized void refreshAccessToken() {
			if (access_token == null) {
				goWxGetPageToken();
			}
			if ((new Date()).getTime() - startTime > expires) {
				goWxGetPageToken();
			}
		}


		private void goWxGetPageToken() {
			String url = KV.PAGE_REF_TOKEN_URL 
							+ "?appid=" + KV.APP_ID 
							+ "&grant_type=refresh_token" 
							+ "&refresh_token=" + refresh_token;
			String access_json = HttpClient4.doGet(url);
			JSONObject json = JSONObject.parseObject(access_json);
			if (!json.containsKey("errcode")) {
				this.access_token = json.getString("access_token");
				this.expires = json.getLong("expires_in") * 1000;
				this.startTime = (new Date()).getTime();
				logger.info("微信取page-token成功：" + json);
			} else {
				logger.info("微信取page-token错误：" + json);
			}
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		public String getRefresh_token() {
			return refresh_token;
		}

		public void setRefresh_token(String refresh_token) {
			this.refresh_token = refresh_token;
		}

		public long getExpires() {
			return expires;
		}

		public void setExpires(long expires) {
			this.expires = expires;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}
		
	}

}
