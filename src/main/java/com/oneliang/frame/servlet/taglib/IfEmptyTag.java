package com.oneliang.frame.servlet.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;

public class IfEmptyTag extends BodyTagSupport {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 300829271738694544L;
	
	private String value=null;
	private String scope=null;
	
	/**
	 * doStartTag
	 */
	public int doStartTag() throws JspException{
		this.value=StringUtil.nullToBlank(this.value);
		this.scope=StringUtil.nullToBlank(this.scope);
		Object o=null;
		if (this.scope.equals(Constant.RequestScope.SESSION)) {
			o=this.pageContext.getSession().getAttribute(this.value);
		} else {
			o=this.pageContext.getRequest().getAttribute(this.value);
		}
		int eval=EVAL_PAGE;
		if(o==null){
			eval=EVAL_BODY_INCLUDE;
		}else{
			if(o instanceof List){
				List<?> list=(List<?>)o;
				if(list.isEmpty()){
					eval=EVAL_BODY_INCLUDE;
				}
			}
		}
		return eval;
	}
	
	/**
	 * doEndTag
	 */
	public int doEndTag() throws JspException{
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
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
}
