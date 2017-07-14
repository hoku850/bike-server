package org.ccframe.commons.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.commons.web.WebSessionContext;

public class WebContextFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		WebContextHolder.setContextPath(filterConfig.getServletContext().getContextPath());
		WebContextHolder.setWarPath(filterConfig.getServletContext().getRealPath("/"));
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        WebContextHolder.setSessionContextStore(new WebSessionContext((HttpServletRequest)request, (HttpServletResponse)response));
        chain.doFilter(request, response);
    }
    public void destroy() {
        
    }
}
