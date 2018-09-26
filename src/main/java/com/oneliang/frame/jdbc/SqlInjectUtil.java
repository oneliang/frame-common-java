package com.oneliang.frame.jdbc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.exception.MappingNotFoundException;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;

/**
 * sql util for generate the common sql such as:select,insert,delete,update
 * @author Dandelion
 * @since 2008-09-25
 */
public final class SqlInjectUtil{

	private SqlInjectUtil(){}

	/**
	 * <p>
	 * Method: for database use,make the insert sql string
	 * </p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param mappingBean
	 * @param parameterList
	 * @return String
	 */
	public static <T extends Object> String objectToInsertSql(T object, String table,MappingBean mappingBean,List<Object> parameterList){
		StringBuilder sql = new StringBuilder();
		if (object != null) {
			if(mappingBean!=null){
				try{
					Method[] methods=object.getClass().getMethods();
					StringBuilder columnNameStringBuilder = new StringBuilder();
					StringBuilder values = new StringBuilder();
					for (Method method:methods) {
						String methodName=method.getName();
						String fieldName=ObjectUtil.methodNameToFieldName(methodName);
						if(fieldName!=null){
							String columnName=mappingBean.getColumn(fieldName);
							if (columnName!=null) {
								columnNameStringBuilder.append(columnName+Constants.Symbol.COMMA);
								Object value = method.invoke(object,new Object[]{});
								values.append(Constants.Symbol.QUESTION_MARK+Constants.Symbol.COMMA);
								if(parameterList!=null){
									parameterList.add(value);
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					sql.append("INSERT INTO ");
					sql.append(table);
					sql.append("("+ columnNameStringBuilder.substring(0, columnNameStringBuilder.length() - 1)+")");
					sql.append(" VALUES (" + values.substring(0, values.length() - 1)+")");
				}catch(Exception e){
					throw new SqlInjectUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object,object can not be null!");
		}
		return sql.toString();
	}

	/**
	 * <p>
	 * Method: for database use,make the insert sql string
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param mappingBean
	 * @param fieldList
	 * @return String
	 */
	public static <T extends Object> String classToInsertSql(Class<T> clazz, String table,MappingBean mappingBean,List<String> fieldNameList) {
		StringBuilder sql = new StringBuilder();
		if (clazz != null) {
			if(mappingBean!=null){
				Method[] methods=clazz.getMethods();
				StringBuilder columnNameStringBuilder = new StringBuilder();
				StringBuilder valueStringBuilder = new StringBuilder();
				for (Method method:methods) {
					String methodName=method.getName();
					String fieldName=ObjectUtil.methodNameToFieldName(methodName);
					if(fieldName!=null){
						String columnName=mappingBean.getColumn(fieldName);
						if (columnName!=null) {
							columnNameStringBuilder.append(columnName+Constants.Symbol.COMMA);
							valueStringBuilder.append(Constants.Symbol.QUESTION_MARK+Constants.Symbol.COMMA);
							if(fieldNameList!=null){
								fieldNameList.add(fieldName);
							}
						}
					}
				}
				if(StringUtil.isBlank(table)){
					table=mappingBean.getTable();
				}
				sql.append("INSERT INTO ");
				sql.append(table);
				sql.append("("+ columnNameStringBuilder.substring(0, columnNameStringBuilder.length() - 1)+")");
				sql.append(" VALUES (" + valueStringBuilder.substring(0, valueStringBuilder.length() - 1)+")");
			}else{
				throw new MappingNotFoundException("Can not find the class mapping:"+clazz);
			}
		} else {
			throw new NullPointerException("Class can not be null!");
		}
		return sql.toString();
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
	 * @param parameterList
	 * @return String
	 */
	public static <T extends Object> String objectToUpdateSql(T object, String table,String otherCondition,boolean byId,MappingBean mappingBean,List<Object> parameterList) {
		StringBuilder sql = new StringBuilder();
		List<Object> idList=new ArrayList<Object>();
		List<Object> valueList=new ArrayList<Object>();
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
								Object value = method.invoke(object,new Object[]{});
								String result=null;
								if(byId&&isId){
									if(value!=null){
										result=" AND "+columnName+"=?";
										idList.add(value);
										condition.append(result);
									}else{
										result=" AND "+columnName+" is "+StringUtil.NULL;
										condition.append(result);
									}
								}else{
									if(value!=null){
										result=columnName+"=?,";
										valueList.add(value);
										columnsAndValues.append(result);
									}
								}
							}
						}
					}
					if(StringUtil.isBlank(table)){
						table=mappingBean.getTable();
					}
					otherCondition=StringUtil.nullToBlank(otherCondition);
					sql.append("UPDATE ");
					sql.append(table);
					sql.append(" SET "+columnsAndValues.substring(0, columnsAndValues.length() - 1));
					sql.append(" WHERE 1=1 " + condition+" "+otherCondition);
					if(parameterList!=null){
						parameterList.addAll(valueList);
						parameterList.addAll(idList);
					}
				}catch(Exception e){
					throw new SqlInjectUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Can not find the object,object can not be null!");
		}
		return sql.toString();
	}

	/**
	 * <p>
	 * Method: for database use,make the update sql string
	 * </p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param otherCondition
	 * @param byId
	 * @param mappingBean
	 * @param fieldNameList
	 * @return String
	 */
	public static <T extends Object> String classToUpdateSql(Class<T> clazz, String table,String otherCondition,boolean byId,MappingBean mappingBean,List<String> fieldNameList) {
		StringBuilder sql = new StringBuilder();
		List<String> idList=new ArrayList<String>();
		List<String> valueList=new ArrayList<String>();
		if (clazz != null) {
			if(mappingBean!=null){
				Method[] methods=clazz.getMethods();
				StringBuilder columnsAndValues = new StringBuilder();
				StringBuilder condition = new StringBuilder();
				for (Method method:methods) {
					String methodName=method.getName();
					String fieldName=ObjectUtil.methodNameToFieldName(methodName);
					if(fieldName!=null){
						String columnName = mappingBean.getColumn(fieldName);
						if(columnName!=null){
							boolean isId=mappingBean.isId(fieldName);
							String result=null;
							if(byId&&isId){
								result=" AND "+columnName+"=?";
								idList.add(fieldName);
								condition.append(result);
							}else{
								result=columnName+"=?,";
								valueList.add(fieldName);
								columnsAndValues.append(result);
							}
						}
					}
				}
				if(StringUtil.isBlank(table)){
					table=mappingBean.getTable();
				}
				otherCondition=StringUtil.nullToBlank(otherCondition);
				sql.append("UPDATE ");
				sql.append(table);
				sql.append(" SET "+columnsAndValues.substring(0, columnsAndValues.length() - 1));
				sql.append(" WHERE 1=1 " + condition+" "+otherCondition);
				if(fieldNameList!=null){
					fieldNameList.addAll(valueList);
					fieldNameList.addAll(idList);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+clazz);
			}
		} else {
			throw new NullPointerException("Class can not be null");
		}
		return sql.toString();
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
	 * @param byId
	 * @param mappingBean
	 * @param parameterList
	 * @return String
	 */
	public static <T extends Object> String objectToDeleteSql(T object, String table, String otherCondition, boolean byId, MappingBean mappingBean, List<Object> parameterList) {
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
									Object value = method.invoke(object, new Object[]{});
									if(value!=null){
										condition.append(" AND "+columnName+"=?");
										if(parameterList!=null){
											parameterList.add(value);
										}
									}else{
										if((byId&&isId)){
											condition.append(" AND "+columnName+" is "+StringUtil.NULL);
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
					sql = SqlUtil.deleteSql(table, condition.toString()+" "+otherCondition);
				} catch (Exception e) {
					throw new SqlInjectUtilException(e);
				}
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+object.getClass());
			}
		} else {
			throw new NullPointerException("Object can not be null!");
		}
		return sql;
	}

	/**
	 * <p>Method: for database use make the delete sql string,sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param otherCondition
	 * @param byId
	 * @param mappingBean
	 * @param fieldNameList
	 * @return String
	 */
	public static <T extends Object> String classToDeleteSql(Class<T> clazz, String table, String otherCondition, boolean byId, MappingBean mappingBean, List<String> fieldNameList) {
		String sql = null;
		if (clazz != null) {
			if(mappingBean!=null){
				Method[] methods=clazz.getMethods();
				StringBuilder condition = new StringBuilder();
				for (Method method:methods) {
					String methodName=method.getName();
					String fieldName=ObjectUtil.methodNameToFieldName(methodName);
					if(fieldName!=null){
						boolean isId=mappingBean.isId(fieldName);
						String columnName = mappingBean.getColumn(fieldName);
						if(columnName!=null){
							if((byId&&isId)||(!byId&&!isId)){
								condition.append(" AND "+columnName+"=?");
								if(fieldNameList!=null){
									fieldNameList.add(fieldName);
								}
							}
						}
					}
				}
				if(StringUtil.isBlank(table)){
					table=mappingBean.getTable();
				}
				otherCondition=StringUtil.nullToBlank(otherCondition);
				sql = SqlUtil.deleteSql(table, condition.toString()+" "+otherCondition);
			}else{
				throw new MappingNotFoundException("Can not find the object mapping:"+clazz);
			}
		} else {
			throw new NullPointerException("Class can not be null!");
		}
		return sql;
	}

	public static class SqlInjectUtilException extends RuntimeException{
		private static final long serialVersionUID = 8992546537569042057L;
		public SqlInjectUtilException(Throwable cause) {
			super(cause);
		}
	}
}
