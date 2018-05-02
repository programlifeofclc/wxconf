package cn.wx.web.conf.msg;

import java.util.HashMap;
import java.util.Map;
import cn.wx.web.inter.msg.Processor;

public class ProcessorEngine {

	
	private static Map<String, Processor> map = new HashMap<>();
	
	
	public static void signProcessor(String msgtype ,Processor processor){
		if(processor != null){
			map.put(msgtype,processor);
		}
	}
	
	
	public static String launch(String msgtype ,String postData) throws Exception{
		Processor p = map.get(msgtype);
		if(p != null){
			return p.process(postData);
		}else{
			throw new Exception("not find msgtype:" + msgtype);
		}
	}
	
}
