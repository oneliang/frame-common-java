package com.oneliang.frame.servlet.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constants;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class BasePathTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8252008854114747305L;

	private static final Logger logger=LoggerManager.getLogger(BasePathTag.class);

	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + Constants.Symbol.COLON+Constants.Symbol.SLASH_LEFT+Constants.Symbol.SLASH_LEFT+request.getServerName() + Constants.Symbol.COLON + request.getServerPort()+ path+Constants.Symbol.SLASH_LEFT;
		try {
			pageContext.getOut().print(basePath);
		} catch (Exception e) {
			logger.error(Constants.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}
}
