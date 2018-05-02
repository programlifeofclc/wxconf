package cn.wx.web.conf.msg.processor;

import javax.servlet.annotation.WebListener;

@WebListener
public class TextProcessor extends BaseProcessor{
	
	private static final  String TYPE = "text";
	
	public String process(String postData) {
		
		
		return null;
	}

	
	public String getType() {
		return TYPE;
	}
	
}
