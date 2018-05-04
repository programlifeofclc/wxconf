package cn.wx.web.util;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

public class KV{
	
	private static ResourceBundle rb = ResourceBundle.getBundle("config");
	
	static{
		init();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
					init();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static String IS_ENCRYPT;

	public static String TOKEN;

	public static String ENCODING_AES_KEY;

	public static String APP_ID;

	public static String APP_SECRET;

	public static String CHARSET;
	
	public static String ACCESS_TOKEN_URL;
	
	public static String MENU_CREATE_URL;

	public static String PAGE_CODE_URL;
	
	public static String PAGE_REDIRECT_URI;
	
	public static String PAGE_TOKEN_URL;
	
	public static String PAGE_REF_TOKEN_URL;
	
	public static String PAGE_USER_INFO_URL;
	
	public static String JS_TICKET_TOKEN_RRL;
	
	private static String key(String key) {
		key = rb.getString(key);
		if (key == null || key.trim().equals("")) {
			return "";
		}
		return key;
	}

	private static void init(){
		Field[] fields = KV.class.getFields();
		for(Field f : fields){
			try {
				Boolean b = f.isAccessible();
				f.setAccessible(true);
				f.set(null, KV.key(f.getName()));
				f.setAccessible(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
