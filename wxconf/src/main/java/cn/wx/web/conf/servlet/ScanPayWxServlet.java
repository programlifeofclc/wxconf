package cn.wx.web.conf.servlet;

import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import cn.wx.web.conf.compon.PageAccessToken;
import cn.wx.web.conf.compon.UniqueCache;
import cn.wx.web.conf.compon.pay.WxScanPay;
import cn.wx.web.util.CodeImg;
import cn.wx.web.util.CookieUtils;
import cn.wx.web.util.KV;

/**
 * 微信配置类 事件处理类
 * 
 * @author clc
 * @date 2018/04/24
 */

@WebServlet(urlPatterns = { "/sanpay" })
public class ScanPayWxServlet extends BaseServlet {

	private static Logger logger = Logger.getLogger(ScanPayWxServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * 收到请求
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String openid = CookieUtils.getCookieValue(req, "openid");
			logger.info("unorder :" + openid);
			if(!UniqueCache.VerifyUnique(ScanPayWxServlet.class, openid ,5000L)){
				return;
			}
			String out_trade_no = new Date().getTime() + "";
			int  total_fee = 1;
			String body = "管家帮好酒";
			openid =  PageAccessToken.getOpenIdByIdMd5(openid);
			String  spbill_create_ip = getIp(req);
			WxScanPay wjp = new WxScanPay();
			Map<String, String> map = wjp.unifiedOrder(openid, body, out_trade_no, total_fee, spbill_create_ip, KV.NOTIFY_URL); 
			logger.info("扫码下单下单加签数据:" + JSON.toJSONString(map));
			String http = map.get("code_url");
			CodeImg.getImgToStream(http , 300, 300, "png", resp.getOutputStream());
		} catch (Exception e) {
			out(resp, "false");
			e.printStackTrace();
		}
	}

	 
	
}
