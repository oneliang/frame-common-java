package com.oneliang.frame.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import com.oneliang.frame.jdbc.SqlUtil.SqlProcessor;
import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.ClassUtil.ClassType;

public abstract class AbstractSqlProcessor implements SqlProcessor {

	public void statementProcess(PreparedStatement statement,int index,Object parameter){
		try{
			if(parameter!=null){
				Class<?> parameterClass=null;
				parameterClass=parameter.getClass();
				ClassType classType=ClassUtil.getClassType(parameterClass);
				if(classType!=null){
					String value=parameter.toString();
					switch(classType){
					case CHAR:
					case JAVA_LANG_STRING:
					case JAVA_LANG_CHARACTER:
						statement.setString(index, value);
						break;
					case BYTE:
					case JAVA_LANG_BYTE:
						statement.setByte(index, Byte.parseByte(value));
						break;
					case SHORT:
					case JAVA_LANG_SHORT:
						statement.setShort(index, Short.parseShort(value));
						break;
					case INT:
					case JAVA_LANG_INTEGER:
						statement.setInt(index, Integer.parseInt(value));
						break;
					case LONG:
					case JAVA_LANG_LONG:
						statement.setLong(index, Long.parseLong(value));
						break;
					case FLOAT:
					case JAVA_LANG_FLOAT:
						statement.setFloat(index, Float.parseFloat(value));
						break;
					case DOUBLE:
					case JAVA_LANG_DOUBLE:
						statement.setDouble(index, Double.parseDouble(value));
						break;
					case BOOLEAN:
					case JAVA_LANG_BOOLEAN:
						statement.setBoolean(index, Boolean.parseBoolean(value));
						break;
					case JAVA_UTIL_DATE:
						statement.setTimestamp(index, new Timestamp(((Date)parameter).getTime()));
						break;
					default:
						statement.setObject(index, parameter);
					}
				}else{
					statement.setObject(index, parameter);
				}
			}else{
				statement.setObject(index, null);
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * after select process,for result set to object
	 * @param parameterType
	 * @param resultSet
	 * @param columnName
	 * @return Object
	 */
	public Object afterSelectProcess(Class<?> parameterType, ResultSet resultSet,String columnName){
		Object value=null;
		try{
			ClassType classType=ClassUtil.getClassType(parameterType);
			switch(classType){
			case CHAR:
				value = resultSet.getString(columnName).toCharArray()[0];
				break;
			case JAVA_LANG_STRING:
				value = resultSet.getString(columnName);
				break;
			case JAVA_LANG_CHARACTER:
				value = Character.valueOf(resultSet.getString(columnName).toCharArray()[0]);
				break;
			case BYTE:
				value = resultSet.getByte(columnName);
				break;
			case JAVA_LANG_BYTE:
				value = Byte.valueOf(resultSet.getByte(columnName));
				break;
			case SHORT:
				value = resultSet.getShort(columnName);
				break;
			case JAVA_LANG_SHORT:
				value = Short.valueOf(resultSet.getShort(columnName));
				break;
			case INT:
				value = resultSet.getInt(columnName);
				break;
			case JAVA_LANG_INTEGER:
				value = Integer.valueOf(resultSet.getInt(columnName));
				break;
			case LONG:
				value = resultSet.getLong(columnName);
				break;
			case JAVA_LANG_LONG:
				value = Long.valueOf(resultSet.getLong(columnName));
				break;
			case FLOAT:
				value = resultSet.getFloat(columnName);
				break;
			case JAVA_LANG_FLOAT:
				value = Float.valueOf(resultSet.getFloat(columnName));
				break;
			case DOUBLE:
				value = resultSet.getDouble(columnName);
				break;
			case JAVA_LANG_DOUBLE:
				value = Double.valueOf(resultSet.getDouble(columnName));
				break;
			case BOOLEAN:
				value = resultSet.getBoolean(columnName);
				break;
			case JAVA_LANG_BOOLEAN:
				value = Boolean.valueOf(resultSet.getBoolean(columnName));
				break;
			case JAVA_UTIL_DATE:
				value = resultSet.getTimestamp(columnName);
				if(value!=null){
					value=new Date(((Timestamp)value).getTime());
				}
				break;
			default:
				break;
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return value;
	}
}
