package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import com.oneliang.frame.bean.Page;
import com.oneliang.util.resource.ResourcePool;

public abstract interface Query extends BaseQuery {

	/**
	 * <p>Method: delete object just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObject(T object) throws QueryException;

	/**
	 * <p>Method: delete object,by condition just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObject(T object,String condition) throws QueryException;

	/**
	 * <p>Method: delete object,by table condition just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObject(T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: delete object not by id,it is sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObjectNotById(T object) throws QueryException;

	/**
	 * <p>Method: delete object not by id,by condition,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObjectNotById(T object,String condition) throws QueryException;

	/**
	 * <p>Method: delete object not by id,by table condition,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObjectNotById(T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: delete class</p>
	 * @param clazz
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObject(Class<T> clazz) throws QueryException;

	/**
	 * <p>Method: delete class,by condition</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObject(Class<T> clazz,String condition) throws QueryException;

	/**
	 * <p>Method: delete object collection,transaction,not sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] deleteObject(Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: delete object collection,transaction,not sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T> int[] deleteObject(Collection<T> collection,String table) throws QueryException;

	/**
	 * <p>Method: delete object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] deleteObject(Collection<T> collection,Class<M> clazz) throws QueryException;

	/**
	 * <p>Method: delete object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] deleteObject(Collection<T> collection,Class<M> clazz,String table) throws QueryException;

	/**
	 * <p>Method: delete object by id,not sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObjectById(Class<T> clazz,Serializable id) throws QueryException;

	/**
	 * <p>Method: delete object by multiple id,transaction,not sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param ids
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int deleteObjectByIds(Class<T> clazz,Serializable[] ids) throws QueryException;

	/**
	 * <p>Method: insert object for sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int insertObject(T object) throws QueryException;

	/**
	 * <p>Method: insert object for sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int insertObject(T object,String table) throws QueryException;

	/**
	 * <p>Method: insert object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] insertObject(Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: insert object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] insertObject(Collection<T> collection,String table) throws QueryException;

	/**
	 * <p>Method: insert object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] insertObject(Collection<T> collection,Class<M> clazz) throws QueryException;

	/**
	 * <p>Method: insert object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] insertObject(Collection<T> collection,Class<M> clazz,String table) throws QueryException;

	/**
	 * <p>Method: update object,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObject(T object) throws QueryException;

	/**
	 * <p>Method: update object,by condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObject(T object,String condition) throws QueryException;

	/**
	 * <p>Method: update object,by table,condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObject(T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: update object not by id,it is sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObjectNotById(T object) throws QueryException;

	/**
	 * <p>Method: update object not by id,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObjectNotById(T object,String condition) throws QueryException;

	/**
	 * <p>Method: update object not by id,by table,condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int updateObjectNotById(T object,String table,String condition) throws QueryException;

	/**
	 * <p>Method: update object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] updateObject(Collection<T> collection) throws QueryException;

	/**
	 * <p>Method: update object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object> int[] updateObject(Collection<T> collection,String table) throws QueryException;

	/**
	 * <p>Method: update object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] updateObject(Collection<T> collection,Class<M> clazz) throws QueryException;

	/**
	 * <p>Method: update object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract <T extends Object,M extends Object> int[] updateObject(Collection<T> collection,Class<M> clazz,String table) throws QueryException;

	/**
	 * <p>Method: select object by id</p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> T selectObjectById(Class<T> clazz, Serializable id) throws QueryException;

	/**
	 * <p>Through the class to find all</p>
	 * <p>Method: select object list</p>
	 * @param <T>
	 * @param clazz
	 * @return List<T>
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz) throws QueryException;

	/**
	 * <p>Through the class to find all but with the condition</p>
	 * <p>Method: select object list,by condition</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String condition) throws QueryException;

	/**
	 * <p>Method: select object list,by condition,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: select object list,by table,condition</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String table,String condition) throws QueryException;

	/**
	 * <p>Method: select object list,by table,condition,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String table,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: select object list,by column,table,condition</p>
	 * @param <T>
	 * @param clazz
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String[] selectColumns,String table,String condition) throws QueryException;

	/**
	 * <p>Method: select object list,by column,table,condition,parameters,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectList(Class<T> clazz,String[] selectColumns,String table,String condition,Object[] parameters) throws QueryException;

	/**<p>Method: select object list by sql</p>
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectListBySql(Class<T> clazz,String sql) throws QueryException;

	/**
	 * <p>Method: select object list by sql,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectListBySql(Class<T> clazz, String sql, Object[] parameters) throws QueryException;

	/**
	 * <p>Method: select object pagination list,has implement</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page) throws QueryException;

	/**
	 * <p>Method: select object pagination list,has implement</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page,String condition) throws QueryException;

	/**
	 * <p>Method: select object pagination list,has implement,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: select object pagination list,has implement</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @param selectColumns
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public abstract <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page,String[] selectColumns,String table,String condition) throws QueryException;

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
	public abstract <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz, Page page, String[] selectColumns, String table, String condition, Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute by sql ,for all sql</p>
	 * @param sql
	 * @throws QueryException
	 */
	public abstract void executeBySql(String sql) throws QueryException;

	/**
	 * <p>Method: execute by sql ,for all sql,sql binding</p>
	 * @param sql
	 * @param parameters
	 * @throws QueryException
	 */
	public abstract void executeBySql(String sql, Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute query by sql statement,use caution,must close the statement</p>
	 * @param sql
	 * @return ResultSet
	 * @throws QueryException
	 */
	public abstract ResultSet executeQueryBySql(String sql) throws QueryException;

	/**
	 * <p>Method: execute query by sql statement,use caution,must close the statement</p>
	 * @param sql
	 * @param parameters
	 * @return ResultSet
	 * @throws QueryException
	 */
	public abstract ResultSet executeQueryBySql(String sql,Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute update</p>
	 * @param object
	 * @param table
	 * @param executeType
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int executeUpdate(T object,String table,String condition,ExecuteType executeType) throws QueryException;

	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param sql include insert delete update
	 * @return int
	 * @throws QueryException 
	 */
	public abstract int executeUpdateBySql(String sql) throws QueryException;

	/**
	 * <p>Method: execute update by sql statement it is sql binding</p>
	 * @param sql include insert delete update
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public abstract int executeUpdateBySql(String sql, Object[] parameters) throws QueryException;

	/**
	 * <p>Method: execute batch</p>
	 * @param sqls
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(String[] sqls) throws QueryException;

	/**
	 * <p>Method: execute batch,transaction</p>
	 * @param sql include insert update delete sql only the same sql many data
	 * @param parametersList
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(String sql,List<Object[]> parametersList) throws QueryException;

	/**
	 * <p>Method: execute batch</p>
	 * @param batchObjectCollection
	 * @return int[]
	 * @throws QueryException
	 */
	public abstract int[] executeBatch(Collection<BatchObject> batchObjectCollection) throws QueryException;

	/**
	 * <p>Method: table total rows</p>
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public abstract int totalRows(String table) throws QueryException;

	/**
	 * <p>count table total rows</p>
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract int totalRows(String table,String condition) throws QueryException;

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(String table,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(Class<T> clazz) throws QueryException;

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(Class<T> clazz,String condition) throws QueryException;

	/**
	 * <p>Method; get the total size</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(Class<T> clazz,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>Method; get the total size</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(Class<T> clazz,String table,String condition) throws QueryException;

	/**
	 * <p>Method; get the total size</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public abstract <T extends Object> int totalRows(Class<T> clazz,String table,String condition,Object[] parameters) throws QueryException;

	/**
	 * <p>execute transaction</p>
	 * @param transaction
	 * @throws QueryException
	 */
	public abstract void executeTransaction(Transaction transaction) throws QueryException;

	/**
	 * @return the connectionPool
	 */
	public abstract ResourcePool<Connection> getConnectionPool();
}
