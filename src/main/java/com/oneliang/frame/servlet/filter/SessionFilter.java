package com.oneliang.frame.servlet.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oneliang.Constants;
import com.oneliang.util.common.Encoder;
import com.oneliang.util.common.StringUtil;

/**
 * session filter
 * @author Dandelion
 * @since 2010-06-27
 */
public class SessionFilter implements Filter {
	
	private static final String SESSION_KEY="sessionKey";
	private static final String EXCLUDE_PATH="excludePath";
	private static final String ERROR_FORWARD="errorForward";
	private static final String COMMA_SPLIT=",";
	
	private static final String QUESTION_ENCODE=Encoder.escape("?");
	private static final String EQUAL_ENCODE=Encoder.escape("=");
	private static final String AND_ENCODE=Encoder.escape("&");
	
	private String[] sessionKeyArray=null;
	private String[] excludePathArray=null;
	private String errorForward=null;
	
	/**
	 * initial from config file
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		String sessionKeys=filterConfig.getInitParameter(SESSION_KEY);
		String excludePaths=filterConfig.getInitParameter(EXCLUDE_PATH);
		this.errorForward=filterConfig.getInitParameter(ERROR_FORWARD);
		if(sessionKeys!=null){
			String[] sessionKeyArray=sessionKeys.split(COMMA_SPLIT);
			this.sessionKeyArray=new String[sessionKeyArray.length];
			int i=0;
			for(String sessionKey:sessionKeyArray){
				this.sessionKeyArray[i]=sessionKey.trim();
				i++;
			}
		}
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
	 * sessionFilter
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession httpSession=httpRequest.getSession();
		String webRoot=httpRequest.getContextPath();
		String requestUri = httpRequest.getRequestURI();
		boolean excludePathThrough=false;
		boolean sessionThrough=false;
		if(this.excludePathArray!=null){
			for(String excludePath:this.excludePathArray){
				String path=webRoot+excludePath;
				if(StringUtil.isMatchPattern(requestUri,path)){
					excludePathThrough=true;
					break;
				}
			}
		}
		if(this.sessionKeyArray!=null){
			for(String sessionKey:this.sessionKeyArray){
				Object object=httpSession.getAttribute(sessionKey);
				if(object!=null){
					sessionThrough=true;
					break;
				}
			}
		}
		if(excludePathThrough){
			filterChain.doFilter(request, response);
		}else if(sessionThrough){
			filterChain.doFilter(request, response);
		}else{
			String uri=httpRequest.getRequestURI();
			int front=httpRequest.getContextPath().length();
			uri=uri.substring(front);
			String params=mapToParameter(request.getParameterMap());
			String errorForwardUrl=this.errorForward;
			if(errorForwardUrl.indexOf(Constants.Symbol.QUESTION_MARK)>0){
				errorForwardUrl=this.errorForward+Constants.Symbol.AND+Constants.RequestParameter.RETURN_URL+Constants.Symbol.EQUAL+uri+QUESTION_ENCODE+params;
			}else{
				errorForwardUrl=this.errorForward+Constants.Symbol.QUESTION_MARK+Constants.RequestParameter.RETURN_URL+Constants.Symbol.EQUAL+uri+QUESTION_ENCODE+params;
			}
			httpRequest.getRequestDispatcher(errorForwardUrl).forward(request, response);
		}
	}
	
	private String mapToParameter(Map<String,String[]> map){
		StringBuilder params=new StringBuilder();
		if(map!=null){
			Iterator<Entry<String,String[]>> iterator=map.entrySet().iterator();
			while(iterator.hasNext()){
			    Entry<String,String[]> entry=iterator.next();
			    String key=entry.getKey();
				String[] values=entry.getValue();
				int j=0;
				for(String value:values){
					params.append(key+EQUAL_ENCODE+value);
					if(iterator.hasNext()||j<values.length-1){
						params.append(AND_ENCODE);
					}
					j++;
				}
			}
		}
		return params.toString();
	}
	
	public void destroy() {
		this.sessionKeyArray=null;
		this.excludePathArray=null;
		this.errorForward=null;
	}
}
