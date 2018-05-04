package cn.wx.web.conf.compon;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;

public class UserInfoCache {

	private static Logger logger = LogManager.getLogger(UserInfoCache.class);
	
	private static Map<String,JSONObject> map = new HashMap<>();
	
	
	public static JSONObject getWxUserInfo(String key){
		JSONObject json = PageAccessToken.getAccToken(key);
		logger.info("获取actoken:*" + json);
		if(json != null){
			String access_token = json.getString("access_token");
			String openid = json.getString("openid");
			//用户信息获取
			String info_url = KV.PAGE_USER_INFO_URL 
								+ "?access_token=" + access_token 
								+ "&openid=" + openid 
								+ "&lang=zh_CN";
			logger.error("获取userinf url：" + info_url);
			String userString = HttpClient4.doGet(info_url);
			JSONObject userjson = JSONObject.parseObject(userString);
			if(!userjson.containsKey("errcode")){
				UserInfoCache.addUserInfo(key, userjson);
				logger.error("获取userinf ok：" + userjson);
				return userjson;
			}
			logger.error("获取userinf error：" + userjson);
		}
		logger.error("pagetoken cache is null");
		return null;
	}
	
	
	/**
	 * 加入缓存
	 * @param openid
	 * @param cacheString
	 * @return 1成功 0 失败
	 */
	public static int addUserInfo(String key, JSONObject json) {
		if (key != null && !key.equals("") && json != null && !json.equals("")) {
			map.put(key, json);
			logger.info("存入缓存openid:" + key);
			return 1;
		}
		return 0;
	}
	
	/**
	 * 获取key 对应缓存
	 * @param key
	 * @return
	 */
	public static JSONObject getUserInfo(String key){
		if(map.containsKey(key)){
			return map.get(key);
		}
		return getWxUserInfo(key);
	}
	
	
	/**
	 * 检测是否存在 键值 缓存
	 * @param openid
	 * @return
	 */
	public static Boolean haveUserInfo(String key){
		return map.containsKey(key);
	}
	
	
	/**
	 * 返回删除的缓存
	 * @param openid
	 * @return
	 */
	public static JSONObject delUserInfo(String key){
		if(haveUserInfo(key)){
			return map.remove(key);
		}
		return null;
	}
	
}
