package com.rency.dpf.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.annotation.Secured;
import org.rency.utils.common.CONST;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.tools.BrowserUtils;
import org.rency.utils.tools.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rency.dpf.beans.User;
import com.rency.dpf.service.UserService;

@Component("userAction")
@Scope("prototype")
@Log(logName="用户",logType=LogType.MANAGEMENT)
public class UserAction extends BasicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	private static final Logger logger = LoggerFactory.getLogger(UserAction.class);

	@Autowired
	private UserService userService;
	private User user;
	private String j_username;
	private String j_password;
	private String newPassword;
	
	@Secured
	@Log(logName="请求登录")
	public String loginInput() throws CoreException{
		if(session.containsKey(CONST.SESSION_USER_KEY)){
			User user = (User) session.get(CONST.SESSION_USER_KEY);
			if(user != null){
				return ResultType.SUCCESS;
			}
		}
		return ResultType.USER_LOGIN;
	}
	
	@Secured
	@Log(logName="登录")
	public String login() throws CoreException{
		if(StringUtils.isBlank(j_username) && StringUtils.isBlank(j_password)){
			this.addFieldError(ResultType.RESPONSE_MESSAGE_KEY,"请输入用户信息");
			return ResultType.USER_LOGIN;
		}
		User loginUser = userService.queryByName(j_username);
		if(loginUser == null){
			logger.debug("user["+j_username+"] dose not exists.");
			this.addFieldError(ResultType.RESPONSE_MESSAGE_KEY,"用户不存在");
			return ResultType.USER_LOGIN;
		}
		if((MD5Utils.checkPassword(j_password,loginUser.getPassword()))){
			String ipaddr = ServletActionContext.getRequest().getRemoteAddr();
			logger.debug("user["+loginUser.toString()+"] login success, and browser type is "+BrowserUtils.checkBrowse(ServletActionContext.getRequest()));
			userService.updateLoginInfo(loginUser.getGuid(), new Date(), ipaddr,true);
			session.put(CONST.SESSION_USER_KEY, loginUser);
			return ResultType.SUCCESS;
			
		}else{
			logger.debug("user["+j_username+"] password error.");
			this.addFieldError(ResultType.RESPONSE_MESSAGE_KEY,"密码错误");
			return ResultType.USER_LOGIN;
		}
	}
	
	@Log(logName="注销")
	public String logout() throws CoreException{
		if(session.containsKey(CONST.SESSION_USER_KEY)){
			User user = (User) session.get(CONST.SESSION_USER_KEY);
			logger.debug("user["+user.toString()+"] logout success.");
			userService.updateLoginInfo(user.getGuid(), null, null,false);
			session.remove(CONST.SESSION_USER_KEY);
		}
		return ResultType.USER_LOGIN;
	}

	@JSON
	@Log(logName="添加")
	public String save() throws CoreException{
		logger.debug("save user["+user.toString()+"]");
		try{
			if(user == null){
				super.writeError("请填写用户信息");
				return ResultType.SUCCESS;
			}
			if(userService.add(user)){
				super.write("添加成功");
			}else{
				super.writeError("添加失败");
			}
		}catch(Exception e){
			logger.error("load all user list error."+e);
			e.printStackTrace();
			super.writeError("添加失败");
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="删除")
	public String remove() throws CoreException{
		logger.debug("remove user["+user.toString()+"]");
		try{
			if(StringUtils.isBlank(user.getGuid())){
				super.writeError("请提交删除用户信息");
				return ResultType.SUCCESS;
			}
			if(userService.deleteByGuid(user.getGuid())){
				super.write("删除成功");
			}else{
				super.writeError("删除失败");
			}
		}catch(Exception e){
			logger.error("remove user error."+e);
			e.printStackTrace();
			super.writeError("删除失败");
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="加载")
	public String load() throws CoreException{
		List<User> userList = new ArrayList<User>();
		try{
			userList = userService.listWithoutSuper();
			logger.debug("load all user list."+userList.size());
		}catch(Exception e){
			logger.error("load all user list error."+e);
			e.printStackTrace();
			super.writeError("加载失败");
		}
		super.writeRows(userList);
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="修改密码")
	public String modifyPassword()throws CoreException{
		try{
			if(session.containsKey(CONST.SESSION_USER_KEY)){
				User user = (User) session.get(CONST.SESSION_USER_KEY);
				logger.debug("modify user["+user.toString()+"] password.");
				if((MD5Utils.checkPassword(j_password,user.getPassword()))){
					if(userService.updatePassword(user.getGuid(), newPassword)){
						user.setPassword(MD5Utils.getMD5String(newPassword));
						session.put(CONST.SESSION_USER_KEY, user);
						super.write("修改成功");
					}else{
						super.writeError("修改失败");
					}
				}else{
					logger.debug("user["+j_username+"] password error.");
					super.writeError("旧密码不正确");
				}
			}else{
				super.writeError("修改失败");
			}
		}catch(Exception e){
			logger.error("modify user password error."+e);
			e.printStackTrace();
			super.writeError("修改失败");
		}
		return ResultType.SUCCESS;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getJ_username() {
		return j_username;
	}

	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	public String getJ_password() {
		return j_password;
	}

	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}