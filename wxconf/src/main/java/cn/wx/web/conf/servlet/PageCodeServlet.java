package cn.wx.web.conf.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.wx.web.util.KV;

@WebServlet(urlPatterns={"/pageCode"})
public class PageCodeServlet extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageCodeServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("微信页面code申请");
		//进行验证 是否是微信端请求
		String url = KV.PAGE_CODE_URL 
						+ "?appid=" + KV.APP_ID 
						+ "&redirect_uri=" + KV.PAGE_REDIRECT_URI 
						+ "&response_type=code&scope=snsapi_userinfo" 
						+ "&state=" + new Date().getTime() + "#wechat_redirect";
		resp.sendRedirect(url);
		logger.info("微信页面code申请完毕等待回调");
	}

}
