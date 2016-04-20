package com.oneliang.frame.servlet;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.file.FileUtil;
import com.oneliang.util.log.Logger;

public final class ActionUtil{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5172649641869938138L;

	private static final Logger logger=Logger.getLogger(ActionUtil.class);

	private static final String REGEX = "\\{[\\w]*\\}";
	private static final String FIRST_REGEX="\\{";
	
	private static ThreadLocal<ServletBean> servletBeanThreadLocal=new ThreadLocal<ServletBean>();

	private ActionUtil(){}

	/**
	 * set servlet bean
	 * @param servletBean
	 */
	static void setServletBean(ServletBean servletBean){
		servletBeanThreadLocal.set(servletBean);
	}

	/**
	 * get servlet bean
	 * @return ServletBean
	 */
	static ServletBean getServletBean(){
		return servletBeanThreadLocal.get();
	}

	/**
	 * get servlet context
	 * @return ServletContext
	 */
	public static ServletContext getServletContext(){
		return servletBeanThreadLocal.get().getServletContext();
	}
	
	/**
	 * get servlet request
	 * @return ServletRequest
	 */
	public static ServletRequest getServletRequest(){
		return servletBeanThreadLocal.get().getServletRequest();
	}
	
	/**
	 * get servlet response
	 * @return ServletResponse
	 */
	public static ServletResponse getServletResponse(){
		return servletBeanThreadLocal.get().getServletResponse();
	}
	
	/**
	 * parse forward path and replace the parameter value
	 * all by request.setAttribute() and request.getAttribute()
	 * @param path
	 * @return path
	 */
	static String parsePath(String path){
		ServletRequest request=getServletRequest();
		List<String> attributeList=StringUtil.parseStringGroup(path,REGEX,FIRST_REGEX,StringUtil.BLANK,1);
		if(attributeList!=null){
			for(String attribute:attributeList){
				Object attributeValue=request.getAttribute(attribute);
				path=path.replaceFirst(REGEX, attributeValue==null?StringUtil.BLANK:attributeValue.toString());
			}
		}
		return path;
	}

	/**
	 * include jsp,for servlet(ActionListener) use.
	 * @param jspUriPath
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 * @throws ServletException
	 */
	public static ByteArrayOutputStream includeJsp(String jspUriPath) throws ServletException, IOException{
		ServletRequest request=getServletRequest();
		ServletResponse response=getServletResponse();
		return includeJsp(jspUriPath,request,response);
	}

	/**
	 * include jsp,and return the byte array output stream
	 * @param jspUriPath
	 * @param servletRequest
	 * @param servletResponse
	 * @return ByteArrayOutputStream
	 * @throws IOException
	 * @throws ServletException
	 */
	public static ByteArrayOutputStream includeJsp(String jspUriPath,ServletRequest servletRequest,ServletResponse servletResponse) throws ServletException, IOException{
		RequestDispatcher requestDispatcher=servletRequest.getRequestDispatcher(jspUriPath);
		final ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		final ServletOutputStream servletOuputStream=new ServletOutputStream(){
			public void write(byte[] b, int off, int len) {
				byteArrayOutputStream.write(b, off, len);
			}
			public void write(int b) {
				byteArrayOutputStream.write(b);
			}
		};
		final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(byteArrayOutputStream));
		HttpServletResponse httpServletResponse = new HttpServletResponseWrapper((HttpServletResponse)servletResponse){
			public ServletOutputStream getOutputStream() {
				return servletOuputStream;
			}
			public PrintWriter getWriter() {
				return printWriter;
			}
		};
		requestDispatcher.include(servletRequest, httpServletResponse);
		printWriter.flush();
		return byteArrayOutputStream;
	}

	/**
	 * include jsp and save,for servlet(ActionListener) use.
	 * @param jspUriPath
	 * @param saveFullFilename
	 * @return boolean
	 */
	public static boolean includeJspAndSave(String jspUriPath,String saveFullFilename){
		ServletRequest request=getServletRequest();
		ServletResponse response=getServletResponse();
		return includeJspAndSave(jspUriPath,saveFullFilename,request,response);
	}

	/**
	 * include jsp and save
	 * @param jspUriPath
	 * @param saveFullFilename
	 * @param servletRequest
	 * @param servletResponse
	 * @return boolean
	 */
	public static boolean includeJspAndSave(String jspUriPath,String saveFullFilename,ServletRequest servletRequest,ServletResponse servletResponse){
		boolean result=false;
		try{
			ByteArrayOutputStream byteArrayOutputStream=includeJsp(jspUriPath,servletRequest,servletResponse);
			FileUtil.createFile(saveFullFilename);
			FileOutputStream fileOutputStream=new FileOutputStream(saveFullFilename);
			BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(fileOutputStream,Constant.Encoding.UTF8));
			bufferedWriter.write(new String(byteArrayOutputStream.toByteArray()));
			bufferedWriter.flush();
			bufferedWriter.close();
			result=true;
		}catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return result;
	}

	static class ServletBean implements Serializable{
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -1505174208109390941L;
		
		private ServletContext servletContext=null;
		private ServletRequest servletRequest=null;
		private ServletResponse servletResponse=null;
		/**
		 * @return the servletContext
		 */
		public ServletContext getServletContext() {
			return servletContext;
		}
		/**
		 * @param servletContext the servletContext to set
		 */
		public void setServletContext(ServletContext servletContext) {
			this.servletContext = servletContext;
		}
		/**
		 * @return the servletRequest
		 */
		public ServletRequest getServletRequest() {
			return servletRequest;
		}
		/**
		 * @param servletRequest the servletRequest to set
		 */
		public void setServletRequest(ServletRequest servletRequest) {
			this.servletRequest = servletRequest;
		}
		/**
		 * @return the servletResponse
		 */
		public ServletResponse getServletResponse() {
			return servletResponse;
		}
		/**
		 * @param servletResponse the servletResponse to set
		 */
		public void setServletResponse(ServletResponse servletResponse) {
			this.servletResponse = servletResponse;
		}
	}
}