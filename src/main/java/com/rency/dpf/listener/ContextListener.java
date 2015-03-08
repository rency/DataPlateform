package com.rency.dpf.listener;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.rency.utils.common.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rency.dpf.beans.CrawlerConfig;
import com.rency.dpf.service.CrawlerConfigService;

/**
 * 
* @ClassName: ContextListener
* @Description: 容器启动、停止监听器
* @author user_rcy@163.com
* @date 2014年12月21日 下午4:24:57
* @version V1.0
 */
public class ContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(ContextListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("》》》》》》关闭容器");
		try {
			stopCrawler();
		} catch (Exception e) {
			logger.error("关闭容器时发生异常.",e);
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("》》》》》》启动容器");
	}

	/**
	 * @desc 如果爬虫状态由于服务异常终止，则在启动时修改爬虫状态
	 * @date 2014年12月30日 上午9:50:08
	 * @throws Exception
	 */
	private void stopCrawler() throws Exception{
		try {
			CrawlerConfigService crawlerConfigService = SpringContextHolder.getBean(CrawlerConfigService.class);
			List<CrawlerConfig> crawlerList = crawlerConfigService.loadAll();
			for(CrawlerConfig crawler : crawlerList){
				if(crawler.isStatus()){
					crawlerConfigService.stop(crawler.getCrawlerId());
					logger.info("停止爬虫服务."+crawler.toString());
				}
			}
		} catch (Exception e) {
			logger.error("停止爬虫服务异常.",e);
			e.printStackTrace();
			throw e;
		}
	}
	
}