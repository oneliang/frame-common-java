package com.oneliang.frame.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.oneliang.Constants;
/**
 * <p>
 * Class: EncodingFilter class
 * </p>
 * 
 * com.lwx.frame.filter.EncodingFilter.java
 * This is a encoding filter in commonFrame
 * @author Dandelion
 * @since 2008-07-31
 */
public class EncodingFilter implements Filter {
	
	private static final String DEFAULT_ENCODING=Constants.Encoding.UTF8;
	private static final String ENCODING="encoding";
	private static final String IGNORE="ignore";
	
	private String encoding = null;
	private FilterConfig filterConfig = null;
	private boolean ignore = false;

	/**
	 * <p>Method: public void destory()</p>
	 * This method will be reset the encoding=null and filterconfig=null
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}
	
	/**
	 * <p>Method: public void doFilter(ServletRequest,ServletResponse,FilterChain) throws IOException,ServletException</p>
	 * @param servletRequest
	 * @param servletResponse
	 * @param filterChain
	 * @throws IOException,ServletException
	 * This method will be doFilter in request scope and response scope
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (!this.ignore) {
			servletRequest.setCharacterEncoding(this.encoding);
			servletResponse.setCharacterEncoding(this.encoding);
		}
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (ServletException sx) {
			this.filterConfig.getServletContext().log(sx.getMessage());
		} catch (IOException iox) {
			this.filterConfig.getServletContext().log(iox.getMessage());
		}
	}
	
	/**
	 * <p>Method: public void init(FilterConfig filterConfig) throws ServletException</p>
	 * @param filterConfig
	 * @throws ServletException
	 * This method will be initial the key 'encoding' and 'ignore' in web.xml  
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		// read from web.xml to initial the key 'encoding' and 'ignore'
		String encoding = filterConfig.getInitParameter(ENCODING);
		String ignore = filterConfig.getInitParameter(IGNORE);
		if(encoding==null){
			this.encoding=DEFAULT_ENCODING;
		}else{
			this.encoding=encoding;
		}
		if (ignore == null)
			this.ignore = false;
		else if (ignore.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (ignore.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}
}
