package cn.wx.web.conf;

import java.io.IOException;

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

}
