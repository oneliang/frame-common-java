package com.oneliang.frame.servlet.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.oneliang.Constant;
import com.oneliang.frame.bean.Page;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class PaginationTag extends BodyTagSupport {

	/**
	 * serialVersiionUID
	 */
	private static final long serialVersionUID = -7387028073417293099L;

	private static final Logger logger=LoggerManager.getLogger(PaginationTag.class);

	private static final String BLANK_2="&nbsp;&nbsp;";
	private static final String BLANK_8=BLANK_2+BLANK_2+BLANK_2+BLANK_2;
	private static final String TIPS_PAGE="Page:";
	private static final String TIPS_ROWS="Rows:";
	
	private String action=null;
	private String value=null;
	private String scope=null;
	private int size=1;
	private String firstIcon="first";
	private String lastIcon="last";
	private String previousIcon="previous";
	private String nextIcon="next";
	private String linkString=null;
	
	public int doStartTag() throws JspException{
		this.action=StringUtil.nullToBlank(this.action);
		this.scope=StringUtil.nullToBlank(this.value);
		this.value=StringUtil.nullToBlank(this.scope);
		this.firstIcon=StringUtil.nullToBlank(this.firstIcon);
		this.lastIcon=StringUtil.nullToBlank(this.lastIcon);
		this.previousIcon=StringUtil.nullToBlank(this.previousIcon);
		this.nextIcon=StringUtil.nullToBlank(this.nextIcon);
		this.linkString=StringUtil.nullToBlank(this.linkString);
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException{
		Object object=null;
		if(this.scope.equals(Constant.RequestScope.SESSION)){
			object=this.pageContext.getSession().getAttribute(value);
		}else{
			object=this.pageContext.getRequest().getAttribute(value);
		}
		if(object instanceof Page){
			Page page=(Page)object;
			StringBuilder paginationHtml=new StringBuilder();
			String action=null;
			if(this.action.indexOf("?")>-1){
				action=this.action+"&page=";
			}else{
				action=this.action+"?page=";
			}
			//first and previous
			String first="<a href=\""+action+page.getFirstPage()+"\" "+linkString+">"+this.firstIcon+"</a>"+BLANK_2;
			String previous="<a href=\""+action+(page.getPage()-1)+"\" "+linkString+">"+this.previousIcon+"</a>"+BLANK_2;
			paginationHtml.append(first);
			paginationHtml.append(previous);
			//middle
			if(this.size>page.getTotalPages()){
				this.size=page.getTotalPages();
			}
			int middlePosition=0;
			if(this.size%2==0){//even
				middlePosition=this.size/2;
			}else{//odd
				middlePosition=this.size/2+1;
			}
			int startPage=0;
			if(page.getPage()<=middlePosition){
				startPage=1;
			}else if(page.getPage()>middlePosition){
				if(page.getPage()>(page.getTotalPages()-middlePosition)){
					startPage=page.getTotalPages()-this.size+1;
				}else{
					startPage=page.getPage()-middlePosition+1;
				}
			}
			for(int i=0;i<this.size;i++){
				int showPage=startPage+i;
				String middle=null;
				if(showPage==page.getPage()){
					middle="<a href=\""+action+showPage+"\" "+linkString+"><font color=\"red\">["+BLANK_2+showPage+BLANK_2+"]</font></a>"+BLANK_2;
				}else{
					middle="<a href=\""+action+showPage+"\" "+linkString+">["+BLANK_2+showPage+BLANK_2+"]</a>"+BLANK_2;
				}
				paginationHtml.append(middle);
			}
			//next and last(total)
			String next="<a href=\""+action+(page.getPage()+1)+"\" "+linkString+">"+this.nextIcon+"</a>"+BLANK_2;
			String last="<a href=\""+action+page.getTotalPages()+"\" "+linkString+">"+this.lastIcon+"</a>";
			paginationHtml.append(next);
			paginationHtml.append(last);
			String other=BLANK_8+TIPS_PAGE+page.getPage()+"/"+page.getTotalPages()+BLANK_8+TIPS_ROWS+(page.getPageFirstRow()+1)+"~"+(page.getPage()*page.getRowsPerPage()<page.getTotalRows()?page.getPage()*page.getRowsPerPage():page.getTotalRows())+"/"+page.getTotalRows();
			paginationHtml.append(other);
			//goto page
			try {
				this.pageContext.getOut().println(paginationHtml.toString());
			} catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
		return EVAL_PAGE;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the firstIcon
	 */
	public String getFirstIcon() {
		return firstIcon;
	}

	/**
	 * @param firstIcon the firstIcon to set
	 */
	public void setFirstIcon(String firstIcon) {
		this.firstIcon = firstIcon;
	}

	/**
	 * @return the lastIcon
	 */
	public String getLastIcon() {
		return lastIcon;
	}

	/**
	 * @param lastIcon the lastIcon to set
	 */
	public void setLastIcon(String lastIcon) {
		this.lastIcon = lastIcon;
	}

	/**
	 * @return the previousIcon
	 */
	public String getPreviousIcon() {
		return previousIcon;
	}

	/**
	 * @param previousIcon the previousIcon to set
	 */
	public void setPreviousIcon(String previousIcon) {
		this.previousIcon = previousIcon;
	}

	/**
	 * @return the nextIcon
	 */
	public String getNextIcon() {
		return nextIcon;
	}

	/**
	 * @param nextIcon the nextIcon to set
	 */
	public void setNextIcon(String nextIcon) {
		this.nextIcon = nextIcon;
	}

	/**
	 * @return the linkString
	 */
	public String getLinkString() {
		return linkString;
	}

	/**
	 * @param linkString the linkString to set
	 */
	public void setLinkString(String linkString) {
		this.linkString = linkString;
	}
}
