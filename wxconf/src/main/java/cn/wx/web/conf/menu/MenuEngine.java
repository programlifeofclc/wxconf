package cn.wx.web.conf.menu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.wx.web.conf.menu.bean.ViewButton;
import cn.wx.web.util.HttpClient4;
import cn.wx.web.util.KV;

public class MenuEngine {

	public static String launch(String buttonJson) throws Exception{
		
		String url = KV.MENU_CREATE_URL + "?access_token=9_kw3LEJUwqREjCA2Mrbwb3Yb4Nwy16aPulEdoQ7sUASJNgg83ttjsQcNwumtWpj1IdlVmQHxzVgxuo_cic6bdxg91LCdPmhek6ltD8WsEyww6eQJLcSwr5ilL6NPkHiJk7ECJxFjx6EQTS4_gOHBhAHAATN";
		
		ViewButton cb1 = new ViewButton("测试页面1","http://sanfangzhifu.95081.cn/wxconf/index.html");
		ViewButton cb2 = new ViewButton("page2","http://sanfangzhifu.95081.cn/wxconf/pageCode");
		
		JSONArray ja = new JSONArray();
		ja.add(cb1);
		ja.add(cb2);
		JSONObject jobject = new JSONObject();
		jobject.put("button",ja);
		
		String access_json = HttpClient4.doPost(url, jobject.toJSONString());
		
		System.out.println(access_json);
		return access_json;
	}
	
}
