package cn.wx.web.conf.msg.processor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.wx.web.conf.msg.ProcessorEngine;
import cn.wx.web.inter.msg.Processor;

public abstract class BaseProcessor implements Processor ,ServletContextListener{
	
	
	public abstract String getType();
	
	public abstract String process(String postData);
	
	public void contextInitialized(ServletContextEvent event) {
		ProcessorEngine.signProcessor(getType(), this);
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		
	}
}
