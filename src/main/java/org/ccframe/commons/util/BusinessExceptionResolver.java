package org.ccframe.commons.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ccframe.client.ResGlobal;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.dto.ErrorObjectResp;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class BusinessExceptionResolver implements HandlerExceptionResolver  {

	public static final String ERRORPAGE = "errorPage";

	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ErrorObjectResp error = null;
		if (ex instanceof BusinessException) {
			BusinessException busException = (BusinessException) ex;
			String errorText = SpringContextHelper.getBean(
					AbstractMessageSource.class).getMessage(
					busException.getCode(), busException.getArgs(),
					request.getLocale());
			error = new ErrorObjectResp(busException.getCode(), errorText,
					busException.getViewData());
			if(busException.isUseSimpleLog()){
				log.error(errorText);
			}else{
				log.error(errorText, ex);
			}
		} else {
			error = new ErrorObjectResp(ResGlobal.ERRORS_EXCEPTION, ex.getMessage() == null ? ex.toString() : ex.getMessage(),
					null);
			log.error(ex.getMessage(), ex);
		}
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return new ModelAndView().addObject(error);
	}

}
