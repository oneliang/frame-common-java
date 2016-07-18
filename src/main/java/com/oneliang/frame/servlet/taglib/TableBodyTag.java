package com.oneliang.frame.servlet.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.frame.bean.Page;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TagUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

/**
 * @author Dandelion
 * @since 2008-11-06
 */
public class TableBodyTag extends BodyTagSupport {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7892161495680411808L;

	private static final Logger logger=LoggerManager.getLogger(TableBodyTag.class);

	private String value = null;
	private String scope = null;
	private int wrapSize=1;
	private String trString=null;
	private String paginationValue=null;
	private String paginationScope=null;
	
	/**
	 * <p>
	 * Method: override method do start tag
	 * </p>
	 */
	public int doStartTag() throws JspException {
		this.value = StringUtil.nullToBlank(this.value);
		this.scope = StringUtil.nullToBlank(this.scope);
		this.trString = StringUtil.nullToBlank(this.trString);
		this.paginationValue=StringUtil.nullToBlank(this.paginationValue);
		this.paginationScope=StringUtil.nullToBlank(this.paginationScope);
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * <p>
	 * Method: override method do end tag
	 * </p>
	 */
	public int doEndTag() throws JspException {
		String tdString=this.bodyContent.getString().trim();
		Object o = null;
		if (!this.value.equals("")) {
			if (this.scope.equals(Constant.RequestScope.SESSION)) {
				o=this.pageContext.getSession().getAttribute(this.value);
			} else {
				o=this.pageContext.getRequest().getAttribute(this.value);
			}
			String[] fields = TagUtil.fieldSplit(tdString);
			for (int i = 0; i < fields.length; i++) {
				fields[i] = StringUtil.trim(fields[i]);
			}
			
			int currentPage=1;
			//pagination object
			Object pageObject=null;
			if(StringUtil.isNotBlank(this.paginationValue)){
				if(this.paginationScope.equals(Constant.RequestScope.SESSION)){
					pageObject=this.pageContext.getSession().getAttribute(this.paginationValue);
				}else{
					pageObject=this.pageContext.getRequest().getAttribute(this.paginationValue);
				}
				if(pageObject instanceof Page){
					Page page=(Page)pageObject;
					currentPage=page.getPage();
				}
			}
			
			if(o instanceof List){
				List<?> list=(List<?>)o;
				StringBuilder trs = new StringBuilder();
				if (list != null&&!list.isEmpty()) {
					// delete blank
					
					int count = 0;
					int index=Page.DEFAULT_ROWS*(currentPage-1);
					int total=list.size();
					for (Object object : list) {
						int totalCells=count*fields.length;
						if(totalCells%this.wrapSize==0){
							trs.append("<tr "+this.trString+">");
							index++;
						}
						count++;
						trs.append(this.tdGenerator(fields,object,index));
						totalCells=count*fields.length;
						if(totalCells%this.wrapSize==0||count==total){
							trs.append("</tr>");
						}
					}
				}
				try {
					this.pageContext.getOut().print(trs.toString());
				} catch (Exception e) {
					logger.error(Constant.Base.EXCEPTION, e);
				}
			}else if(o instanceof Object){
				StringBuilder trs = new StringBuilder();
				trs.append("<tr "+this.trString+">");
				trs.append(this.tdGenerator(fields,o,0));
				trs.append("</tr>");
			}
			
		}
		return EVAL_PAGE;
	}
	
	/**
	 * generator the tds like<td>..</td><td>..</td>
	 * @param fields
	 * @param object
	 * @param index
	 * @return tdsstring
	 */
	private String tdGenerator(String[] fields,Object object,int index){
		StringBuilder tds=new StringBuilder();
		for (String td : fields) {
			String[] fieldStyle=TagUtil.fieldStyleSplit(td);
			tds.append("<td"+fieldStyle[1]+">");
			if (fieldStyle[0].equals("INDEX")) {
				fieldStyle[0]=String.valueOf(index);
			} else {
				fieldStyle[0]=TagUtil.fieldReplace(fieldStyle[0],object,"no data");
			}
			tds.append(fieldStyle[0]);
			tds.append("</td>");
		}
		return tds.toString();
	}

	public static void main(String[] arg) {
//		String REGEX = "\\$\\{[\\w.]*\\}";
		
//		String htmlString = "id=${ab.ad.b}&name=${name}";
		String REGEX="FILTER\\[[\\S]*].";
		String htmlString = "FILTER[user.common?action=modify]...";
		htmlString = htmlString.replaceAll(REGEX, "");
		System.out.println(htmlString);
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

	/**
	 * @return the wrapSize
	 */
	public int getWrapSize() {
		return wrapSize;
	}

	/**
	 * @param wrapSize the wrapSize to set
	 */
	public void setWrapSize(int wrapSize) {
		this.wrapSize = wrapSize;
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

	/**
	 * @return the paginationValue
	 */
	public String getPaginationValue() {
		return paginationValue;
	}

	/**
	 * @param paginationValue the paginationValue to set
	 */
	public void setPaginationValue(String paginationValue) {
		this.paginationValue = paginationValue;
	}

	/**
	 * @return the paginationScope
	 */
	public String getPaginationScope() {
		return paginationScope;
	}

	/**
	 * @param paginationScope the paginationScope to set
	 */
	public void setPaginationScope(String paginationScope) {
		this.paginationScope = paginationScope;
	}

}
