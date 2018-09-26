package com.oneliang.frame.servlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constants;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TagUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;
/**
 * @author Dandelion
 * @since 2008-11-06
 */
public class TableHeaderTag extends BodyTagSupport{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1390170675598454919L;

	private static final Logger logger=LoggerManager.getLogger(TableHeaderTag.class);

	private String trString=null;
	
	/**
	 * <p>
	 * Method: override method do start tag
	 * </p>
	 */
	public int doStartTag() throws JspException{
		this.trString = StringUtil.nullToBlank(this.trString);
		String startTr = "<tr "+this.trString+">";
		try {
			this.pageContext.getOut().print(startTr);
		} catch (Exception e) {
			logger.error(Constants.Base.EXCEPTION, e);
		}
		return EVAL_BODY_BUFFERED;
	}
	/**
	 * <p>
	 * Method: override method do end tag
	 * </p>
	 */
	public int doEndTag() throws JspException {
		String thString=this.bodyContent.getString().trim();
		String[] headers=TagUtil.fieldSplit(thString);
		StringBuilder ths=new StringBuilder();
		for(String th:headers){
			th=StringUtil.trim(th);
			String[] fieldStyle=TagUtil.fieldStyleSplit(th);
			ths.append("<th");
			ths.append(fieldStyle[1]);
			ths.append(">");
			ths.append(fieldStyle[0]);
			ths.append("</th>");
		}
		String endTr="</tr>";
		try {
			this.pageContext.getOut().print(ths.toString());
			this.pageContext.getOut().print(endTr);
		} catch (Exception e) {
			logger.error(Constants.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}
	/**
	 * @return the trString
	 */
	public String getTrString() {
		return trString;
	}
	/**
	 * @param trString the trString to set
	 */
	public void setTrString(String trString) {
		this.trString = trString;
	}
}
