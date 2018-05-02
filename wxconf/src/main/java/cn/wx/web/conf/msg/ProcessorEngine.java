package cn.wx.web.conf.msg;

import java.util.HashMap;
import java.util.Map;
import cn.wx.web.inter.msg.Processor;

/**
 * 处理器引擎
 * @author Administrator
 *
 */
public class ProcessorEngine {

	
	private static Map<String, Processor> map = new HashMap<>();
	
	
	public static void signProcessor(String msgtype ,Processor processor){
		if(processor != null){
			map.put(msgtype,processor);
		}
	}
	
	/**
	 * 根据类型找到对应消息处理器
	 * @param msgtype
	 * @param postData
	 * @return
	 * @throws Exception
	 */
	public static String launch(String msgtype ,String postData) throws Exception{
		Processor p = map.get(msgtype);
		if(p != null){
			return p.process(postData);
		}else{
			throw new Exception("not find msgtype:" + msgtype);
		}
	}
	
}
