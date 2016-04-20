package com.oneliang.frame.servlet.action;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract interface ActionInterface{
	
	/**
	 * common action name
	 */
	public static final String ACTION="action";
	/**
	 * request type
	 */
	public static final String REQUEST_TYPE="requestType";
	/**
	 * common action type
	 */
	public static final String ACTION_LIST="list";
	public static final String ACTION_ADD="add";
	public static final String ACTION_ADD_SUBMIT="addSubmit";
	public static final String ACTION_MODIFY="modify";
	public static final String ACTION_MODIFY_SUBMIT="modifySubmit";
	public static final String ACTION_DELETE="delete";
	public static final String ACTION_VIEW="view";
	public static final String ACTION_EXPORT="export";
	public static final String ACTION_GOTO_PAGE="gotoPage";
	/**
	 * request type include ajax and not ajax
	 */
	public static final String REQUEST_TYPE_AJAX="ajax";
	public static final String REQUEST_TYPE_NOT_AJAX="notAjax";
	
	/**
	 * common forward type
	 */
	public static final String FORWARD_LIST="list";
	public static final String FORWARD_ADD="add";
	public static final String FORWARD_ADD_SUBMIT="add.submit";
	public static final String FORWARD_MODIFY="modify";
	public static final String FORWARD_MODIFY_SUBMIT="modify.submit";
	public static final String FORWARD_DELETE="delete";
	public static final String FORWARD_VIEW="view";
	public static final String FORWARD_GOTO_PAGE="goto.page";
	
	/**
	 * <p>
	 * abstract Method: This method is abstract
	 * </p>
	 * 
	 * This method is to execute
	 */
	public abstract String execute(ServletRequest request, ServletResponse response) throws ActionExecuteException;

	/**
	 * @author oneliang
	 */
	public static enum HttpRequestMethod{
		PUT(0x01),DELETE(0x02),GET(0x04),POST(0x08),HEAD(0x10),OPTIONS(0x20),TRACE(0x40);
		private int code=0;
		/**
		 * emnu constructor
		 * @param code
		 */
		HttpRequestMethod(int code){
			this.code=code;
		}
		/**
		 * get code
		 * @return int
		 */
		public int getCode(){
			return this.code;
		}
	}
}
