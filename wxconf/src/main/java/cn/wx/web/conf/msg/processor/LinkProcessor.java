package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;
import cn.wx.web.conf.msg.bean.from.LinkFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class LinkProcessor extends BaseProcessor{
	
	private static final  String TYPE = "link";
	
	public String process(String postData) {
		LinkFromMsg tfm = DomBean.xml2Bean(postData, LinkFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
