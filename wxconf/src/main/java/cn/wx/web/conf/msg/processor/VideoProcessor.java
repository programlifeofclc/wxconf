package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;
import cn.wx.web.conf.msg.bean.from.VideoFromMsg;
import cn.wx.web.util.DomBean;

@WebListener
public class VideoProcessor extends BaseProcessor{
	
	private static final  String TYPE = "video";
	
	public String process(String postData) {
		VideoFromMsg tfm = DomBean.xml2Bean(postData, VideoFromMsg.class);
		System.out.println(tfm.getFromUserName());
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
