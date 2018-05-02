package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;
import cn.wx.web.conf.msg.bean.from.ShortVideoFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class ShortVideoProcessor extends BaseProcessor{
	
	private static final  String TYPE = "shortvideo";
	
	public String process(String postData) {
		ShortVideoFromMsg tfm = DomBean.xml2Bean(postData, ShortVideoFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
