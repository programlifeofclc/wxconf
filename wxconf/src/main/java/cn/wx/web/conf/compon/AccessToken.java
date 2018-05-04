package cn.wx.web.conf.compon;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import cn.wx.web.util.KV;
import cn.wx.web.util.HttpClient4;

/**
 * access_token获取类
 * 
 * @author clc
 */
public class AccessToken {

	private static String access_token;

	private static long expires;

	private static long startTime;

	/**
	 * 获取访问token
	 * 
	 * @return
	 */
	public synchronized static String getAccessToken() {
		if (access_token == null) {
			goWxGetToken();
		}

		if ((new Date()).getTime() - startTime > expires) {
			goWxGetToken();
		}

		return access_token;
	}

	private static void goWxGetToken() {
		String url = KV.ACCESS_TOKEN_URL;
		String appid = KV.APP_ID;
		String secret = KV.APP_SECRET;
		String access_json = HttpClient4.doGet(url + "?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
		JSONObject json = JSONObject.parseObject(access_json);
		if (!json.containsKey("errcode")) {
			access_token = json.getString("access_token");
			expires = json.getLong("expires_in") * 1000 - 600 * 1000 ;
			startTime = (new Date()).getTime();
		} else {
			System.out.println("微信取token错误：" + json);
		}
	}

}
