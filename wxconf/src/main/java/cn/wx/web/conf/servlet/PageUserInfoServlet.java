package cn.wx.web.conf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.wx.web.conf.compon.UserInfoCache;
import cn.wx.web.util.CookieUtils;
import cn.wx.web.util.ReturnBean;

@WebServlet(urlPatterns={"/userInfo"})
public class PageUserInfoServlet extends BaseServlet{
	
	private static Logger logger = LogManager.getLogger(PageUserInfoServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("获得用户信息");
		ReturnBean rb =	new ReturnBean();
		String openidMd5 = CookieUtils.getCookieValue(req, "openid");
		if(openidMd5 == null || openidMd5.equals("")){
			rb.setMsgcode("-1");
			rb.setMsg("openid is null");
		}else{
			JSONObject json = UserInfoCache.getUserInfo(openidMd5);
			if(json == null){
				rb.setData(json);
			}else{
				rb.setMsgcode("没有用户信息");
				rb.setMsgcode("-1");
			}
		}
		out(resp, rb.toJson());
	}

}
