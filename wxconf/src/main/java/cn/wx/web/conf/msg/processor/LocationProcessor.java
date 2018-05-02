package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;

import cn.wx.web.conf.msg.bean.from.LocationFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class LocationProcessor extends BaseProcessor{
	
	private static final  String TYPE = "location";
	
	public String process(String postData) {
		LocationFromMsg tfm = DomBean.xml2Bean(postData, LocationFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
