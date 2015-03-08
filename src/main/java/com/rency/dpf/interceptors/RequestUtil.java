package com.rency.dpf.interceptors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class RequestUtil {
	
	public static String buildQueryString(HttpServletRequest request){
		String queryString = request.getQueryString();
		StringBuffer finalQs = new StringBuffer();
		if(StringUtils.isNotBlank(queryString)){
			finalQs.append(queryString);
		}else{
			queryString = getRequestParameters(request);
			if(StringUtils.isNotBlank(queryString)){
				finalQs.append(queryString);
			}
		}
		return finalQs.length() == 0 ? null : finalQs.toString();
	}

	public static String getRequestParameters(HttpServletRequest aRequest) {
		return createQueryStringFromMap(aRequest.getParameterMap(), "&").toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StringBuffer createQueryStringFromMap(Map m, String ampersand) {
		StringBuffer aReturn = new StringBuffer("");
		Set aEntryS = m.entrySet();
		Iterator aEntryI = aEntryS.iterator();
		while (aEntryI.hasNext()) {
			Map.Entry aEntry = (Map.Entry) aEntryI.next();
			Object value = aEntry.getValue();
			String[] aValues = new String[1];
			if (value == null) {
				aValues[0] = "";
			} else if ((value instanceof List)) {
				List aList = (List) value;
				aValues = (String[]) (String[]) aList.toArray(new String[aList.size()]);
			} else if ((value instanceof String)) {
				aValues[0] = ((String) value);
			} else {
				aValues = (String[]) (String[]) value;
			}
			for (int i = 0; i < aValues.length; i++) {
				append(aEntry.getKey(), aValues[i], aReturn, ampersand);
			}
		}
		return aReturn;
	}

	private static StringBuffer append(Object key, Object value,
			StringBuffer queryString, String ampersand) {
		if (queryString.length() > 0) {
			queryString.append(ampersand);
		}
		try {
			queryString.append(URLEncoder.encode(key.toString(), "UTF-8"));
			queryString.append("=");
			queryString.append(URLEncoder.encode(value.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return queryString;
	}
}