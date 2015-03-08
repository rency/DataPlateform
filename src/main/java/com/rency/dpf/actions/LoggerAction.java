package com.rency.dpf.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ModelDriven;
import com.rency.dpf.beans.Loggers;
import com.rency.dpf.service.LoggerService;

@Component("loggerAction")
@Scope("prototype")
@Log(logName="日志",logType=LogType.MANAGEMENT)
public class LoggerAction extends BasicAction implements ModelDriven<Loggers>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4525096117628619214L;
	
	private static final Logger logger = LoggerFactory.getLogger(LoggerAction.class);
	
	@Autowired
	private LoggerService loggerService;
	
	private Loggers log = new Loggers();
	
	@JSON
	@Log(logName="加载")
	public String load() throws CoreException{
		List<Loggers> loggerList = new ArrayList<Loggers>();
		try{
			loggerList = loggerService.list();
			logger.debug("load all sys log list."+loggerList.size());
		}catch(Exception e){
			logger.error("load all sys log list error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		super.writeRows(loggerList);
		return ResultType.SUCCESS;
	}

	@Override
	public Loggers getModel() {
		return log;
	}

}
