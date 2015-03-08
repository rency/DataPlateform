package com.rency.dpf.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.rency.utils.common.CONST;
import org.rency.utils.common.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rency.dpf.beans.Authority;
import com.rency.dpf.beans.User;
import com.rency.dpf.service.AuthorityService;
import com.rency.dpf.utils.Tools;

/**
 * @desc JSON拦截器
 * @author T-rency
 * @date 2014年9月24日 下午4:36:53
 */
public class JSONInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6899621435544862331L;
	private static final Logger logger = LoggerFactory.getLogger(JSONInterceptor.class);
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
		String requestURL = request.getRequestURI();
		String rootPath = request.getContextPath()+"/";
		requestURL = requestURL.substring(requestURL.indexOf(rootPath)+rootPath.length(), requestURL.length());
		logger.info("********* JSON Interceptor url:"+requestURL+"  ********");
		if(request.getSession().getAttribute(CONST.SESSION_USER_KEY) != null){
			//是否启用权限验证
			if(!this.isAuthFlag()){
				return invocation.invoke();
			}
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
				Tools.outputData(response,Tools.formatJSON(CONST.DENIED_ACCESS_KEY));
				return ResultType.USER_LOGIN_INPUT;
			}
		}else{
			//未登录
			logger.debug("********* session is empty");
			Tools.outputData(response,Tools.formatJSON(CONST.SESSION_TIMEOUT_KEY));
			return ResultType.USER_LOGIN_INPUT;
		}
	}

	public boolean isAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(boolean authFlag) {
		this.authFlag = authFlag;
	}
}
