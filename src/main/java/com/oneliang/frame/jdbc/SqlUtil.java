package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.exception.MappingNotFoundException;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.json.JsonUtil;

/**
 * sql util for generate the common sql such as:select,insert,delete,update
 * @author Dandelion
 * @since 2008-09-25
 */
public final class SqlUtil{

	private SqlUtil(){}

	/**
	 * <p>
	 * Method: for database use,ResultSet to object list
	 * </p>
	 * @param <T>
	 * @param resultSet
	 * @param clazz
	 * @param mappingBean
	 * @return List<T>
	 */
	public static <T extends Object> List<T> resultSetToObjectList(ResultSet resultSet, Class<T> clazz,MappingBean mappingBean,SqlProcessor sqlProcessor){
		List<T> list = null;
		if(mappingBean!=null&&resultSet!=null){
			try{
				list = new ArrayList<T>();
				T object = null;
	//			Field[] field = clazz.getDeclaredFields();// get the fields one
				Method[] methods=clazz.getMethods();
				// time is ok
				while (resultSet.next()) {
					object = clazz.newInstance();// more instance
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=null;
						if(methodName.startsWith(Constants.Method.PREFIX_SET)){
							fieldName=ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName);
						}
						if(fieldName!=null){
							String columnName = mappingBean.getColumn(fieldName);
							if(columnName!=null){
								Class<?>[] classes=method.getParameterTypes();
								Object value=null;
								if(classes.length==1){
									if(sqlProcessor!=null){
										value=sqlProcessor.afterSelectProcess(classes[0], resultSet, columnName);
									}
									method.invoke(object, value);
								}
							}
						}
					}
					list.add(object);
				}
			}catch (Exception e) {
				throw new SqlUtilException(e);
			}
		}else{
			throw new MappingNotFoundException("Can not find the object mapping:"+clazz);
		}
		return list;
	}
	
	/**
	 * <p>Method: the simple select sql</p>
	 * @param columns can be null
	 * @param table can not be null
	 * @param condition can be null
	 * @return String
	 */
	public static String selectSql(String[] columns,String table,String condition){
		StringBuilder selectColumn=new StringBuilder();
		if(columns!=null&&columns.length>0){
			for(String column:columns){
				selectColumn.append(column+Constants.Symbol.COMMA);
			}
			selectColumn=selectColumn.delete(selectColumn.length()-1,selectColumn.length());
		}else{
			selectColumn.append(Constants.Symbol.WILDCARD);
		}
		condition=StringUtil.nullToBlank(condition);
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT ");
		sql.append(selectColumn);
		sql.append(" FROM ");
		sql.append(table);
		sql.append(" WHERE 1=1 ");
		sql.append(condition);
		return sql.toString();
	}

	/**
	 * <p>Method: class to select sql</p>
	 * @param <T>
	 * @param columns String[] which columns do you select
	 * @param table
	 * @param condition
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String selectSql(String[] columns,String table,String condition,MappingBean mappingBean){
		String sql = null;
		if (StringUtil.isNotBlank(table)||mappingBean!=null) {
			if(StringUtil.isBlank(table)){
				table=mappingBean.getTable();
			}
			sql = selectSql(columns,table,condition);
		} else {
			throw new MappingNotFoundException("Can not find the object mapping or table,object mapping or table can not be null or empty string!");
		}
		return sql;
	}

	/**
	 * delete sql
	 * @param table can not be null
	 * @param condition
	 * @return String
	 */
	public static String deleteSql(String table,String condition){
		condition=StringUtil.nullToBlank(condition);
		StringBuilder sql=new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(table);
		sql.append(" WHERE 1=1 ");
		sql.append(condition);
		return sql.toString();
	}

	/**
	 * delete sql
	 * @param table
	 * @param condition
	 * @param mappingBean
	 * @return String
	 */
	public static String deleteSql(String table,String condition,MappingBean mappingBean){
		String sql = null;
		if (StringUtil.isNotBlank(table)||mappingBean!=null) {
			if(StringUtil.isBlank(table)){
				table=mappingBean.getTable();
			}
			sql = deleteSql(table,condition);
		} else {
			throw new MappingNotFoundException("Can not find the object or table,object or table can not be null or empty string!");
		}
		return sql;
	}

	/**
	 * <p>Method: class to select sql with id</p>
	 * @param <T>
	 * @param clazz
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String classToSelectIdSql(Class<T> clazz,MappingBean mappingBean){
		String sql = null;
		if (clazz != null) {
			if(mappingBean!=null){
				Method[] methods=clazz.getMethods();
				StringBuilder condition = new StringBuilder();
				String table=null;
				for (Method method:methods) {
					String methodName=method.getName();
					String fieldName=ObjectUtil.methodNameToFieldName(methodName);
					if(fieldName!=null){
						boolean isId=mappingBean.isId(fieldName);
						String columnName = mappingBean.getColumn(fieldName);
						if(columnName!=null){
							if (isId) {
								condition.append(" AND " + columnName + "=?");
							}
						}
					}
				}
				table=mappingBean.getTable();
				sql = selectSql(null, table, condition.toString());
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+clazz);
			}
		} else {
			throw new NullPointerException("Class can not be null");
		}
		return sql;
	}

	/**
	 * <p>
	 * Method: for database use make the delete sql string,can delete one row
	 * not the many rows,support single id
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String classToDeleteOneRowSql(Class<T> clazz,Serializable id,MappingBean mappingBean){
		return classToDeleteSql(clazz,new Serializable[]{id},mappingBean,DeleteType.ONE_ROW);
	}

	/**
	 *  <p>
	 * Method: for database use make the delete sql string,can delete multiple row
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @param ids
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String classToDeleteMultipleRowSql(Class<T> clazz,Serializable[] ids,MappingBean mappingBean){
		return classToDeleteSql(clazz,ids,mappingBean,DeleteType.MULTIPLE_ROW);
	}

	/**
	 * <p>
	 * Method: for database use make the delete sql string,can delete one row and multi row
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @param ids
	 * @param mappingBean
	 * @param deleteType
	 * @return String
	 */
	private static <T extends Object> String classToDeleteSql(Class<T> clazz,Serializable[] ids,MappingBean mappingBean,DeleteType deleteType){
		String sql = null;
		if (clazz != null) {
			if(mappingBean!=null){
				Method[] methods=clazz.getMethods();
				StringBuilder condition = new StringBuilder();
				String table=null;
				for (Method method:methods) {
					String methodName=method.getName();
					String fieldName=ObjectUtil.methodNameToFieldName(methodName);
					if(fieldName!=null){
						boolean isId=mappingBean.isId(fieldName);
						String columnName=mappingBean.getColumn(fieldName);
						if(columnName!=null){
							if (isId) {
								if(ids==null||ids.length==0){
									throw new NullPointerException("ids can not be null or empty.");
								}
								switch(deleteType){
								case ONE_ROW:
									condition.append(" AND "+columnName+"='"+ids[0]+"'");
									break;
								case MULTIPLE_ROW:
									String id=JsonUtil.baseArrayToJson(ids);
									id=id.replaceAll("^\\"+Constants.Symbol.MIDDLE_BRACKET_LEFT, "").replaceAll("\\"+Constants.Symbol.MIDDLE_BRACKET_RIGHT+"$", "");
									condition.append(" AND "+columnName+" IN ("+id+")");
									break;
								}
							}
						}
					}
				}
				table=mappingBean.getTable();
				sql=deleteSql(table, condition.toString());
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+clazz);
			}
		} else {
			throw new NullPointerException("Class can not be null");
		}
		return sql;
	}
	
	/**
	 * <p>
	 * Method: object to select sql
	 * </p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String objectToSelectSql(T object, String table, MappingBean mappingBean){
		String sql = null;
		if (object != null) {
			if(mappingBean!=null){
				try{
					Method[] methods=object.getClass().getMethods();
					StringBuilder condition=new StringBuilder();
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=ObjectUtil.methodNameToFieldName(methodName);
						if(fieldName!=null){
							boolean isId=mappingBean.isId(fieldName);
							String columnName = mappingBean.getColumn(fieldName);
							if(columnName!=null){
								if (isId) {
									Object value = method.invoke(object,new Object[]{});
									condition.append(" AND "+columnName+"="+"'"+value+"'");
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					sql = selectSql(null, table, condition.toString());
				}catch (Exception e) {
					throw new SqlUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object,object can not be null!");
		}
		return sql;
	}
	
	/**
	 * <p>
	 * Method: for database use,make the update sql string
	 * </p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param mappingBean
	 * @param sqlProcessor
	 * @return String
	 */
	public static <T extends Object> String objectToInsertSql(T object, String table,MappingBean mappingBean,SqlProcessor sqlProcessor){
		String sql = null;
		if (object!=null) {
			if(mappingBean!=null){
				try{
					Method[] methods=object.getClass().getMethods();
					StringBuilder columnNames = new StringBuilder();
					StringBuilder values = new StringBuilder();
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=ObjectUtil.methodNameToFieldName(methodName);
						if(fieldName!=null){
							String columnName=mappingBean.getColumn(fieldName);
							if (columnName!=null) {
								columnNames.append(columnName+Constants.Symbol.COMMA);
								Class<?> type=method.getReturnType();
								Object value = method.invoke(object,new Object[]{});
								if(sqlProcessor!=null){
									values.append(sqlProcessor.beforeInsertProcess(type, value)+Constants.Symbol.COMMA);
								}else{
									if(value!=null){
										values.append("'"+value.toString()+"'");
										values.append(Constants.Symbol.COMMA);
									}else{
										values.append(StringUtil.NULL+Constants.Symbol.COMMA);
									}
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					StringBuilder stringBuilder=new StringBuilder();
					stringBuilder.append("INSERT INTO ");
					stringBuilder.append(table);
					stringBuilder.append("("+ columnNames.substring(0, columnNames.length() - 1)+")");
					stringBuilder.append(" VALUES (" + values.substring(0, values.length() - 1)+")");
					sql=stringBuilder.toString();
				}catch (Exception e) {
					throw new SqlUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object,object can not be null!");
		}
		return sql;
	}

	/**
	 * <p>
	 * Method: for database use,make the update sql string
	 * </p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param otherCondition
	 * @param byId
	 * @param mappingBean
	 * @param sqlProcessor
	 * @return String
	 */
	public static <T extends Object> String objectToUpdateSql(T object, String table,String otherCondition,boolean byId,MappingBean mappingBean,SqlProcessor sqlProcessor){
		String sql = null;
		if (object != null) {
			if(mappingBean!=null){
				try{
					Method[] methods=object.getClass().getMethods();
					StringBuilder columnsAndValues = new StringBuilder();
					StringBuilder condition = new StringBuilder();
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=ObjectUtil.methodNameToFieldName(methodName);
						if(fieldName!=null){
							String columnName = mappingBean.getColumn(fieldName);
							if(columnName!=null){
								boolean isId=mappingBean.isId(fieldName);
								Class<?> type=method.getReturnType();
								Object value = method.invoke(object,new Object[]{});
								String result=null;
								if(sqlProcessor!=null){
									result=sqlProcessor.beforeUpdateProcess(type, isId, columnName, value);
								}else{
									if(value!=null){
										result=columnName+"='"+value+"',";
									}
								}
								result=StringUtil.nullToBlank(result);
								if(isId){
									if(byId){
										condition.append(result);
									}
								}else{
									columnsAndValues.append(result);
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					otherCondition=StringUtil.nullToBlank(otherCondition);
					StringBuilder stringBuilder=new StringBuilder();
					stringBuilder.append("UPDATE ");
					stringBuilder.append(table);
					stringBuilder.append(" SET "+columnsAndValues.substring(0, columnsAndValues.length() - 1));
					stringBuilder.append(" WHERE 1=1 " + condition+" "+otherCondition);
					sql=stringBuilder.toString();
				}catch (Exception e) {
					throw new SqlUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object or table,object or table can not be null or empty string!");
		}
		return sql;
	}
	
	/**
	 * <p>
	 * Method: for database use make the delete sql string,can delete one row
	 * not the many rows
	 * </p>
	 * 
	 * @param <T>
	 * @param object
	 * @param table
	 * @param otherCondition
	 * @param mappingBean
	 * @return String
	 */
	public static <T extends Object> String objectToDeleteSql(T object, String table,String otherCondition,boolean byId,MappingBean mappingBean,SqlProcessor sqlProcessor){
		String sql = null;
		if (object != null) {
			if(mappingBean!=null){
				try{
					Method[] methods=object.getClass().getMethods();
					StringBuilder condition = new StringBuilder();
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=ObjectUtil.methodNameToFieldName(methodName);
						if(fieldName!=null){
							boolean isId=mappingBean.isId(fieldName);
							String columnName = mappingBean.getColumn(fieldName);
							if(columnName!=null){
								if((byId&&isId)||(!byId&&!isId)){
									Class<?> type=method.getReturnType();
									Object value = method.invoke(object, new Object[]{});
									if(sqlProcessor!=null){
										String result=sqlProcessor.beforeDeleteProcess(type, isId, columnName, value);
										condition.append(result);
									}else{
										if(value!=null){
											condition.append(" AND "+columnName+"='"+value+"'");
										}
									}
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					otherCondition=StringUtil.nullToBlank(otherCondition);
					sql=deleteSql(table, condition.toString()+" "+otherCondition);
				}catch (Exception e) {
					throw new SqlUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object,object can not be null!");
		}
		return sql;
	}

	/**
	 * create table sqls,include drop table
	 * @param annotationMappingBean
	 * @return String[]
	 */
	public static String[] createTableSqls(AnnotationMappingBean annotationMappingBean){
		List<String> sqlList=new ArrayList<String>();
		if(annotationMappingBean!=null){
			String table=annotationMappingBean.getTable();
			if(annotationMappingBean.isDropIfExist()){
				sqlList.add("DROP TABLE IF EXISTS "+table+Constants.Symbol.SEMICOLON);
			}
			StringBuilder createTableSql=new StringBuilder();
			createTableSql.append("CREATE TABLE "+table+" (");
			List<MappingColumnBean> mappingColumnBeanList=annotationMappingBean.getMappingColumnBeanList();
			if(mappingColumnBeanList!=null&&!mappingColumnBeanList.isEmpty()){
				for(MappingColumnBean mappingColumnBean:mappingColumnBeanList){
					if(mappingColumnBean instanceof AnnotationMappingColumnBean){
						AnnotationMappingColumnBean annotationMappingColumnBean=(AnnotationMappingColumnBean)mappingColumnBean;
						createTableSql.append(annotationMappingColumnBean.getColumn());
						createTableSql.append(" "+annotationMappingColumnBean.getCondition()+Constants.Symbol.COMMA);
						if(annotationMappingColumnBean.getIsId()){
							createTableSql.append("PRIMARY KEY ("+annotationMappingColumnBean.getColumn()+")");
							createTableSql.append(Constants.Symbol.COMMA);
						}
					}
				}
				createTableSql.delete(createTableSql.length()-1, createTableSql.length());
			}
			createTableSql.append(") "+annotationMappingBean.getCondition());
			createTableSql.append(Constants.Symbol.SEMICOLON);
			sqlList.add(createTableSql.toString());
		}
		String[] sqls=sqlList.toArray(new String[sqlList.size()]);
		return sqls;
	}

	public static class SqlUtilException extends RuntimeException{
		private static final long serialVersionUID = 6159006798091203909L;
		public SqlUtilException(Throwable cause) {
			super(cause);
		}
	}

	public static abstract interface SqlProcessor {
		
		/**
		 * statement process,for statement use
		 * @param preparedStatement
		 * @param parameter
		 */
		public abstract void statementProcess(PreparedStatement preparedStatement,int index,Object parameter);
		
		/**
		 * after select process,for result set to object
		 * @param parameterType
		 * @param resultSet
		 * @param columnName
		 * @return Object
		 */
		public abstract Object afterSelectProcess(Class<?> parameterType,ResultSet resultSet,String columnName);
		
		/**
		 * before insert process,for generate insert sql
		 * @param <T>
		 * @param clazz
		 * @param value
		 * @return String
		 */
		public abstract <T extends Object> String beforeInsertProcess(final Class<T> clazz,final Object value);
		
		/**
		 * before update process,for generate update sql
		 * @param <T>
		 * @param clazz
		 * @param isId
		 * @param columnName
		 * @param value
		 * @return String
		 */
		public abstract <T extends Object> String beforeUpdateProcess(final Class<T> clazz,final boolean isId,final String columnName,final Object value);

		/**
		 * before delete process,for generate delete sql
		 * @param <T>
		 * @param clazz
		 * @param isId
		 * @param columnName
		 * @param value
		 * @return String
		 */
		public abstract <T extends Object> String beforeDeleteProcess(final Class<T> clazz,final boolean isId,final String columnName,final Object value);
	}

	private static enum DeleteType{
	    ONE_ROW,MULTIPLE_ROW
	}
}