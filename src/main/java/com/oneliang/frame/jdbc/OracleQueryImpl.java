package com.oneliang.frame.jdbc;

import java.sql.Connection;
import java.util.List;

import com.oneliang.Constant;
import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.frame.bean.Page;
import com.oneliang.util.common.StringUtil;

public class OracleQueryImpl extends DefaultQueryImpl {

	/**
	 * <p>Method: select object pagination list,has implement,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz, Page page, String[] selectColumns, String table, String condition, Object[] parameters) throws QueryException {
		int totalRows=this.totalRows(clazz,table,condition,parameters);
		int rowsPerPage=page.getRowsPerPage();
		page.initialize(totalRows, rowsPerPage);
		int startRow=page.getPageFirstRow();
		int currentPage=page.getPage();
		//generate table string
		final String rowNumAlias="rn";
		final String tableAlias="t";
		if (selectColumns==null||selectColumns.length==0) {
			selectColumns=new String[]{tableAlias+Constant.Symbol.DOT+Constant.Symbol.WILDCARD};
		}
		String[] newColumns=new String[selectColumns.length+1];
		System.arraycopy(selectColumns, 0, newColumns, 0, selectColumns.length);
		newColumns[selectColumns.length]="rownum "+rowNumAlias;
		if(StringUtil.isBlank(table)){
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			table=mappingBean.getTable();
		}
		table=table+" "+tableAlias;
		table=SqlUtil.selectSql(newColumns, table, condition);
		table=Constant.Symbol.BRACKET_LEFT+table+Constant.Symbol.BRACKET_RIGHT;
		//generate outer conditions
		StringBuilder sqlConditions=new StringBuilder();
		sqlConditions.append("and "+rowNumAlias+">"+startRow+" and "+rowNumAlias+"<="+rowsPerPage*currentPage);
		List<T> list=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			list=this.executeQuery(connection,clazz,null,table, sqlConditions.toString(), parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return list;
	}
}
