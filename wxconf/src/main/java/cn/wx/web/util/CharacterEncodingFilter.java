package cn.wx.web.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

@WebFilter(urlPatterns="/*",initParams={@WebInitParam(name="encoding",value="UTF-8"),@WebInitParam(name="forceEncoding",value="true")})
public class CharacterEncodingFilter implements Filter {

	/**
	 * The default character encoding to set for requests that pass through this
	 * filter.
	 */
	protected String encoding = null;

	/**
	 * The filter configuration object we are associated with. If this value is
	 * null, this filter instance is not currently configured.
	 */
	protected FilterConfig filterConfig = null;

	// --------------------------------------------------------- Public Methods

	/**
	 * Take this filter out of service.
	 */
	public void destroy() {

		this.encoding = null;
		this.filterConfig = null;

	}

	/**
	 * Select and set (if specified) the character encoding to be used to
	 * interpret request parameters for this request.
	 * 
	 * @param request
	 *            The servlet request we are processing
	 * @param result
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 * 
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (encoding != null) {
			Map<?, ?> map = request.getParameterMap();
			Collection<?> values = map.values();
			for (Iterator<?> iterator = values.iterator(); iterator.hasNext();) {
				Object[] obj = (Object[]) iterator.next();
				String value = obj[0].toString();
				obj[0] = URLDecoder.decode(new String(value.getBytes("iso-8859-1"), encoding), encoding);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * Place this filter into service.
	 * 
	 * @param filterConfig
	 *            The filter configuration object
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
	}

}
