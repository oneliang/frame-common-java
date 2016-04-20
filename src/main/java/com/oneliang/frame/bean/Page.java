package com.oneliang.frame.bean;

/**
 * @author Dandelion
 * @since 2008-11-10
 */
public class Page{

	public static final int DEFAULT_ROWS=20;
	private int page=1;//request parameter
	private int firstPage=1;//view use
	private int totalPages=1;//view use
	private int totalRows=0;//view use
	private int rowsPerPage=DEFAULT_ROWS;//view use
	private int start=0;//ext use

	public void initialize(int totalRows,int rowsPerPage){
		this.firstPage=1;
		this.totalRows=totalRows;
		this.rowsPerPage=rowsPerPage;
		int totalPagesCount=this.totalRows%this.rowsPerPage;
		if(totalPagesCount==0){
			this.totalPages=this.totalRows/this.rowsPerPage;
		}else{
			this.totalPages=this.totalRows/this.rowsPerPage+1;
		}
		
	}
	/**
	 * goto page
	 * @return page*sizePerPage
	 */
	public int getPageFirstRow(){
		if(this.page<1||this.page>this.totalPages){
			this.page=1;
		}
		return (this.page-1)*this.rowsPerPage;
	}
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the firstPage
	 */
	public int getFirstPage() {
		return firstPage;
	}
	/**
	 * @param firstPage the firstPage to set
	 */
	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}
	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}
	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	/**
	 * @return the rowsPerPage
	 */
	public int getRowsPerPage() {
		return rowsPerPage;
	}
	/**
	 * @param rowsPerPage the rowsPerPage to set
	 */
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

}
