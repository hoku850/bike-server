package org.ccframe.commons.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.helper.SysInitBeanHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.UserSearchService;


/**
 * 根据ajax请求还是非ajax请求。
 * 如果是ajax请求，则登陆后跳转到之前的页面
 * 如果是非ajax请求，则登陆后继续执行之前的请求
 * @author Jim
 */
public class MemberLoginFilter extends AbstractExcludeUrlFilter {

private static String backendLoginUri;
    
    private Boolean autoLogin;
    
	@Override
    public void realFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;   // NOSONAR
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        
        User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
        System.out.println("user: " + user);
        if(user == null) { 
        		String phoneNumber = httpRequest.getParameter("phoneName");
        		String IMEI = httpRequest.getParameter("IMEI");
        		
        		if(phoneNumber==null || phoneNumber.equals("") || IMEI==null || IMEI.equals("")) {
        			httpResponse.getWriter().print("checkPhone");
        			httpResponse.flushBuffer();
    	            return;
        		} else {
        			 List<User> users = SpringContextHelper.getBean(UserSearchService.class).findByLoginIdAndUserPsw(phoneNumber, IMEI);
        			 if(users!=null && users.size()>0) {
        				 //自动登录
        				 WebContextHolder.getSessionContextStore().setServerValue(Global.SESSION_LOGIN_MEMBER_USER, users.get(0));
 
        			 } else {
        				 httpResponse.getWriter().print("checkPhone");
             			 httpResponse.flushBuffer();
         	             return;
        			 }
        		}
        		
	            /*httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
	            //httpResponse.getWriter().print(backendLoginUri);
	            httpResponse.flushBuffer();
	            return;*/
        }

        filterChain.doFilter(request, response);
    }

    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		synchronized(MemberLoginFilter.class){
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