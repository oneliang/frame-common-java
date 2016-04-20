package com.oneliang.frame.servlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.log.Logger;

public class RemoteHostTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4623150037980794467L;

	private static final Logger logger=Logger.getLogger(RemoteHostTag.class);

	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		String remoteHost=pageContext.getRequest().getRemoteHost();
		try {
			pageContext.getOut().print(remoteHost);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}
}