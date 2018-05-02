package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;

import cn.wx.web.conf.msg.bean.from.TextFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class TextProcessor extends BaseProcessor{
	
	private static final  String TYPE = "text";
	
	public String process(String postData) {
		TextFromMsg tfm = DomBean.xml2Bean(postData, TextFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
