package cn.wx.web.conf.menu.bean;

public class ClickButton extends Button {

	public String type;
	
	public String name;
	
	public String key;

	public ClickButton() {}

	public ClickButton(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public String getType() {
		return "click";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
