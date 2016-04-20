package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

/**
 * CoreQuery interface base on the connection.
 * @author Dandelion
 * @since 2009-08-12
 */
public abstract interface BaseQuery{

	/**
	 * <p>Method: execute by sql,for all sql</p>
	 * @param connection
	 * @param sql
	 * @throws QueryException
	 */
	public abstract void executeBySql(Connection connection,String sql) throws QueryException;

	/**
	 * <p>Method: execute by sql,for all sql</p>
	 * @param connection
	 * @param sql
	 * @param parameters
	 * @throws QueryException
	 */
	public abstract void executeBySql(Connection connection,String sql,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute query base on the connection and class</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz) throws QueryException;
	
	/**
	 * <p>Method: execute query base on connection and class and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String condition) throws QueryException;
	
	/**
	 * <p>Method: execute query base on connection and class and table and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String table,String condition) throws QueryException;
	
	/**
	 * <p>Through the class generate the sql</p>
	 * <p>Method: execute query base on connection and  class and selectColumns and table and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @return list<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String[] selectColumns,String table,String condition) throws QueryException;
	
	/**
	 * <p>Through the class generate the sql</p>
	 * <p>Method: execute query base on connection and  class and selectColumns and table and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return list<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String[] selectColumns,String table,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute query with id</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param id
	 * @return T
	 * @throws QueryException
	 */
	public abstract <T extends Object> T executeQueryById(Connection connection,Class<T> clazz,Serializable id) throws QueryException;

	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * @param connection
	 * @param clazz
	 * @param sql
	 * @return List
	 * @throws QueryException 
	 */
	public abstract <T extends Object> List<T> executeQueryBySql(Connection connection,Class<T> clazz,String sql) throws QueryException;
	
	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * @param connection
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return List
	 * @throws QueryException 
	 */
	public abstract <T extends Object> List<T> executeQueryBySql(Connection connection,Class<T> clazz,String sql,Object[] parameters) throws QueryException;
	
	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * <p>Caution: use this method must get Statement from the ResultSet and close it and close the ResultSet</p>
	 * @param connection
	 * @param sql
	 * @return ResultSet
	 * @throws QueryException
	 */
	public abstract ResultSet executeQueryBySql(Connection connection,String sql) throws QueryException;
	
	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * <p>Caution: use this method must get Statement from the ResultSet and close it and close the ResultSet</p> 
	 * @param connection
	 * @param sql
	 * @param parameters
	 * @return ResultSet
	 * @throws QueryException
	 */
	public abstract ResultSet executeQueryBySql(Connection connection,String sql,Object[] parameters) throws QueryException;
	
	/**
	 * <p>Method: execute insert</p>
	 * @param connection
	 * @param <T>
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeInsert(Connection connection,T object) throws QueryException;
	
	/**
	 * <p>Method: execute insert</p>
	 * @param connection
	 * @param <T>
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeInsert(Connection connection,T object,String table) throws QueryException;
	
	/**
	 * <p>Method: execute insert collection(list),transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeInsert(Connection connection,Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: execute insert collection(list),transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeInsert(Connection connection,Collection<T> collection,String table) throws QueryException;

	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param <T>
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeUpdate(Connection connection,T object) throws QueryException;

	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeUpdate(Connection connection,T object,String table) throws QueryException;

	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeUpdate(Connection connection,T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: execute update collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeUpdate(Connection connection,Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: execute update collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeUpdate(Connection connection,Collection<T> collection,String table) throws QueryException;

	/**
	 * <p>Method: execute delete by id</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param id
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDeleteById(Connection connection,Class<T> clazz,Serializable id) throws QueryException;

	/**
	 * <p>Method: execute delete with multi id,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param ids
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDeleteByIds(Connection connection,Class<T> clazz,Serializable[] ids) throws QueryException;

	/**
	 * <p>Method: execute delete</p>
	 * @param connection
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDelete(Connection connection,T object) throws QueryException;

	/**
	 * <p>Method: execute delete</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDelete(Connection connection,T object,String table) throws QueryException;

	/**
	 * <p>Method: execute delete</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDelete(Connection connection,T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: execute delete</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeDelete(Connection connection,Class<T> clazz,String condition) throws QueryException;

	/**
	 * <p>Method: execute delete collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeDelete(Connection connection,Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: execute delete collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] executeDelete(Connection connection, Collection<T> collection, String table) throws QueryException;

	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param connection
	 * @param sql include insert delete update
	 * @return int
	 * @throws QueryException
	 */
	public int executeUpdateBySql(Connection connection,String sql) throws QueryException;

	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param connection
	 * @param sql include insert delete update
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public abstract int executeUpdateBySql(Connection connection,String sql,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param sqls
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(Connection connection,String[] sqls) throws QueryException;

	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param sql include insert update delete sql only the same sql many data
	 * @param parametersList
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(Connection connection,String sql,List<Object[]> parametersList) throws QueryException;

	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param batchObjectCollection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(Connection connection,Collection<BatchObject> batchObjectCollection) throws QueryException;

	public static enum ExecuteType implements Serializable{
		INSERT,UPDATE_BY_ID,UPDATE_NOT_BY_ID,DELETE_BY_ID,DELETE_NOT_BY_ID
	}

	public static class BatchObject implements Serializable {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = -6086721524393799069L;

		private Object object=null;
		private String condition=null;
		private ExecuteType excuteType=null;

		public BatchObject(Object object,String condition,ExecuteType excuteType) {
			if(object!=null&&excuteType!=null){
				this.object=object;
				this.condition=condition;
				this.excuteType=excuteType;
			}else{
				throw new NullPointerException("object and excuteType can not be null");
			}
		}

		/**
		 * @return the object
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * @return the excuteType
		 */
		public ExecuteType getExcuteType() {
			return excuteType;
		}

		/**
		 * @return the condition
		 */
		public String getCondition() {
			return condition;
		}

		/**
		 * @param condition the condition to set
		 */
		public void setCondition(String condition) {
			this.condition = condition;
		}
	}
}
