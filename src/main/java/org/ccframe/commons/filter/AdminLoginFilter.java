package org.ccframe.commons.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.Global;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.helper.SysInitBeanHelper;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.UserService;

/**
 * @author Jim
 */
public class AdminLoginFilter extends AbstractExcludeUrlFilter {

//    private static Logger logger = LoggerFactory.getLogger(AdminLoginFilter.class);

    private static String backendLoginUri;
    
    private Boolean autoLogin;
    
	@Override
    public void realFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;   // NOSONAR
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        AdminUser adminUser = (AdminUser)httpRequest.getSession().getAttribute(Global.SESSION_LOGIN_ADMIN);
        if(adminUser == null) {
        	if(isAdminAutoLogin()){ //自动登录
        		httpRequest.getSession().setAttribute(Global.SESSION_LOGIN_ADMIN, AdminUser.create(SpringContextHelper.getBean(UserService.class).getById(Global.ADMIN_USER_ID), Global.PLATFORM_ORG_ID));
        	}else{
	            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            httpResponse.getWriter().print(backendLoginUri);
	            httpResponse.flushBuffer();
	            return;
        	}
        }
        filterChain.doFilter(request, response);
    }

    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		synchronized(AdminLoginFilter.class){
			backendLoginUri = filterConfig.getInitParameter("backendLoginUri");
		}
	}

    public static String getBackendLoginUri() {
		return backendLoginUri;
	}
    
    public boolean isAdminAutoLogin() {
    	if(autoLogin == null){
    		autoLogin = SpringContextHelper.getBean(SysInitBeanHelper.class).isAdminAutoLogin();
    	}
    	return autoLogin.booleanValue();
    }
}
