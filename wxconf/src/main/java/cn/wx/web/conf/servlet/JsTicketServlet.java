package cn.wx.web.conf.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import cn.wx.web.conf.compon.JsTicketToken;
import cn.wx.web.util.KV;
import cn.wx.web.util.ReturnBean;
import cn.wx.web.util.Tools;

@WebServlet(urlPatterns={"/jsTicket"})
public class JsTicketServlet extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(JsTicketServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ReturnBean rb =	new ReturnBean();
			logger.info("获得ticket");
			String jsapi_ticket = JsTicketToken.getTicketToken();
			logger.info("ticket:" + jsapi_ticket);
			String url = getPar(req, "url");
			Long timestamp = new Date().getTime();
			String noncestr = Tools.getMd5(timestamp.toString());
			String signature = SHA1.getUrlSHA1("url=" + url, 
												"timestamp=" + timestamp.toString(), 
												"noncestr=" + noncestr,
												"jsapi_ticket=" + jsapi_ticket);

			JSONObject jo = new JSONObject();
			jo.put("timestamp", timestamp);
			jo.put("noncestr", noncestr);
			jo.put("signature", signature);
			jo.put("appId", KV.APP_ID);
			rb.setMsgcode("1");
			rb.setData(jo);
			String json = rb.toJson();
			logger.info("加签返回json:" + json);
			out(resp, json);
		} catch (AesException e) {
			logger.error("js签名失败");
			e.printStackTrace();
		}
	}
}
