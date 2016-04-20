package com.oneliang.frame.jdbc;

import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TimeUtil;


/**
 * mostly for mysql database
 * @author Dandelion
 * @since 2011-01-07
 */
public class DefaultSqlProcessor extends AbstractSqlProcessor{

	/**
	 * mostly for mysql database
	 * default sql insert processor
	 * promiss:if value is null return null,if it is not null return the new value
	 */
	public <T extends Object> String beforeInsertProcess(final Class<T> clazz, final Object value) {
		String result=null;
		if(value!=null){
			if(clazz.equals(boolean.class)||clazz.equals(Boolean.class)){
				result=value.toString();
			}else if (clazz.equals(java.util.Date.class)) {
				result="'"+TimeUtil.dateToString((java.util.Date)value)+"'";
			}else{
				result="'"+value.toString()+"'";
			}
		}else{
			result=StringUtil.NULL;
		}
		return result;
	}

	/**
	 * mostly for mysql database
	 * default sql update processor
	 * promiss:if value is null,return the blank,if it is not null return the new value
	 */
	public <T extends Object> String beforeUpdateProcess(final Class<T> clazz,final boolean isId,final String columnName,final Object value){
		String result=null;
		if(isId){
			result=" AND "+columnName+"='"+value+"'";
		}else{
			if(value!=null){
				if(clazz.equals(boolean.class)||clazz.equals(Boolean.class)){
					result=columnName+"="+value+",";
				}else if(clazz.equals(java.util.Date.class)){
					result=columnName+"='"+TimeUtil.dateToString((java.util.Date) value)+"',";
				}else{
					result=columnName+"='"+value+"',";
				}
			}else{
				result=StringUtil.BLANK;
			}
		}
		return result;
	}

	/**
	 * before delete process,for generate delete sql
	 * @param <T>
	 * @param clazz
	 * @param isId
	 * @param columnName
	 * @param value
	 * @return String
	 */
	public <T> String beforeDeleteProcess(Class<T> clazz, boolean isId, String columnName, Object value){
		String result=null;
		if(isId){
			result=" AND "+columnName+"='"+value+"'";
		}else{
			if(value!=null){
				if(clazz.equals(boolean.class)||clazz.equals(Boolean.class)){
					result=" AND "+columnName+"="+value;
				}else if(clazz.equals(java.util.Date.class)){
					result=" AND "+columnName+"='"+TimeUtil.dateToString((java.util.Date)value)+"'";
				}else{
					result=" AND "+columnName+"='"+value+"'";
				}
			}else{
				result=StringUtil.BLANK;
			}
		}
		return result;
	}
}
