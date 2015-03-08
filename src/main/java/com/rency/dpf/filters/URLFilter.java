package com.rency.dpf.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rency.utils.common.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc URL过滤器
 * @author T-rency
 * @date 2014年6月12日 下午2:15:07
 */
public class URLFilter implements Filter{
	
	private static final Logger logger = LoggerFactory.getLogger(URLFilter.class);
	private static final String ERROR_URL = "/index!error.do";
	
	public void destroy() {
		logger.info("destory unlogin url filters");
	}

	public void doFilter(ServletRequest sRequest, ServletResponse sResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) sRequest;
		HttpServletResponse response = (HttpServletResponse) sResponse;
		String url = request.getRequestURI();
		url = url.substring(url.indexOf("/")+1,url.length());
		url = url.substring(url.indexOf("/")+1, url.length());
		if(url.lastIndexOf(".jsp") != -1){
			logger.info("********* resources filter forward["+url+"] to ["+ERROR_URL+"]");
			request.getSession().setAttribute(ResultType.RESPONSE_MESSAGE_KEY,ResultType.PAGE_NOT_FOUND+"["+url+"]");
			request.getRequestDispatcher(ERROR_URL).forward(request, response);
		}else{
			chain.doFilter(sRequest, sResponse);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		logger.info("init jsp suffix url filters");
	}

}
