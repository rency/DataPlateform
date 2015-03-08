package com.rency.dpf.interceptors;

import java.lang.reflect.Method;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.rency.utils.annotation.Log;
import org.rency.utils.common.CONST;
import org.rency.utils.tools.AnnotationUtil;
import org.rency.utils.tools.BrowserUtils;
import org.rency.utils.tools.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.rency.dpf.beans.Loggers;
import com.rency.dpf.beans.User;
import com.rency.dpf.service.LoggerService;

/**
 * @desc 日志拦截器
 * @author T-rency
 * @date 2014年9月24日 下午4:36:53
 */
public class LogInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6899621435544862331L;
	
	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
	
	@Autowired
	@Qualifier("loggerService")
	private LoggerService loggerService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Object action = invocation.getAction();
		//获取请求方法
		Method method = Utils.getActionMethod(action.getClass(), invocation.getProxy().getMethod());
		try{
			Loggers log = new Loggers();
			if( session != null && session.getAttribute(CONST.SESSION_USER_KEY) != null ){
				User user = (User) session.getAttribute(CONST.SESSION_USER_KEY);
				log.setUser(user);
			}
			if((action.getClass().isAnnotationPresent(Log.class))||(method.isAnnotationPresent(Log.class))){
				//根据日志Annotation(Log)获取日志描述、日志类别
				Log classLog = AnnotationUtil.getAnnotationValue(action.getClass(),null,Log.class);
				Log methodLog = AnnotationUtil.getAnnotationValue(null, method, Log.class);
				log.setAction(methodLog.logName()+CONST.SPLIT_KEY+classLog.logName());
				log.setLogType(classLog.logType() == null ? methodLog.logType().getId() : classLog.logType().getId());
				log.setLogTypeDesc(classLog.logType() == null ? methodLog.logType().getDescription() : classLog.logType().getDescription());
			}
			log.setClientIP(request.getRemoteAddr());
			log.setBrowser(BrowserUtils.checkBrowse(request));
			Map<String, Object> params = invocation.getInvocationContext().getParameters();
			StringBuilder param = new StringBuilder();
			for(String key : params.keySet()){
				param.append("[");
				param.append(key);
				param.append(":");
				param.append(params.get(key));
				param.append("]");
			}
			log.setParam(param.toString());
			long start = System.currentTimeMillis();
			result = invocation.invoke();
			long end = System.currentTimeMillis();
			String useTime = String.valueOf((end - start));
			log.setUseTime(useTime);
			//记录日志
			loggerService.add(log);
		}catch(Exception e){
			logger.error("log interceptor save loggers exception.",e);
			e.printStackTrace();
		}
		return result;
	}
}
