package com.rency.dpf.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.CONST;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ModelDriven;
import com.rency.dpf.beans.Menus;
import com.rency.dpf.beans.User;
import com.rency.dpf.service.MenusService;
import com.rency.dpf.utils.Tools;

@Component("menusAction")
@Scope("prototype")
@Log(logName="菜单",logType=LogType.MANAGEMENT)
public class MenusAction extends BasicAction implements ModelDriven<Menus>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	private static final Logger logger = LoggerFactory.getLogger(MenusAction.class);
	
	@Autowired
	@Resource(name="menusService")
	private MenusService menusService;

	private Menus menus = new Menus();
	
	/**
	 * @desc 加载树形结构
	 * @date 2014年7月24日 下午3:39:48
	 * @return
	 * @throws CoreException
	 */
	@JSON
	@Log(logName="加载树形结构")
	public String loadMenu() throws CoreException{
		try{
			if(!session.containsKey(CONST.SESSION_USER_KEY)){
				logger.error("load tree menus failed, and user is null.");
				throw new CoreException(super.getSysError());
			}
			
			User user = (User) session.get(CONST.SESSION_USER_KEY);
			logger.debug("******load menus with "+user.getUserType());
			List<Menus> menusList = menusService.queryMenus(user.getUserType());
			if( menusList == null || menusList.size() == 0){
				logger.error("load tree menu resources failed, and list is empty.");
				session.put(ResultType.RESPONSE_MESSAGE_KEY, super.getSysError());
				super.writeSysError();
				return ResultType.SUCCESS;
			}	
			
			HashMap<String, Object> nodes = new HashMap<String, Object>();
			for(Menus menu : menusList ){
				if(menu.getParentResId().trim().equals("-1")){
					HashMap<String, Object> node = new HashMap<String, Object>();
					Tools.formatMenu(menusList, node, menu.getResId());
					nodes.put("id", menu.getResId());
					nodes.put("text", menu.getResName());
					nodes.put("state", "open");
					Object children = node.get("children");
					if(children != null){
						nodes.put("children", node.get("children"));
					}
					break;
				}
			}
			super.writeArray(nodes);
			return ResultType.SUCCESS;
		}catch(CoreException e){
			e.printStackTrace();
			logger.error(e.toString());
			throw new CoreException(super.getSysError());
		}
	}
	
	@JSON
	@Log(logName="根据用户查询树形结构")
	public String queryResConstruct() throws CoreException{
		try{
			
			logger.debug("******load menus with userType["+menus.getUserType().getTypeId()+"]");
			List<Menus> menusList = menusService.queryMenus(menus.getUserType());
			List<Menus> tempList = new ArrayList<Menus>(menusList);
			for(Menus res : tempList){
				if(res.getUserType().getTypeId() == CONST.USER_TYPE_SUPER){
					menusList.remove(res);//移除USER_TYPE_SUPER的信息
				}
			}
			
			HashMap<String, Object> nodes = new HashMap<String, Object>();
			for(Menus r : menusList ){
				if( r.getParentResId().trim().equals("-1")){
					HashMap<String, Object> node = new HashMap<String, Object>();
					Tools.formatMenu(menusList, node, r.getResId());
					nodes.put("id", r.getResId());
					nodes.put("text", r.getResName());
					nodes.put("state", "open");
					Object children = node.get("children");
					if(children != null){
						nodes.put("children", node.get("children"));
					}
					break;
				}
			}
			super.writeArray(nodes);
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
	}
	
	@JSON
	@Log(logName="加载")
	public String list() throws CoreException{
		try{
			List<Menus> list = menusService.list();
			if( list == null || list.size() == 0){
				return ResultType.SUCCESS;
			}
			
			List<Menus> tempList = new ArrayList<Menus>(list);
			for(Menus res : tempList){
				if( res.getUserType().getTypeId() == CONST.USER_TYPE_SUPER || res.getParentResId().equals("-1")){
					list.remove(res);//移除当前用户、超级管理员用户、根节点的信息
				}
			}
			super.writeRows(list);
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
			
		}
		
	}
	
	@JSON
	@Log(logName="根据资源名称查询")
	public String queryByResName() throws CoreException{
		List<Menus> menusList = new ArrayList<Menus>();
		try{
			if(menus == null || StringUtils.isBlank(menus.getResName())){
				logger.debug("查询条件为空");
				return ResultType.SUCCESS;
			}
			menusList = menusService.queryByResName(menus.getResName());
			logger.debug("query by resName list size is "+menusList.size());
		}catch(Exception e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
		super.writeRows(menusList);
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="添加")
	public String save() throws CoreException{
		try{
			if(menus == null){
				super.writeError("新增信息不能为空");
				return ResultType.SUCCESS;
			}
			if(StringUtils.isNotBlank(menus.getHref())){
				menus.setLeaf(true);
			}
			if(menusService.add(menus)){
				super.write("添加成功");
			}else{
				super.writeError("添加失败");
			}
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
	}

	@JSON
	@Log(logName="更新")
	public String update() throws CoreException{
		try{
			if(menus == null || StringUtils.isBlank(menus.getResId())){
				super.writeError("信息不能为空");
				return ResultType.SUCCESS;
			}
			if(menusService.update(menus)){
				super.write("更新成功");
			}else{
				super.writeError("更新失败");
			}
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
	}
	
	@JSON
	@Log(logName="删除")
	public String remove() throws CoreException{
		try{
			if(menus == null || StringUtils.isBlank(menus.getResId())){
				logger.debug("删除条件不能为空");
				return ResultType.SUCCESS;
			}
			if(menusService.deleteById(menus.getResId())){
				super.write("删除成功");
			}else{
				super.writeError("删除失败");
			}
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
	}
	
	@JSON
	@Log(logName="查询优先级")
	public String queryPriority() throws CoreException{
		try{
			if(menus.getUserType().getTypeId() < 2){
				logger.debug("query menus priority error, and user type is "+menus.getUserType().toString());
				return ResultType.SUCCESS;
			}
			List<HashMap<String, String>> priorityList = new ArrayList<HashMap<String, String>>();
			for(int i=1;i<100;i++){
				HashMap<String, String> priorityMap = new HashMap<String, String>();
				priorityMap.put("priority", ""+i);
				priorityList.add(priorityMap);
			}
			List<Menus> menusList = menusService.queryMenus(menus.getUserType());
			if(menusList == null || menusList.size() == 0){
				logger.error("query priority failed, and category type is "+menus.getUserType().toString());
				return ResultType.SUCCESS;
			}
			for(Menus c : menusList){
				HashMap<String, String> priorityMap = new HashMap<String, String>();
				priorityMap.put("priority", ""+c.getPriority());
				priorityList.remove(priorityMap);
			}
			super.writeRows(priorityList);
			return ResultType.SUCCESS;
		}catch(CoreException e){
			logger.error(e.toString());
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
		
	}

	public Menus getMenus() {
		return menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
	}
	
	@Override
	public Menus getModel() {
		return menus;
	}
}