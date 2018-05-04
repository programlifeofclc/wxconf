package cn.wx.web.conf.compon;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import cn.wx.web.util.KV;
import cn.wx.web.util.HttpClient4;

/**
 * JsTicketToken获取类
 * @author clc
 */
public class JsTicketToken {

	private static String ticket_token;

	private static long expires;

	private static long startTime;

	/**
	 * 获取访问token
	 * 
	 * @return
	 */
	public synchronized static String getTicketToken() {
		if (ticket_token == null) {
			goJsTicketToken();
		}

		if ((new Date()).getTime() - startTime > expires) {
			goJsTicketToken();
		}

		return ticket_token;
	}

	private static void goJsTicketToken() {
		String ticket_json = HttpClient4.doGet(KV.JS_TICKET_TOKEN_RRL 
													+ "?access_token=" + AccessToken.getAccessToken() 
													+ "&type=jsapi");
		JSONObject json = JSONObject.parseObject(ticket_json);
		if (json.containsKey("errmsg") && json.get("errmsg").equals("ok")) {
			System.out.println("微信取ticket_tokenc成功：" + json);
			ticket_token = json.getString("ticket");
			expires = json.getLong("expires_in") * 1000 - 600 * 1000 ;
			startTime = (new Date()).getTime();
		} else {
			System.out.println("微信取ticket_token错误：" + json);
		}
	}

}
