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
import com.rency.dpf.beans.UserType;
import com.rency.dpf.service.UserTypeService;

@Component("userTAction")
@Scope("prototype")
@Log(logName="用户类别",logType=LogType.MANAGEMENT)
public class UserTypeAction extends BasicAction implements ModelDriven<UserType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4525096117628619214L;
	
	private static final Logger logger = LoggerFactory.getLogger(UserTypeAction.class);
	
	@Autowired
	private UserTypeService userTypeService;
	
	private UserType userT = new UserType();
	
	@JSON
	@Log(logName="加载")
	public String load() throws CoreException{
		List<UserType> userList = new ArrayList<UserType>();
		try{
			userList = userTypeService.listWithoutSuper();
			logger.debug("load all user type list."+userList.size());
		}catch(Exception e){
			logger.error("load all user type list error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		super.writeRows(userList);
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="添加")
	public String save() throws CoreException{
		try{
			if(userT == null){
				super.writeError("请输入用户类型信息.");
				return ResultType.SUCCESS;
			}
			logger.debug("save userType["+userT.toString()+"].");
			if(userTypeService.add(userT)){
				super.write("添加成功");
			}else{
				super.writeError("添加失败");
			}
		}catch(Exception e){
			logger.error("save user type error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="更新")
	public String update() throws CoreException{
		try{
			if(userT == null && userT.getTypeId() > 0){
				super.writeError("请输入用户类型信息.");
				return ResultType.SUCCESS;
			}
			logger.debug("update userType["+userT.toString()+"].");
			if(userTypeService.update(userT)){
				super.write("更新成功");
			}else{
				super.writeError("更新失败");
			}
		}catch(Exception e){
			logger.error("update user type error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="删除")
	public String remove() throws CoreException{
		logger.debug("reomve userType["+userT.toString()+"].");
		try{
			if(userT == null){
				super.writeError("请输入用户类型信息.");
				return ResultType.SUCCESS;
			}
			if(userTypeService.deleteById(userT.getTypeId())){
				super.write("删除成功");
			}else{
				super.writeError("删除失败");
			}
		}catch(Exception e){
			logger.error("remove user type error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}

	public UserType getUserT() {
		return userT;
	}

	public void setUserT(UserType userT) {
		this.userT = userT;
	}
	
	@Override
	public UserType getModel() {
		return userT;
	}

}
