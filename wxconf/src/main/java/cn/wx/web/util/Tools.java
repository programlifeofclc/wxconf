package cn.wx.web.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletInputStream;

import com.sun.istack.internal.logging.Logger;

import sun.misc.BASE64Encoder;

public class Tools {
	
	private static Logger log = Logger.getLogger(Tools.class);
	
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
	
	
	public static String getMd5(String text){
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(text.getBytes(KV.CHARSET));
			byte[] b = md5.digest();
			BASE64Encoder base64en = new BASE64Encoder();
			String st5 = base64en.encode(b);
			log.info("md5:" + text + "=" + st5);
			return st5;
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
