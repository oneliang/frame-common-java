package com.oneliang.frame.servlet.taglib;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constants;
import com.oneliang.frame.i18n.MessageContext;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class MessageTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4127533235059676726L;

	private static final Logger logger=LoggerManager.getLogger(MessageTag.class);

	private static final String LOCALE="locale";

	private String key=null;
	private String locale=null;
	
	public int doStartTag() throws JspException{
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException{
		try {
			String locale=null;
			String localeParameter=this.pageContext.getRequest().getParameter(LOCALE);
			String localeKey=Locale.getDefault().toString();
			if(StringUtil.isBlank(this.locale)){
				if(StringUtil.isBlank(localeParameter)){
					locale=localeKey;
				}else{
					locale=localeParameter;
				}
			}else{
				locale=this.locale;
			}
			Properties properties=MessageContext.getMessageProperties(locale);
			String value=this.key;
			if(properties!=null){
				value=properties.getProperty(this.key);
			}
			this.pageContext.getOut().print(value);
		} catch (Exception e) {
			logger.error(Constants.Base.EXCEPTION, e);
		}
		return EVAL_PAGE;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
}
