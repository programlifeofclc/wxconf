package cn.wx.web.conf.compon.pay;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;
import cn.wx.web.util.Tools;
import cn.wx.web.util.XmlDom;
/**
 * 公众号支付
 * @author Administrator
 */
public class WxJsPay {
	
	private static Logger logger = Logger.getLogger(WxJsPay.class);
	
	public static enum SIGN_TYPE {MD5 , SHA256};
	
	public SIGN_TYPE sign_type;
	
	public WxJsPay() {
		new WxJsPay(SIGN_TYPE.MD5);
	}
	
	
	public WxJsPay(SIGN_TYPE sign_type) {
		this.sign_type = sign_type;
	}
	
	
	/**
	 * 统一下单
	 * @param openid
	 * @param body
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param notify_url
	 */
	public Map<String, String> unifiedOrder(String openid, String body, String out_trade_no, int total_fee, String spbill_create_ip, String notify_url) {
		try {
			Map<String, String> map = new HashMap<>();
			map.put("appid", KV.APP_ID);
			map.put("mch_id", KV.MCH_ID);
			map.put("nonce_str", Tools.randomStr(32));
			map.put("trade_type", "JSAPI");
			map.put("openid", openid);
			map.put("body", body);
			map.put("out_trade_no", out_trade_no);
			map.put("total_fee", total_fee + "");
			map.put("spbill_create_ip", spbill_create_ip);
			map.put("notify_url", notify_url);
			if (sign_type == SIGN_TYPE.SHA256) {
				map.put("sign_type", "HMAC-SHA256");
			} else {
				map.put("sign_type", "MD5");
			}
			String sign = Tools.getSign(map, KV.SECRET_KEY);
			map.put("sign", sign);
			String xml = XmlDom.mapToXml(map);
			String respon =  HttpClient4.doPost(KV.UNIFIED_ORDER_URL, xml);
			logger.info("统一下单返回 ：" + respon);
			return XmlDom.xmlToMap(respon);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
