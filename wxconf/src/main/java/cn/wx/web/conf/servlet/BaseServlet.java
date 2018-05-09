package cn.wx.web.conf.servlet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	public String getPar(HttpServletRequest req, String param) {
		String rn = req.getParameter(param);
		if (rn == null)
			return "";
		return rn;
	}

	public void out(HttpServletResponse resp, String param) {
		try {
			resp.getWriter().println(param);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIp(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		try {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = InetAddress.getLocalHost().getHostAddress();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		if(ip.contains(",")){
			String[] ips = ip.split(",");
  			if(ips[0].length() >= ips[1].length()){
  				ip = ips[0];
  			}else{
  				ip = ips[1];
  			}
		}
		return ip;
	}

}
