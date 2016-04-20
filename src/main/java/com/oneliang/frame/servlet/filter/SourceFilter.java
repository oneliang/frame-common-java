package com.oneliang.frame.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.oneliang.util.common.StringUtil;

/**
 * source filter
 * @author Dandelion
 * @since 2010-06-27
 */
public class SourceFilter implements Filter {
	
	private static final String EXCLUDE_PATH="excludePath";
	private static final String ERROR_FORWARD="errorForward";
	private static final String COMMA_SPLIT=",";
	
	private String[] excludePathArray=null;
	private String errorForward=null;

	/**
	 * initial from config file
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		String excludePaths=filterConfig.getInitParameter(EXCLUDE_PATH);
		this.errorForward=filterConfig.getInitParameter(ERROR_FORWARD);
		if(excludePaths!=null){
			String[] excludePathArray=excludePaths.split(COMMA_SPLIT);
			this.excludePathArray=new String[excludePathArray.length];
			int i=0;
			for(String excludePath:excludePathArray){
				this.excludePathArray[i]=excludePath.trim();
				i++;
			}
		}
	}
	
	/**
	 * do filter
	 */
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
//		HttpServletResponse httpResponse = (HttpServletResponse)response;
//		HttpSession session = httpRequest.getSession();
//		httpResponse.setHeader("Cache-Control","no-cache");
//		httpResponse.setHeader("Pragma","no-cache");
//		httpResponse.setDateHeader ("Expires", -1);
		String projectPath=httpRequest.getContextPath();
		String requestUri = httpRequest.getRequestURI();
		boolean excludePathThrough=false;
		if(this.excludePathArray!=null){
			for(String excludePath:this.excludePathArray){
				String path=projectPath+excludePath;
				if(StringUtil.isMatchPattern(requestUri,path)){
					excludePathThrough=true;
					break;
				}
			}
		}
		if(excludePathThrough){
			filterChain.doFilter(request, response);
		}else{
			httpRequest.getRequestDispatcher(this.errorForward).forward(request, response);
		}
	}
	
	public void destroy() {
		this.excludePathArray=null;
		this.errorForward=null;
	}
}
