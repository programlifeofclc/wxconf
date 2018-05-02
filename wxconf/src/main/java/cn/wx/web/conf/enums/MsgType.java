package cn.wx.web.conf.enums;

import cn.wx.web.conf.msg.bean.BaseMsg;
import cn.wx.web.conf.msg.bean.FromMsg;

public enum MsgType {

	TEXT("text",FromMsg.class);//, IMAGE(), VOICE(), VIDEO(), SHORTVIDEO(), LOCATION(), LINK();

	public String value;
	public Class<?> msg;

	private MsgType(String value, Class<? extends BaseMsg> clazz) {
		this.value = value;
		this.msg = clazz;
	}
	
	public static Class<?> getMsg(String key) {
		for (MsgType m : MsgType.values()) {
			if (m.value.equalsIgnoreCase(key)) {
				return m.msg;
			}
		}
		return null;
	}
	
	
}
