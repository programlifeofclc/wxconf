package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;

import cn.wx.web.conf.msg.bean.from.EventFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class EventProcessor extends BaseProcessor{
	
	private static final  String TYPE = "event";
	
	public String process(String postData) {
		EventFromMsg tfm = DomBean.xml2Bean(postData, EventFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
