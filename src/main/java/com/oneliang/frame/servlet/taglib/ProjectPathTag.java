package com.oneliang.frame.servlet.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.log.Logger;

public class ProjectPathTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1688901633941253335L;

	private static final Logger logger=Logger.getLogger(ProjectPathTag.class);

	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
		String path = request.getContextPath();
		try {
			pageContext.getOut().print(path);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}
}
