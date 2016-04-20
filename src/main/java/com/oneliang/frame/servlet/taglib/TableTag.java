package com.oneliang.frame.servlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.log.Logger;

/**
 * @author Dandelion
 * @since 2008-11-06
 */
public class TableTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3944273586211542011L;

	private static final Logger logger=Logger.getLogger(TableTag.class);

	private String tableString=null;

	/**
	 * <p>
	 * Method: override method do start tag
	 * </p>
	 */
	public int doStartTag() throws JspException {
		this.tableString = StringUtil.nullToBlank(this.tableString);
		String startTable = "<table "+this.tableString+">";
		try {
			this.pageContext.getOut().print(startTable);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * <p>
	 * Method: override method do end tag
	 * </p>
	 */
	public int doEndTag() throws JspException {
		String endTable = "</table>";
		try {
			this.pageContext.getOut().print(endTable);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}

	/**
	 * @return the tableString
	 */
	public String getTableString() {
		return tableString;
	}

	/**
	 * @param tableString the tableString to set
	 */
	public void setTableString(String tableString) {
		this.tableString = tableString;
	}
}
