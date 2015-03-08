package com.rency.dpf.actions;

import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("jspAction")
@Scope("prototype")
public class JSPAction extends BasicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	public String maintain_users() throws CoreException{
		return ResultType.MAINTAIN_USERS;
	}
	
	public String maintain_usert() throws CoreException{
		return ResultType.MAINTAIN_USERT;
	}
	
	public String maintain_authencation() throws CoreException{
		return ResultType.MAINTAIN_AUTHENCATION;
	}
	
	public String maintain_menus() throws CoreException{
		return ResultType.MAINTAIN_MENUS;
	}
	
	public String maintain_syslog() throws CoreException{
		return ResultType.MAINTAIN_SYSLOG;
	}
	
	public String crawler_monitor() throws CoreException{
		return ResultType.CRAWLER_MONITOR;
	}
	
	public String hadoop_monitor() throws CoreException{
		return ResultType.HADOOP_MONITOR;
	}
	
	public String lucene_monitor() throws CoreException{
		return ResultType.LUCENE_MONITOR;
	}
	
	public String lucene_search() throws CoreException{
		return ResultType.LUCENE_SEARCH;
	}
	
}