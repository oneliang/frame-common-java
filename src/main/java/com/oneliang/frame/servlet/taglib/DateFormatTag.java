package com.oneliang.frame.servlet.taglib;

import java.util.Date;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TimeUtil;
import com.oneliang.util.log.Logger;

public class DateFormatTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3876792310864662550L;

	private static final Logger logger=Logger.getLogger(DateFormatTag.class);

	private String value=null;
	private String originalFormat=null;
	private String format=TimeUtil.YEAR_MONTH_DAY;
	private String originalLanguage=null;
	private String language=null;
	private String originalCountry=null;
	private String country=null;

	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		if(StringUtil.isNotBlank(this.value)){
			try {
				Locale originalLocale=null;
				if(StringUtil.isNotBlank(this.originalLanguage)&&StringUtil.isNotBlank(this.originalCountry)){
					originalLocale=new Locale(this.originalLanguage,this.originalCountry);
				}else if(StringUtil.isNotBlank(this.originalLanguage)){
					originalLocale=new Locale(this.originalLanguage);
				}else{
					originalLocale=Locale.getDefault();
				}
				Locale locale=null;
				if(StringUtil.isNotBlank(this.language)&&StringUtil.isNotBlank(this.country)){
					locale=new Locale(this.language,this.country);
				}else if(StringUtil.isNotBlank(this.language)){
					locale=new Locale(this.language);
				}else{
					locale=Locale.getDefault();
				}
				String originalFormat=null;
				if(StringUtil.isNotBlank(this.originalFormat)){
					originalFormat=this.originalFormat;
				}else{
					originalFormat=TimeUtil.DEFAULT_DATE_FORMAT;
				}
				Date date=TimeUtil.stringToDate(this.value,originalFormat,originalLocale);
				String dateString=TimeUtil.dateToString(date,this.format,locale);
				this.pageContext.getOut().print(dateString);
			} catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
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
	 * @return the originalFormat
	 */
	public String getOriginalFormat() {
		return originalFormat;
	}

	/**
	 * @param originalFormat the originalFormat to set
	 */
	public void setOriginalFormat(String originalFormat) {
		this.originalFormat = originalFormat;
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

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the originalLanguage
	 */
	public String getOriginalLanguage() {
		return originalLanguage;
	}

	/**
	 * @param originalLanguage the originalLanguage to set
	 */
	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	/**
	 * @return the originalCountry
	 */
	public String getOriginalCountry() {
		return originalCountry;
	}

	/**
	 * @param originalCountry the originalCountry to set
	 */
	public void setOriginalCountry(String originalCountry) {
		this.originalCountry = originalCountry;
	}
}
