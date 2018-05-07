package cn.wx.web.conf.servlet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.SHA1;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import cn.wx.web.conf.msg.ProcessorEngine;
import cn.wx.web.util.KV;
import cn.wx.web.util.Tools;

/**
 * 微信配置类 事件处理类
 * 
 * @author clc
 * @date 2018/04/24
 */

@WebServlet(urlPatterns = { "/pay" })
public class PayWxServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * 收到请求
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			//检测是不是验证配置的
			String echostr = req.getParameter("echostr");
			if(echostr != null && !echostr.equals("")){
				Boolean bool = validateSHA(req);
				if(bool){
					out(resp, req.getParameter("echostr"));
				}else{
					out(resp, "false");
				}
			}else{
				process(req, resp);
			}
		} catch (Exception e) {
			out(resp, "false");
			e.printStackTrace();
		}
	}

	/**
	 * 验签名
	 * @param req
	 * @return
	 */
	private Boolean validateSHA(HttpServletRequest req) {
		try {
			String sign = SHA1.getSHA1(KV.TOKEN, getPar(req, "timestamp"), getPar(req, "nonce"));
			String wxsign = getPar(req, "signature");
			return sign.equals(wxsign);
		} catch (AesException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解密postData
	 * @param req
	 * @param postData
	 * @return
	 * @throws Exception
	 */
	private String decryptPostData(HttpServletRequest req, String postData) throws Exception {
		WXBizMsgCrypt pc = new WXBizMsgCrypt(KV.TOKEN, KV.ENCODING_AES_KEY, KV.APP_ID);
		String msg_signature = getPar(req, "msg_signature");
		String timeStamp = getPar(req, "timestamp");
		String nonce = getPar(req, "nonce");
		postData = pc.decryptMsg(msg_signature, timeStamp, nonce, postData);
		return postData;
	}
	
	
	/**
	 * 处理事件
	 * @param req
	 * @param resp
	 */
	private void process(HttpServletRequest req, HttpServletResponse resp) {
		try {
			ServletInputStream in = req.getInputStream();
			String postData = Tools.stream2String(in);
			if (KV.IS_ENCRYPT.equals("1")) {
				postData = decryptPostData(req,postData);
			}
			//分辨消息类型 
			Pattern pattern= Pattern.compile("<MsgType>.*?CDATA\\[(.*?)\\].*?</MsgType>");
			Matcher matcher = pattern.matcher(postData);
			if(matcher.find()){
				String msgtype = matcher.group(1);
				String retData = ProcessorEngine.launch(msgtype, postData);
				resp.getWriter().println(retData);
				resp.getWriter().close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
