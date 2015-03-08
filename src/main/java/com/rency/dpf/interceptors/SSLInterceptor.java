package com.rency.dpf.interceptors;

import java.lang.reflect.Method;
import java.net.URI;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.rency.utils.annotation.Secured;
import org.rency.utils.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @desc SSL安全拦截器
 * @author T-rency
 * @date 2014年11月14日 下午2:11:25
 */
public class SSLInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3648693607695135505L;

	private static final Logger logger = LoggerFactory.getLogger(SSLInterceptor.class);

	private String httpsPort;
	private String httpPort;
	private boolean useAnnotations = true;
	private static final int HTTP_PORT = 80;//默认值
	private static final int HTTPS_PORT = 443;//默认值
	private static final String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";
	private static final String SCHEME_HTTP = "http";
	private static final String SCHEME_HTTPS = "https";

	@Override
	public String intercept(ActionInvocation invocate) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.addCookie(new Cookie("JSESSIONID",request.getSession().getId()));
		String scheme = request.getScheme().toLowerCase();
		String requireMethod = request.getMethod().toUpperCase();
		Object action = invocate.getAction();
		Method method = Utils.getActionMethod(action.getClass(), invocate.getProxy().getMethod());
		if((!isUseAnnotations())||(action.getClass().isAnnotationPresent(Secured.class))||(method.isAnnotationPresent(Secured.class))){
			if(((HTTP_GET.equals(requireMethod)) || (HTTP_POST.equals(requireMethod)))&& (SCHEME_HTTP.equals(scheme))) {
				int httpsPort = getHttpsPort() == null ? HTTPS_PORT : Integer.parseInt(getHttpsPort());
				URI uri = new URI(SCHEME_HTTPS, null, request.getServerName(),httpsPort, response.encodeRedirectURL(request.getRequestURI()),RequestUtil.buildQueryString(request), null);
				logger.info("Going to SSL mode, redirecting to "+ uri.toString());
				response.sendRedirect(uri.toString());
				return null;
			}
		}else if(((HTTP_GET.equals(requireMethod))||(HTTP_POST.equals(requireMethod)))&&(SCHEME_HTTPS.equals(scheme))){
			int httpPort = getHttpPort() == null ? HTTP_PORT : Integer.parseInt(getHttpPort());
			URI uri = new URI(SCHEME_HTTP, null, request.getServerName(), httpPort,response.encodeRedirectURL(request.getRequestURI()),RequestUtil.buildQueryString(request), null);
			logger.info("Going to non-SSL mode, redirecting to "+ uri.toString());
			response.sendRedirect(uri.toString());
			return null;
		}
		return invocate.invoke();
	}

	public String getHttpsPort() {
		return this.httpsPort;
	}

	@Inject(value = "struts2.sslplugin.httpsPort", required = false)
	public void setHttpsPort(String httpsPort) {
		this.httpsPort = httpsPort;
	}

	public String getHttpPort() {
		return this.httpPort;
	}

	@Inject(value = "struts2.sslplugin.httpPort", required = false)
	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}

	public boolean isUseAnnotations() {
		return this.useAnnotations;
	}

	public void setUseAnnotations(boolean useAnnotations) {
		this.useAnnotations = useAnnotations;
	}

	@Inject(value = "struts2.sslplugin.annotations", required = false)
	public void setAnnotations(String annotations) {
		if (annotations == null) {
			annotations = "true";
		}
		this.useAnnotations = new Boolean(annotations).booleanValue();
	}
}