package com.rency.dpf.actions;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;
import org.rency.lucene.beans.SearchResult;
import org.rency.lucene.core.LuceneConfiguration;
import org.rency.lucene.service.LuceneService;
import org.rency.lucene.service.SearchService;
import org.rency.lucene.utils.LuceneDict;
import org.rency.utils.annotation.Log;
import org.rency.utils.annotation.LogType;
import org.rency.utils.common.CONST;
import org.rency.utils.common.PageQueryResult;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("luceneAction")
@Scope("prototype")
@Log(logName="索引",logType=LogType.LUCENE)
public class LuceneAction extends BasicAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862864564806809951L;
	
	private static final Logger logger = LoggerFactory.getLogger(LuceneAction.class);
	
	@Autowired
	@Qualifier("luceneService")
	private LuceneService luceneService;
	
	@Autowired
	@Qualifier("searchService")
	private SearchService searchService;

	private String keyword;
	private int pageNo = 0;
	private int pageSize = 0;
	
	@JSON
	@Log(logName="创建")
	public String startIndex() throws CoreException{
		try{
			LuceneConfiguration cfg = new LuceneConfiguration(ServletActionContext.getRequest().getSession().getId(),LuceneDict.LUCENE_INDEX_STORE_FILE,LuceneDict.INDEX_PATH);
			if(luceneService.startIndex(cfg)){
				super.write("启动成功");
			}else{
				super.writeError("启动失败");
			}
		}catch(IllegalArgumentException e){
			logger.error("start lucene error.",e);
			e.printStackTrace();
			super.writeError(e.getMessage());
		}catch(RuntimeException e){
			logger.error("start lucene error.",e);
			e.printStackTrace();
			super.writeError(e.getMessage());
		}catch(Exception e){
			logger.error("start lucene error.",e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}
	
	@Log(logName="停止")
	public String stop() throws CoreException{
		return "";
	}
	
	@Log(logName="请求搜索")
	public String search()throws CoreException{
		return ResultType.INPUT;
	}
	
	@Log(logName="搜索")
	public String searchDo() throws CoreException{
		try{
			if(StringUtils.isBlank(keyword)){
				return "search_success";
			}
			int no = pageNo > 1 ? pageNo : 1;
			int size = pageSize > 1 ? pageSize : CONST.PAGE_SIZE;
			PageQueryResult<SearchResult> pqr= searchService.queryByKeyword(keyword, no, size);
			request.put("pqr", pqr);
		}catch(Exception e){
			logger.error("search error.",e);
			e.printStackTrace();
			throw new CoreException(super.getSysError());
		}
		return "search_success";
	}
	
	@JSON
	@Log(logName="搜索")
	public String doSearch() throws CoreException{
		try{
			if(StringUtils.isBlank(keyword)){
				super.writeError("请输入搜索关键字！");
				return ResultType.SUCCESS;
			}
			int no = pageNo > 1 ? pageNo : 1;
			int size = pageSize > 1 ? pageSize : CONST.PAGE_SIZE;
			PageQueryResult<SearchResult> pqr= searchService.queryByKeyword(keyword, no, size);
			super.writeRows(pqr);
		}catch(Exception e){
			logger.error("search error.",e);
			e.printStackTrace();
			super.writeError(super.getSysError());
		}
		return ResultType.SUCCESS;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}