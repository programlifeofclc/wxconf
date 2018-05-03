package cn.wx.web.conf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@WebServlet(urlPatterns={"/pageCode"})
public class PageCode extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageCode.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("微信页面code申请");
		resp.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5e45d21d2c0e4bb1&redirect_uri=http%3a%2f%2fsanfangzhifu.95081.cn%2fwxconf%2fpageToken&response_type=code&scope=snsapi_userinfo&state=456#wechat_redirect");
		logger.info("微信页面code申请完毕等待回调");
	}
	

}
