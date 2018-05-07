package cn.wx.web.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.sun.istack.internal.logging.Logger;

import sun.misc.BASE64Encoder;

public class Tools {
	
	private static Logger log = Logger.getLogger(Tools.class);
	
	/**
	 * 流转字符串
	 * @param in
	 * @return
	 * @throws Exception
	 */
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
	
	
	/**
	 * 普通md5使用
	 * @param text
	 * @return
	 */
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
	
	 /**
     * 生成 MD5 微信支付专用
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String WX_MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据 微信支付专用
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String WX_HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
	
	
	/**
	 * 随机指定长度的字符串
	 * @param length
	 * @return
	 */
	public static String randomStr(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	
	  /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。 微信支付签名
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String getSign(Map<String, String> map, String secretKey) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	Set<String> keySet = map.keySet();
    	List<String> list = new ArrayList<>(keySet);
    	Collections.sort(list);
    	for(String key : list){
    		String val = map.get(key);
    		if (val.trim().length() > 0){
    			sb.append(key).append("=").append(val).append("&");
    		}
    	}
        sb.append("key=").append(secretKey);
        System.out.println(sb);
        if(map.get("sign_type").equalsIgnoreCase("HMAC-SHA256")){
        	return Tools.WX_HMACSHA256(sb.toString(), secretKey);
        }else{
        	return Tools.WX_MD5(sb.toString());
        }
    }
	
}
