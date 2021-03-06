package cn.wx.web.conf.servlet;

import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.wx.web.conf.compon.PageAccessToken;
import cn.wx.web.conf.compon.UniqueCache;
import cn.wx.web.conf.compon.pay.WxJsPay;
import cn.wx.web.util.CookieUtils;
import cn.wx.web.util.KV;
import cn.wx.web.util.ReturnBean;

/**
 * 微信配置类 事件处理类
 * 
 * @author clc
 * @date 2018/04/24
 */

@WebServlet(urlPatterns = { "/pay" })
public class PayWxServlet extends BaseServlet {

	private static Logger logger = Logger.getLogger(PayWxServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * 收到请求
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String openid = CookieUtils.getCookieValue(req, "openid");
			logger.info("unorder :" + openid);
			if(!UniqueCache.VerifyUnique(PayWxServlet.class, openid ,5000L)){
				return;
			}
			String out_trade_no = new Date().getTime() + "";
			int  total_fee = 1;
			String body = "管家帮好酒";
			openid =  PageAccessToken.getOpenIdByIdMd5(openid);
			String  spbill_create_ip = getIp(req);
			WxJsPay wjp = new WxJsPay();
			Map<String, String> map = wjp.unifiedOrder(openid, body, out_trade_no, total_fee, spbill_create_ip, KV.NOTIFY_URL); 
			map = wjp.order2Ajax(map);
			ReturnBean rb = new ReturnBean();
			rb.setData(map);
			rb.setMsgcode("1");
			logger.info("下单加签数据:" + rb.toJson());
			out(resp, rb.toJson());
		} catch (Exception e) {
			out(resp, "false");
			e.printStackTrace();
		}
	}

	 
	
}
