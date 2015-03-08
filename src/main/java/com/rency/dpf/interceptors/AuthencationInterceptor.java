package com.rency.dpf.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.rency.utils.common.CONST;
import org.rency.utils.common.ResultType;
import org.rency.utils.common.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rency.dpf.beans.Authority;
import com.rency.dpf.beans.URLFilter;
import com.rency.dpf.beans.User;
import com.rency.dpf.service.AuthorityService;
import com.rency.dpf.utils.Tools;

/**
 * @desc 权限拦截器
 * @author T-rency
 * @date 2014年9月24日 下午4:36:53
 */
public class AuthencationInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6899621435544862331L;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthencationInterceptor.class);
	private boolean authFlag = false;
	@Autowired
	@Qualifier("authorityService")
	private AuthorityService authorityService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String result =  authentication(invocation,request,response);
		return result;
	}
	
	/**
	 * description 权限认证
	 * date 2014年3月22日 下午10:42:20
	 * @param invocation
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String authentication(ActionInvocation invocation,HttpServletRequest request,HttpServletResponse response) throws Exception{
		URLFilter urlFilter = (URLFilter) SpringContextHolder.getBean(URLFilter.class);
		String requestURL = request.getRequestURI();
		logger.info("********* Auth Interceptor url:"+requestURL);
		if(requestURL.indexOf("?") != -1){
			requestURL = requestURL.substring(0,requestURL.indexOf("?"));
		}
		String rootPath = request.getContextPath()+"/";
		requestURL = requestURL.substring(requestURL.indexOf(rootPath)+rootPath.length(), requestURL.length());

		//免登陆过滤
		@SuppressWarnings("unchecked")
		List<String> filters = urlFilter.getFilter();
		if(filters.contains(requestURL)){
			return invocation.invoke();
		}
		
		//验证session是否过期
		if(!request.isRequestedSessionIdValid()){
			logger.debug("********* session timeout.");
			String javascript = Tools.alert(request, CONST.SESSION_TIMEOUT,"user!loginInput.do");
			Tools.outputData(response,javascript);
			return ResultType.USER_LOGIN_INPUT;
		}else{
			if(request.getSession().getAttribute(CONST.SESSION_USER_KEY) != null){
				//JSP页面，只验证是否登录用户
				if(requestURL.startsWith("jsp_")){
					logger.debug("********* Auth Interceptor exclude jsp url["+requestURL+"] invoke.");
					return invocation.invoke();
				}
				//是否启用权限验证
				if(!this.isAuthFlag()){
					return invocation.invoke();
				}
				//权限验证
				User user = (User) request.getSession().getAttribute(CONST.SESSION_USER_KEY);
				Authority authority = new Authority();
				authority.setUserType(user.getUserType());
				authority.setResources(requestURL);
				boolean hasAuth = authorityService.findAuth(authority);
				if(hasAuth){
					return invocation.invoke();
				}else{
					//无访问权限
					logger.info("********* denied access resources["+requestURL+"] with["+user.getUsername()+" | "+user.getUserType().getTypeName()+"]");
					request.getSession().setAttribute(ResultType.RESPONSE_MESSAGE_KEY, CONST.DENIED_ACCESS_RESOURCES);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//401
					return ResultType.ERROR;
				}
			}else{
				//未登录
				logger.debug("********* session is empty");
				String javascript = Tools.alert(request, CONST.SESSION_TIMEOUT,"user!loginInput.do");
				Tools.outputData(response,javascript);
				return ResultType.USER_LOGIN_INPUT;
			}
		}
	}

	public boolean isAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(boolean authFlag) {
		this.authFlag = authFlag;
	}
}
