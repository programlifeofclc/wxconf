package cn.wx.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CookieUtils {
	public static Logger log = Logger.getLogger(CookieUtils.class);

	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	/**
	 * 获取cookie值
	 * @param request
	 * @param cookName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String cookName) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie ck : cookies) {
				if (ck.getName().equalsIgnoreCase(cookName)) {
					String value = ck.getValue();
					if (value == null)
						return "";
					return value;
				}
			}
		}
		return "";
	}

	/**
	 * 删除cookies
	 * @param request
	 * @param response
	 */
	public static void delCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookies[];
		cookies = request.getCookies();
		if (cookies != null)
			for (Cookie ck : cookies) {
				Cookie cookie = new Cookie(ck.getName(), null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
	}

}
