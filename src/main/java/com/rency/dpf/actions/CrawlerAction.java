package com.rency.dpf.actions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.jsoup.Connection.Response;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.tools.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rency.dpf.beans.CrawlerConfig;
import com.rency.dpf.service.CrawlerConfigService;

@Component("crawlerAction")
@Scope("prototype")
@Log(logName="爬虫",logType=LogType.CRAWLER)
public class CrawlerAction extends BasicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerAction.class);
	
	@Autowired
	private CrawlerConfigService crawlerConfigService;
	
	private CrawlerConfig crawlerConfig;

	@JSON
	@Log(logName="加载")
	public String loadAll() throws CoreException{
		List<CrawlerConfig> crawlerConfigList = new ArrayList<CrawlerConfig>();
		try{
			crawlerConfigList = crawlerConfigService.loadAll();
			logger.debug("load all crawler config list. "+crawlerConfigList.size());
			super.writeRows(crawlerConfigList);
		}catch(Exception e){
			logger.error("load crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="根据名字查询")
	public String queryByName() throws CoreException{
		List<CrawlerConfig> crawlerConfigList = new ArrayList<CrawlerConfig>();
		try{
			if(crawlerConfig == null || StringUtils.isBlank(crawlerConfig.getCrawlerName().trim())){
				logger.debug("queryByName crawlerConfig failed, and property is null");
				super.writeError("请填写查询条件！");
				return ResultType.SUCCESS;
			}
			crawlerConfigList = crawlerConfigService.queryByName(crawlerConfig.getCrawlerName());
			logger.debug("queryByName crawler config. "+crawlerConfigList.size());
			super.writeRows(crawlerConfigList);
		}catch(Exception e){
			logger.error("queryByName error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="添加")
	public String add() throws CoreException{
		String remoteAddr = "";
		try{
			if(crawlerConfig == null){
				logger.debug("add crawlerConfig failed, and property is null");
				super.writeError("请填写信息！");
				return ResultType.SUCCESS;
			}
			remoteAddr = crawlerConfig.getRemoteAddr();
			InetAddress address = InetAddress.getByName(remoteAddr);
			if(!address.isReachable(5000)){
				throw new UnknownHostException(remoteAddr);
			}
			Response response = HttpUtils.getResponse(crawlerConfig.getInitAddr());
			if(response == null){
				throw new UnknownHostException(crawlerConfig.getInitAddr());
			}
			if(crawlerConfigService.add(crawlerConfig)){
				super.write("添加成功");
			}else{
				super.writeError("添加失败");
			}
		}catch(UnknownHostException e){
			logger.error("add remote target failed, remoteAddr can not be access."+crawlerConfig.toString(),e);
			e.printStackTrace();
			super.writeError("添加失败,远程主机["+remoteAddr+"]无法访问.");
		}catch(Exception e){
			logger.error("add crawler error.",e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="更新")
	public String update() throws CoreException{
		try{
			if(crawlerConfig == null){
				logger.debug("update crawlerConfig failed, and property is null");
				super.writeError("请填写信息！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.update(crawlerConfig)){
				super.write("更新成功");
			}else{
				super.writeError("更新失败");
			}
		}catch(Exception e){
			logger.error("update crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="删除")
	public String remove() throws CoreException{
		try{
			if(crawlerConfig == null || StringUtils.isBlank(crawlerConfig.getCrawlerId())){
				logger.debug("remove crawlerConfig failed, and property is null");
				super.writeError("请填写信息！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.deleteById(crawlerConfig.getCrawlerId())){
				super.write("删除成功");
			}else{
				super.writeError("删除失败");
			}
		}catch(Exception e){
			logger.error("remove crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="查询状态")
	public String status() throws CoreException{
		try{
			if(crawlerConfig == null || StringUtils.isBlank(crawlerConfig.getCrawlerId())){
				logger.debug("query crawler status failed, and crawlerId is null");
				super.writeError("请选择要查询的任务！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.queryCrawlerExecutorsStatus(crawlerConfig.getCrawlerId())){
				super.writeError("未运行...");
			}else{
				super.write("运行中...");
			}
		}catch(Exception e){
			logger.error("load crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="启动")
	public String start() throws CoreException{
		try{
			if(crawlerConfig == null || StringUtils.isBlank(crawlerConfig.getCrawlerId())){
				logger.debug("start crawler failed, and crawlerId is null");
				super.writeError("请选择要启动的任务！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.start(crawlerConfig.getCrawlerId(),ServletActionContext.getRequest().getSession().getId())){
				super.write("启动成功,重新加载页面以查看状态");
			}else{
				super.writeError("启动失败");
			}
		}catch(IllegalArgumentException e){
			logger.error("start crawler error."+e);
			e.printStackTrace();
			super.writeError(e.getMessage());
		}catch(RuntimeException e){
			logger.error("start crawler error."+e);
			e.printStackTrace();
			super.writeError(e.getMessage());
		}catch(Exception e){
			logger.error("start crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@JSON
	@Log(logName="停止")
	public String stop() throws CoreException{
		try{
			if(crawlerConfig == null || StringUtils.isBlank(crawlerConfig.getCrawlerId())){
				logger.debug("stop crawler failed, and crawlerId is null");
				super.writeError("请选择要停止的任务！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.stop(crawlerConfig.getCrawlerId())){
				super.write("停止成功");
			}else{
				super.writeError("停止失败");
			}
		}catch(Exception e){
			logger.error("load crawler error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}

	@JSON
	@Log(logName="清除任务队列")
	public String clearQueue() throws CoreException{
		try{
			if(StringUtils.isBlank(crawlerConfig.getCrawlerId())){
				logger.debug("clear crawler task queue, and crawlerId is null");
				super.writeError("请选择要清除任务队列的爬虫！");
				return ResultType.SUCCESS;
			}
			if(crawlerConfigService.deleteTaskQueueByCrawlerId(crawlerConfig.getCrawlerId())){
				super.write("清除任务队列成功");
			}else{
				super.writeError("清除任务队列失败");
			}
		}catch(Exception e){
			logger.error("clear crawler task error."+e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	public CrawlerConfig getCrawlerConfig() {
		return crawlerConfig;
	}

	public void setCrawlerConfig(CrawlerConfig crawlerConfig) {
		this.crawlerConfig = crawlerConfig;
	}
	
}