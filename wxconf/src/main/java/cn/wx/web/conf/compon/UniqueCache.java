package cn.wx.web.conf.compon;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * 防止重复操作类
 * @author Administrator
 */
public class UniqueCache {

	private static Logger logger = Logger.getLogger(UniqueCache.class);

	private static Long timeout = 1000 * 3000 * 2L;

	private static Map<String, Entry> map = new ConcurrentHashMap<>();

	/**
	* 验证是否已经缓存过 此值
	 * @param clazz 某个类作为唯一器的标识
	 * @param key 要验证唯一器中的是否缓存了此值 
	 * @param timeout 唯一器中值的超时时间
	 * @return
	 */
	public static Boolean VerifyUnique(Class<?> clazz, String key, Long timeout) {
		String cn = clazz.getName();
		logger.info("classname:" + cn);
		if (!map.containsKey(cn)) {
			addEntry(cn,timeout);
		}
		return map.get(cn).VerifyUnique(key);
	}
	
	/**
	 * 添加某类的唯一器
	 * @param cn 唯一器的标志 类似id
	 * @return
	 */
	private static synchronized Entry addEntry(String cn, Long timeout) {
		if (map.containsKey(cn))
			return null;
		return map.put(cn, new Entry(timeout));
	}
	
	/**
	 * 验证是否已经缓存过 此值
	 * @param clazz
	 * @param key
	 * @return
	 */
	public static Boolean VerifyUnique(Class<?> clazz, String key) {
		return VerifyUnique(clazz, key, timeout);
	}

	/**
	 * 内部便利类 
	 * @author Administrator
	 */
	private static class Entry {

		private Map<String, Long> keymap = new ConcurrentHashMap<>();

		private Long timeout;

		/**
		 * 毫秒
		 * @param timeout
		 */
		public Entry(Long timeout) {
			this.timeout = timeout;
		}

		public synchronized Boolean VerifyUnique(String key) {
			Long currt = new Date().getTime();
			if (keymap.containsKey(key) && (currt - keymap.get(key)) < this.timeout) {
				return false;
			}
			keymap.put(key, new Date().getTime());
			return true;
		}
	}
}
