package demo;

import cn.wx.web.conf.msg.bean.from.TextFromMsg;
import cn.wx.web.util.DomBean;

public class DomBeanTest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		String data = "<xml><cc><list><aa>1111</aa><bb>4455</bb></list><list><aa>2222</aa><bb>2233</bb></list></cc><cc><aa>123</aa><bb>456</bb></cc><ToUserName><![CDATA[oia2Tj我是中文jewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
		TextFromMsg f = DomBean.xml2Bean(data, TextFromMsg.class);
		System.out.println(f.ToUserName);
	}
	
}
