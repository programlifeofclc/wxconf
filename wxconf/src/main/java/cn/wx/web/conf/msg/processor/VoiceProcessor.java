package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;

import cn.wx.web.conf.msg.bean.from.VoiceFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class VoiceProcessor extends BaseProcessor{
	
	private static final  String TYPE = "voice";
	
	public String process(String postData) {
		VoiceFromMsg tfm = DomBean.xml2Bean(postData, VoiceFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
