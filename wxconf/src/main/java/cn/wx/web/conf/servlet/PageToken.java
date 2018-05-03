package cn.wx.web.conf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.wx.web.conf.compon.PageAccessToken;
import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;

@WebServlet(urlPatterns={"/pageToken"})
public class PageToken extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageToken.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("获得code");
		String code = getPar(req, "code");
		
		String state = getPar(req, "state");
		logger.info("code:" + code + "state:" + state);
		String url = KV.PAGE_TOKEN_URL + "?appid=" + KV.APP_ID + "&secret=" + KV.APP_SECRET + "&code=" + code + "&grant_type=authorization_code";
		logger.info("url:" + url);
		String access_json = HttpClient4.doGet(url);
		JSONObject json = JSONObject.parseObject(access_json);
		logger.info("json:" + json);
		if (!json.containsKey("errcode")) {
			String access_token = json.getString("access_token");
			String refresh_token = json.getString("refresh_token");
			Long expires = json.getLong("expires_in") * 1000;
			String openid = json.getString("openid");
			PageAccessToken.addEntry(access_token, refresh_token, expires, openid);
			String info_url = KV.PAGE_USER_INFO_URL 
								+ "?access_token=" + access_token 
								+ "&openid=" + openid 
								+ "&lang=zh_CN";
			
			String rjson = HttpClient4.doGet(info_url);
			JSONObject rjjson = JSONObject.parseObject(rjson);
			resp.addCookie(new Cookie("userinfo",rjjson.toJSONString()));
			resp.addCookie(new Cookie("openid",openid));
		} else {
			logger.info("微信取page-token错误：" + json);
		}
		logger.info("微信页面token申请完毕");
		resp.sendRedirect("/wxconf/index.html");
	}
	

}
