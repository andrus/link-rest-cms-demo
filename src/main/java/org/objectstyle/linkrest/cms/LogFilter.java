package org.objectstyle.linkrest.cms;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used for debugging of requests.
 */
@WebFilter(filterName = "log", value = "/*")
public class LogFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		try {
			chain.doFilter(request, response);
		} finally {

			HttpServletResponse httpResponse = (HttpServletResponse) response;
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			StringBuilder log = new StringBuilder();
			log.append(httpRequest.getMethod()).append(' ');

			String host = httpRequest.getHeader("Host");
			if (host != null) {
				log.append(host);
			}

			String uri = httpRequest.getRequestURI();
			if (uri != null) {
				log.append(uri);
			}

			String queryString = httpRequest.getQueryString();
			if (queryString != null) {
				log.append('?').append(queryString);
			}

			log.append(' ').append(httpResponse.getStatus());

			LOGGER.info(log.toString());
		}
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
