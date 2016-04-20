package com.oneliang.frame.jdbc;

import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.TimeUtil;


public class OracleSqlProcessor extends AbstractSqlProcessor{

	/**
	 * before insert process,for generate insert sql
	 * @param <T>
	 * @param clazz
	 * @param value
	 * @return String
	 */
	public <T> String beforeInsertProcess(final Class<T> clazz, final Object value) {
		String result=null;
		if (clazz.equals(java.util.Date.class)) {
			if(value!=null){
				result="to_date('"+TimeUtil.dateToString((java.util.Date)value)+"','yyyy-mm-dd hh24:mi:ss')";
			}
		}else{
			if(value!=null){
				result="'"+value.toString()+"'";
			}
		}
		return result;
	}

	/**
	 * before update process,for generate update sql
	 * @param <T>
	 * @param clazz
	 * @param isId
	 * @param columnName
	 * @param value
	 * @return String
	 */
	public <T> String beforeUpdateProcess(final Class<T> clazz,final boolean isId,final String columnName,final Object value) {
		String result=null;
		if(isId){
			result="AND "+columnName+"='"+value+"'";
		}else{
			if(clazz.equals(java.util.Date.class)){
				if(value!=null){
					result=columnName+"=to_date('"+TimeUtil.dateToString((java.util.Date)value)+"','yyyy-mm-dd hh24:mi:ss'),";
				}else{
					result=StringUtil.BLANK;
				}
			}else{
				if(value!=null){
					result=columnName+"='"+value+"',";
				}else{
					result=StringUtil.BLANK;
				}
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
	public <T> String beforeDeleteProcess(Class<T> clazz, boolean isId, String columnName, Object value) {
		String result=null;
		if(isId){
			result="AND "+columnName+"='"+value+"'";
		}else{
			if(clazz.equals(java.util.Date.class)){
				if(value!=null){
					result=" AND "+columnName+"=to_date('"+TimeUtil.dateToString((java.util.Date)value)+"','yyyy-mm-dd hh24:mi:ss')";
				}else{
					result=StringUtil.BLANK;
				}
			}else{
				if(value!=null){
					result=" AND "+columnName+"='"+value+"'";
				}else{
					result=StringUtil.BLANK;
				}
			}
		}
		return result;
	}
}
