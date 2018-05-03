package cn.wx.web.conf.menu.bean;

public class ViewButton extends Button {

	public String type;
	
	public String name;
	
	public String url;

	public ViewButton() {
		super();
	}

	public ViewButton(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getType() {
		return "view";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
