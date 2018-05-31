package cn.wx.web.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFilter {

	Logger logger = LoggerFactory.getLogger(RequestFilter.class);
	
	private static Map<String,Long> map = new ConcurrentHashMap<>();
	
	private Long interval;
	
	public static final Long SECOND_1 = 1L;
	public static final Long SECOND_3 = 3L;
	public static final Long SECOND_5 = 5L;
	public static final Long SECOND_10 = 10L;
	public static final Long SECOND_30 = 30L;
	public static final Long SECOND_50 = 50L;
	public static final Long MINUTE_1 = 60L;
	public static final Long MINUTE_5 = 60L * 5;
	public static final Long HOUR_1 = 60L * 60;
	public static final Long HOUR_2 = 60L * 60 * 2;
	public static final Long HOUR_12 = 60L * 60 * 12;
	public static final Long DAY_1 = 60L * 60 * 24;
	
	/**
	 * 私有构造
	 */
	private RequestFilter() { }

	/**
	 * 私有构造 
	 * @param times 单位秒
	 */
	private RequestFilter(Long times) {
		this.interval = times;

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(interval * 1000 / 2);
						cleanKey();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * 创建过滤器
	 * @param times 秒
	 * @return
	 */
	public static RequestFilter createFilter(Long times) {
		return new RequestFilter(times);
	}
	
	/**
	 * 过滤执行
	 * @param request
	 * @return
	 */
	public Boolean filter(String request) {
		if (request == null || request.equals("")){
			logger.info("过滤器传递参数为空");
			return true;
		}
		String key = getMD5(request);
		if (map.containsKey(key)) {
			logger.info("key值" + key + "在有效时间内存在 不能再次访问");
			return false;
		} else {
			logger.info("key值" + key + "在有效时间内不存在 可以访问");
			map.put(key, System.currentTimeMillis());
			return true;
		}
	}
	
	/**
	 * 清理过期key
	 */
	private void cleanKey(){
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Long value = map.get(key);
			if( System.currentTimeMillis() - value > interval * 1000){
				logger.info("key值" + key + "过期 被清理");
				map.remove(key);
			}
		}
	}
	
	/**
	 * 对字符串md5加密(小写+字母)
	 * @param str 传入要加密的字符串
	 * @return MD5加密后的字符串
	 */
	private String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes("utf-8"));
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static void main(String[] args) {
		RequestFilter rf = RequestFilter.createFilter(MINUTE_5);
		System.out.println(rf.filter("123"));
		try {
			Thread.sleep(60 * 1000 * 6);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(rf.filter("123"));
	}
	
	
}
