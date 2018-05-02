package cn.wx.web.util;

import java.io.ByteArrayOutputStream;
import javax.servlet.ServletInputStream;

import cn.wx.web.conf.enums.MsgType;

public class Tools {

	public static String stream2String(ServletInputStream in ) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		byte[] b = new byte[1024];
		int readlen = 0;
		while((readlen = in.read(b)) != -1 ){
			out.write(b, 0, readlen);
		}
		in.close();
		out.close();
		return new String(out.toByteArray(),"utf-8");
	}
	
	
	
	
	
	public static Object string2Bean(String postData) throws Exception {
		//if(type == "text")
		DomBean.xml2Bean(postData, MsgType.getMsg("text"));
		
		return null;
	}
	
	
}
