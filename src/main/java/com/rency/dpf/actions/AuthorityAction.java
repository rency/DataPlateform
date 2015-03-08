package com.rency.dpf.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rency.dpf.beans.Authority;
import com.rency.dpf.service.AuthorityService;

@Component("authorityAction")
@Scope("prototype")
@Log(logName="权限",logType=LogType.MANAGEMENT)
public class AuthorityAction extends BasicAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4976362945097120204L;

	private static final Logger logger = LoggerFactory.getLogger(AuthorityAction.class);
	
	@Autowired
	@Qualifier("authorityService")
	private AuthorityService authorityService;
	private Authority authority;
	private String rows;
	private String page;
	
	@JSON
	@Log(logName="加载列表")
	public String load() throws CoreException{
		logger.debug("******load authority resources");
		List<Authority> authorityList = new ArrayList<Authority>();
		try{
			authorityList = authorityService.list();
			if( authorityList == null || authorityList.size() == 0){
				super.writeError("查询无结果");
				return ResultType.SUCCESS;
			}
			/*List<Authority> tempList = new ArrayList<Authority>(authorityList);
			for(Authority au : tempList){
				if(au.getUserType().getTypeId() == CONST.USER_TYPE_SUPER){
					authorityList.remove(au);
				}
			}*/
		}catch(Exception e){
			logger.error("");
			e.printStackTrace();
		}
		super.writeRows(authorityList);
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="根据地址查询")
	public String queryByAddr() throws CoreException{
		List<Authority> authorities = new ArrayList<Authority>();
		try{
			if(StringUtils.isBlank(authority.getResources())){
				logger.debug("query attribute is null.");
				super.writeError("请输入查询条件");
				return ResultType.SUCCESS;
			}
			/*List<Authority> tempList = authorityService.queryByAddr(authority.getResources());
			authorities = new ArrayList<Authority>(tempList);
			for(Authority au : tempList){
				if(au.getUserType().getTypeId() == CONST.USER_TYPE_SUPER){
					authorities.remove(au);
				}
			}*/
			logger.debug("query by addr list size is "+authorities.size());
		}catch(Exception e){
			logger.error("queryByAddr error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		super.writeRows(authorities);
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="添加")
	public String add() throws CoreException{
		if( authority == null ){
			logger.error("add authority error, and authority is null");
			super.writeError("");
			return ResultType.SUCCESS;
		}
		
		boolean isAdd = authorityService.add(authority);
		if(isAdd){
			super.write("添加成功");
		}else{
			super.writeError("添加失败");
		}
		return ResultType.SUCCESS;
		
	}

	@JSON
	@Log(logName="删除")
	public String remove() throws CoreException{
		if(authority.getId() <= 0){
			logger.error("remove authority error, and authority is "+authority);
			super.writeError("删除失败");
			return ResultType.SUCCESS;
		}
		
		boolean isDel = authorityService.deleteById(authority.getId());
		if(isDel){
			super.write("删除成功");
		}else{
			super.writeError("删除失败");
		}
		return ResultType.SUCCESS;
		
	}
	
	public Authority getAuthority() {
		return authority;
	}
	
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

}
