package com.oneliang.frame.servlet.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.log.Logger;

public class BasePathTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8252008854114747305L;

	private static final Logger logger=Logger.getLogger(BasePathTag.class);

	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + Constant.Symbol.COLON+Constant.Symbol.SLASH_LEFT+Constant.Symbol.SLASH_LEFT+request.getServerName() + Constant.Symbol.COLON + request.getServerPort()+ path+Constant.Symbol.SLASH_LEFT;
		try {
			pageContext.getOut().print(basePath);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}
}
