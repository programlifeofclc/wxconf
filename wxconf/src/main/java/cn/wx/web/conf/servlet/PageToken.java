package cn.wx.web.conf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@WebServlet(urlPatterns={"/pageToken"})
public class PageToken extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageToken.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("获得code");
		String code = getPar(req, "code");
		String state = getPar(req, "state");
		https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		resp.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx5e45d21d2c0e4bb1&redirect_uri=http%3a%2f%2fsanfangzhifu.95081.cn%2fwxconf%2findex.html&response_type=code&scope=snsapi_userinfo&state=456#wechat_redirect");
		logger.info("微信页面code申请完毕等待回调");
	}
	

}
