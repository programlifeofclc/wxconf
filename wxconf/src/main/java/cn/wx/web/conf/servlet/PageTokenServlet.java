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

import cn.wx.web.conf.compon.CodeCache;
import cn.wx.web.conf.compon.PageAccessToken;
import cn.wx.web.conf.compon.UserInfoCache;
import cn.wx.web.util.ReturnBean;

@WebServlet(urlPatterns={"/pageToken"})
public class PageTokenServlet extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageTokenServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("获得code");
		String code = getPar(req, "code");
		String state = getPar(req, "state");
		logger.info("code:" + code + "state:" + state);
		if(CodeCache.addCode(code)){
			String openidMd5 = PageAccessToken.getPageToken(code);
			logger.info("openidMd5:" + openidMd5);
			if(openidMd5 != null){
				logger.info("获取用户*");
				JSONObject json = UserInfoCache.getUserInfo(openidMd5);
				resp.addCookie(new Cookie("openid",openidMd5));
				resp.addCookie(new Cookie("userInfo",json.toJSONString()));
			}
			logger.info("微信页面token申请完毕");
			resp.sendRedirect("/wxconf/index.html");
		}else{
			logger.info("重复访问：***:" + code + "state:" + state);
		}
	}
	
	public static void main(String[] args) {
		ReturnBean n = null;
		System.out.println("daf" + n);
	}

}
