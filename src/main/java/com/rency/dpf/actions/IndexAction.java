package com.rency.dpf.actions;

import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("indexAction")
@Scope("prototype")
@Log(logName="主页",logType=LogType.UNDEFINED)
public class IndexAction extends BasicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	private static final Logger logger = LoggerFactory.getLogger(IndexAction.class);

	@Log(logName="主页面")
	public String index() throws CoreException{
		return ResultType.SUCCESS;
	}
	
	@Log(logName="错误页面")
	public String error() throws CoreException{
		String errorMessage = String.valueOf(session.get(ResultType.RESPONSE_MESSAGE_KEY));
		logger.debug("xxxxxx  error message:"+errorMessage);
		if(errorMessage.startsWith(ResultType.PAGE_NOT_FOUND)){
			return ResultType.PAGE_NOT_FOUND;
		}
		return ResultType.ERROR;
	}
	
}