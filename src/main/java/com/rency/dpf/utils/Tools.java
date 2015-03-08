package com.rency.dpf.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.rency.utils.common.CONST;
import org.rency.utils.common.ResultType;
import org.rency.utils.exceptions.CoreException;

import com.rency.dpf.beans.Menus;

public class Tools {

	/**
	 * @desc 格式化树形资源节点
	 * @author user_rcy@163.com
	 * @date 2014年6月11日 下午7:30:10
	 * @param resourcesList 节点列表
	 * @param resources 格式化后节点
	 * @param resourcesId 父节点ID
	 */
	public static void formatMenu(List<Menus> menusList,HashMap<String, Object> resources,String menusId) throws CoreException{
		
		for(Menus r : menusList){
			
			if(menusId.equals(r.getParentResId())){//是子节点
				@SuppressWarnings("unchecked")
				List<HashMap<String, Object>> childrens = (List<HashMap<String, Object>>) resources.get("children");
				if(childrens == null){
					childrens = new ArrayList<HashMap<String, Object>>();
				}
				HashMap<String, Object> children = new HashMap<String, Object>();
				children.put("id", r.getResId());
				children.put("text", r.getResName());
				if(!r.isLeaf()){
					children.put("state", "open");
					formatMenu(menusList,children,r.getResId());
				}else{
					HashMap<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("url",r.getHref());
					children.put("attributes",attributes);
				}
				childrens.add(children);
				resources.put("children", childrens);
			}
		}
	}

	/**
	 * @desc 弹出Javascript警告框
	 * @date 2014年9月24日 上午10:32:18
	 * @param request
	 * @param alertMessage 弹出信息
	 * @param redirectURL 跳转地址。如果为空则不跳转
	 * @return
	 * @throws CoreException
	 */
	public static String alert(HttpServletRequest request,String alertMessage,String redirectURL){
		StringBuilder builder = new StringBuilder();
		builder.append("<script type='text/javascript' language='javascript'>");
		builder.append("alert('"+alertMessage+"');");
		if(StringUtils.isNotBlank(redirectURL)){
			builder.append("window.top.location.href='");
			builder.append(request.getContextPath()+"/");
			builder.append(redirectURL);
			builder.append("';");
		}
		builder.append("</script>");
		return builder.toString();
	}
	
	/**
	 * @desc 输出字符串
	 * @date 2014年9月13日 上午10:33:29
	 * @param response
	 * @param data
	 */
	public static void outputData(HttpServletResponse response, String data) {
		response.setContentType("text/html;charset=" + CONST.CHARSET);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * @desc 返回JSON错误消息字符串
	 * @date 2014年12月15日 下午12:18:08
	 * @param message 错误消息
	 * @return
	 */
	public static String formatJSON(String message){
		JSONObject jsonObject = new JSONObject();
		HashMap<String, Object> responseMap = new HashMap<String,Object>();
		responseMap.put(ResultType.RESPONSE_TYPE_KEY, ResultType.RESPONSE_TYPE_ERROR);
		responseMap.put(ResultType.RESPONSE_INFO_KEY, message);
		responseMap.put(ResultType.RESPONSE_ROWS_KEY, "");
		jsonObject.putAll(responseMap);
		return jsonObject.toString();
	}
}