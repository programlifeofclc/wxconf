package cn.wx.web.util;

import com.alibaba.fastjson.JSON;

public class ReturnBean{

	private String msgcode;
	
	private String msg;
	
	private Object data;

	public String toJson(){
		return JSON.toJSONString(this);  
	}

	public String getMsgcode() {
		return msgcode;
	}

	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	}
