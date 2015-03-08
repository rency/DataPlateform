package com.rency.dpf.actions;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.rency.utils.common.PageQueryResult;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionSupport;

public class BasicAction extends ActionSupport implements RequestAware,SessionAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5550685936932139664L;
	
	private static final Logger logger = LoggerFactory.getLogger(BasicAction.class);
	
	protected HashMap<String, Object> responseMap = new HashMap<String,Object>();
	protected Map<String, Object> request = new HashMap<String, Object>();
	protected Map<String, Object> session = new HashMap<String, Object>();
	
	public BasicAction(){
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_NORMAL);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, null);
		responseMap.put(ResultType.RESPONSE_ROWS_KEY, null);
	}
	
	/**
	 * @desc action输出信息
	 * @date 2014年11月14日 上午11:01:25
	 * @param message
	 * @throws CoreException
	 */
	protected void putMessage(String message) throws CoreException{
		responseMap.put(ResultType.RESPONSE_INFO_KEY, message);
	}
	
	/**
	 * @desc 获取系统错误信息
	 * @date 2014年11月14日 上午11:01:11
	 * @return
	 * @throws CoreException
	 */
	protected String getSysError() throws CoreException{
		return "系统正在维护，请稍后再试或者联系管理员！";
	}
	
	/**
	 * @desc 输出系统错误信息
	 * @date 2014年11月12日 下午4:58:10
	 * @throws CoreException
	 */
	protected void writeSysError() throws CoreException{
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_ERROR);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, getSysError());
		logger.debug("********** write sys error info. responseMap is "+responseMap);
	}
	
	/**
	 * @desc 输出错误JSON信息
	 * @date 2014年6月16日 上午10:42:18
	 * @param message 错误消息
	 * @throws CoreException
	 */
	protected void writeError(String message) throws CoreException{
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_ERROR);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, message);
		logger.debug("********** write json error info. responseMap is "+responseMap);
	}
	
	/**
	 * @desc 输出JSON信息
	 * @date 2014年6月16日 上午10:42:18
	 * @param value 输出结果
	 * @throws CoreException
	 */
	protected void write(Object value) throws CoreException{
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_NORMAL);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, value);
		logger.debug("********** write json. responseMap is "+responseMap);
	}
	
	/**
	 * @desc 输出符合easyui的JSON信息
	 * @date 2014年6月16日 上午10:42:18
	 * @param value 输出结果
	 * @throws CoreException
	 */
	protected void writeRows(Object value) throws CoreException{
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_NORMAL);
		if(value != null){
			responseMap.put(ResultType.RESPONSE_ROWS_KEY, value);
		}
		logger.debug("********** write json rows. responseMap is "+responseMap);
	}
	
	/**
	 * @desc 输出符合easyui的JSON分页信息
	 * @date 2014年6月16日 上午10:42:18
	 * @param value 输出结果
	 * @throws CoreException
	 */
	protected  void writeRows(PageQueryResult<?> pqr) throws CoreException{
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_NORMAL);
		responseMap.put(ResultType.RESPONSE_ROWS_KEY, pqr);
		logger.debug("********** write json rows. responseMap is "+responseMap);
	}
	
	/**
	 * @desc 输出json数组
	 * @author user_rcy@163.com
	 * @date 2014年6月14日 下午12:56:36
	 * @param value
	 * @throws CoreException
	 */
	protected void writeArray(Object value) throws CoreException{
		JSONArray nodeArray = JSONArray.fromObject(value);
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_NORMAL);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, nodeArray);
		logger.debug("********** write json array. responseMap is "+responseMap);
	}
	
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}
	
	public HashMap<String, Object> getResponseMap() {
		return responseMap;
	}
	
	public void setResponseMap(HashMap<String, Object> responseMap) {
		this.responseMap = responseMap;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
}