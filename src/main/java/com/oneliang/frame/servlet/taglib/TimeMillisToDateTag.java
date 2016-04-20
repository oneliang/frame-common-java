package com.oneliang.frame.servlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TimeUtil;
import com.oneliang.util.log.Logger;

public class TimeMillisToDateTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4155763064911316797L;

	private static final Logger logger=Logger.getLogger(TimeMillisToDateTag.class);

	private String value=null;
	private String format=TimeUtil.YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

	public int doStartTag() throws JspException {
		if(StringUtil.isNotBlank(this.value)){
			try {
				String dateString=TimeUtil.dateToString(TimeUtil.timeMillisToDate(Long.parseLong(value)),this.format);
				this.pageContext.getOut().print(dateString);
			} catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
		return EVAL_PAGE;
	}
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
}
