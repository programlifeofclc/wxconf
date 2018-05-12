package cn.wx.web.conf.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@WebServlet(urlPatterns={"/index"})
public class IndexServlet extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(IndexServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("微信首页面");
		//进行验证 是否是微信端请求
		String url = "http://sanfangzhifu.95081.cn/wxconf/index.html?atime=" + new Date().getTime();
		resp.sendRedirect(url);
		logger.info("微信页面:" + url);
	}

}
