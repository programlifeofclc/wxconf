package cn.wx.web.inter.msg;

import cn.wx.web.conf.msg.bean.BaseMsg;

public interface Processor<T extends BaseMsg>{

	T String2Bean(String data);
	
}
