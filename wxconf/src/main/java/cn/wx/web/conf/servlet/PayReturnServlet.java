package cn.wx.web.conf.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.wx.web.util.Tools;
import cn.wx.web.util.XmlDom;

/**
 * 微信配置类 事件处理类
 * 
 * @author clc
 * @date 2018/04/24
 */

@WebServlet(urlPatterns = { "/payreturn" })
public class PayReturnServlet extends BaseServlet {

	private static Logger logger = Logger.getLogger(PayReturnServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * 收到请求
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			
			ServletInputStream in = req.getInputStream();
			String postData = Tools.stream2String(in);
			logger.info("支付回调:" + postData);
			Map<String,String> map = new HashMap<>();
			map.put("return_code", "SUCCESS");
			map.put("return_msg", "OK");
			resp.getWriter().println(XmlDom.mapToXml(map));
			resp.getWriter().close();
		} catch (Exception e) {
			out(resp, "false");
			e.printStackTrace();
		}
	}
	
}
