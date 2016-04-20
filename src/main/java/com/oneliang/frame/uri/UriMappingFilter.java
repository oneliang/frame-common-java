package com.oneliang.frame.uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.oneliang.util.common.StringUtil;

public class UriMappingFilter implements Filter {

	private static final String REGEX = "\\{[\\w]*\\}";
	private static final String FIRST_REGEX="\\{";

	private static final Map<String,String> uriMappingCache=new ConcurrentHashMap<String, String>();

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		String uri=httpServletRequest.getRequestURI();
		int front=httpServletRequest.getContextPath().length();
		uri=uri.substring(front,uri.length());
		if(uriMappingCache.containsKey(uri)){
			String uriTo=uriMappingCache.get(uri);
			servletRequest.getRequestDispatcher(uriTo).forward(servletRequest, servletResponse);
		}else{
			String uriTo=UriMappingContext.findUriTo(uri);
			if(StringUtil.isNotBlank(uriTo)){
				uriMappingCache.put(uri, uriTo);
				servletRequest.getRequestDispatcher(uriTo).forward(servletRequest, servletResponse);
			}else{
				filterChain.doFilter(servletRequest, servletResponse);
			}
		}
	}

	public void destroy() {
		uriMappingCache.clear();
	}

	public static void main(String[] args){
		String uri="/portal_aa_bb_123.html";
		String regex="^/portal_([a-z]+)_([a-z]+)_([\\d]+).html$";
		String mapping="/portal/portalIndex/index.do?a={0}&b={1}&id={2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(uri);
		int groupCount=m.groupCount();
		int count=1;
		List<String> groupList=new ArrayList<String>();
		if(m.find()){
			while (count<=groupCount) {
				groupList.add(m.group(count));
				count++;
			}
		}
		List<String> parameterList=StringUtil.parseStringGroup(mapping, REGEX, FIRST_REGEX, StringUtil.BLANK, 1);
		for(String parameter:parameterList){
			mapping=mapping.replaceFirst(REGEX,groupList.get(Integer.parseInt(parameter)));
		}
		System.out.println(mapping);
	}
}
