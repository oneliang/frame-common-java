package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.frame.bean.Page;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.resource.ResourcePool;

/**
 * all QueryImpl can extends DefaultQueryImpl,but must initialize the property 
 * @author lwx
 * @since 2011-02-12
 */
public class DefaultQueryImpl extends BaseQueryImpl implements Query{
	
	protected ResourcePool<Connection> connectionPool=null;

	/**
	 * <p>Method: delete object just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObject(T object) throws QueryException{
		return this.deleteObject(object,null);
	}
	
	/**
	 * <p>Method: delete object,by condition just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObject(T object,String condition) throws QueryException{
		return this.deleteObject(object,null,condition);
	}

	/**
	 * <p>Method: delete object,by table condition just by object id,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObject(T object,String table,String condition) throws QueryException{
		return this.executeUpdate(object, table,condition,ExecuteType.DELETE_BY_ID);
	}

	/**
	 * <p>Method: delete object not by id,it is sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObjectNotById(T object) throws QueryException {
		return this.deleteObjectNotById(object, null);
	}

	/**
	 * <p>Method: delete object not by id,by condition,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObjectNotById(T object, String condition) throws QueryException {
		return this.deleteObjectNotById(object, null, condition);
	}

	/**
	 * <p>Method: delete object not by id,by table condition,sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObjectNotById(T object, String table, String condition) throws QueryException {
		return this.executeUpdate(object, table, condition, ExecuteType.DELETE_NOT_BY_ID);
	}

	/**
	 * <p>Method: delete class</p>
	 * @param clazz
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObject(Class<T> clazz) throws QueryException {
	    return this.deleteObject(clazz, null);
	}

	/**
	 * <p>Method: delete class,by condition</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObject(Class<T> clazz,String condition) throws QueryException{
		int result=0;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			result=this.executeDelete(connection, clazz, condition);
		}catch (Exception e) {
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return result;
	}

	/**
	 * <p>Method: delete object collection,transaction,not sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public <T> int[] deleteObject(Collection<T> collection) throws QueryException {
		return this.deleteObject(collection,StringUtil.BLANK);
	}

	/**
	 * <p>Method: delete object collection,transaction,not sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] deleteObject(Collection<T> collection,String table) throws QueryException {
		return this.executeUpdate(collection,table,ExecuteType.DELETE_BY_ID);
	}

	/**
	 * <p>Method: delete object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object,M extends Object> int[] deleteObject(Collection<T> collection, Class<M> clazz) throws QueryException {
		return this.deleteObject(collection, clazz, null);
	}

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
	public <T extends Object,M extends Object> int[] deleteObject(Collection<T> collection, Class<M> clazz, String table) throws QueryException {
		return this.executeUpdate(collection, clazz, table, ExecuteType.DELETE_BY_ID);
	}

	/**
	 * <p>Method: delete object by id,not sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObjectById(Class<T> clazz,Serializable id) throws QueryException{
		int updateResult=0;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			updateResult=this.executeDeleteById(connection,clazz,id);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return updateResult;
	}

	/**
	 * <p>Method: delete object by multiple id,transaction,not sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param ids
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int deleteObjectByIds(Class<T> clazz,Serializable[] ids) throws QueryException{
		int updateResult=0;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			updateResult=this.executeDeleteByIds(connection,clazz,ids);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return updateResult;
	}

	/**
	 * <p>Method: insert object for sql binding</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int insertObject(T object) throws QueryException{
		return this.insertObject(object,null);
	}

	/**
	 * <p>Method: insert object for sql binding</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int insertObject(T object,String table) throws QueryException{
		return this.executeUpdate(object, table, null, ExecuteType.INSERT);
	}

	/**
	 * <p>Method: insert object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] insertObject(Collection<T> collection) throws QueryException {
		return this.insertObject(collection, StringUtil.BLANK);
	}

	/**
	 * <p>Method: insert object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] insertObject(Collection<T> collection,String table) throws QueryException {
		return this.executeUpdate(collection, table,ExecuteType.INSERT);
	}

	/**
	 * <p>Method: insert object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object,M extends Object> int[] insertObject(Collection<T> collection, Class<M> clazz) throws QueryException {
		return this.insertObject(collection, clazz, null);
	}

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
	public <T extends Object,M extends Object> int[] insertObject(Collection<T> collection, Class<M> clazz, String table) throws QueryException {
		return this.executeUpdate(collection, clazz, table, ExecuteType.INSERT);
	}

	/**
	 * <p>Method: update object,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObject(T object) throws QueryException{
		return this.updateObject(object, null);
	}

	/**
	 * <p>Method: update object,by condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObject(T object,String condition) throws QueryException{
		return this.updateObject(object, null, condition);
	}

	/**
	 * <p>Method: update object,by table,condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObject(T object,String table,String condition) throws QueryException{
		return this.executeUpdate(object, table, condition, ExecuteType.UPDATE_BY_ID);
	}

	/**
	 * <p>Method: update object not by id,it is sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObjectNotById(T object) throws QueryException {
		return this.updateObjectNotById(object, null);
	}

	/**
	 * <p>Method: update object not by id,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObjectNotById(T object, String condition) throws QueryException {
		return this.updateObjectNotById(object, null, condition);
	}

	/**
	 * <p>Method: update object not by id,by table,condition,sql binding,null value field is not update</p>
	 * @param <T>
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int updateObjectNotById(T object,String table,String condition) throws QueryException {
		return this.executeUpdate(object, table, condition, ExecuteType.UPDATE_NOT_BY_ID);
	}

	/**
	 * <p>Method: update object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] updateObject(Collection<T> collection) throws QueryException {
		return this.updateObject(collection,StringUtil.BLANK);
	}

	/**
	 * <p>Method: update object collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] updateObject(Collection<T> collection,String table) throws QueryException {
		return this.executeUpdate(collection,table,ExecuteType.UPDATE_BY_ID);
	}

	/**
	 * <p>Method: update object collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz mapping class
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object,M extends Object> int[] updateObject(Collection<T> collection, Class<M> clazz) throws QueryException {
		return this.updateObject(collection, clazz, null);
	}

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
	public <T extends Object,M extends Object> int[] updateObject(Collection<T> collection, Class<M> clazz, String table) throws QueryException {
		return this.executeUpdate(collection, clazz, table, ExecuteType.UPDATE_BY_ID);
	}

	/**
	 * <p>Method: select object by id</p>
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return T
	 * @throws QueryException
	 */
	public <T extends Object> T selectObjectById(Class<T> clazz, Serializable id) throws QueryException{
		T object=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			object=this.executeQueryById(connection,clazz,id);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return object;
	}

	/**
	 * <p>Through the class to find all</p>
	 * <p>Method: select object list</p>
	 * @param <T>
	 * @param clazz
	 * @return List<T>
	 */
	public <T extends Object> List<T> selectObjectList(Class<T> clazz) throws QueryException{
		return this.selectObjectList(clazz,null);
	}
	
	/**
	 * <p>Through the class to find all but with the condition</p>
	 * <p>Method: select object list,by condition</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String condition) throws QueryException{
		return this.selectObjectList(clazz,null,condition);
	}

	/**
	 * <p>Method: select object list,by condition,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String condition,Object[] parameters) throws QueryException {
		return this.selectObjectList(clazz, null, null, condition, parameters);
	}

	/**
	 * <p>Method: select object list,by table,condition</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String table,String condition) throws QueryException {
		return this.selectObjectList(clazz,null,table, condition);
	}

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
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String table,String condition,Object[] parameters) throws QueryException {
		return this.selectObjectList(clazz, null, table, condition, parameters);
	}

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
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String[] selectColumns,String table,String condition) throws QueryException{
		return this.selectObjectList(clazz, selectColumns, table, condition,null);
	}

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
	public <T extends Object> List<T> selectObjectList(Class<T> clazz,String[] selectColumns,String table,String condition,Object[] parameters) throws QueryException{
		List<T> list=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			list=this.executeQuery(connection, clazz,selectColumns,table,condition,parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return list;
	}

	/**
	 * <p>Method: select object list by sql</p>
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectListBySql(Class<T> clazz, String sql) throws QueryException {
		return this.selectObjectListBySql(clazz, sql, null);
	}

	/**
	 * <p>Method: select object list by sql,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectListBySql(Class<T> clazz, String sql, Object[] parameters) throws QueryException {
		List<T> list=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			list=this.executeQueryBySql(connection,clazz,sql,parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return list;
	}

	/**
	 * <p>Method: select object pagination list,has implement</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page) throws QueryException {
		return this.selectObjectPaginationList(clazz, page, null);
	}

	/**
	 * <p>Method: select object pagination list,has implement</p>
	 * @param <T>
	 * @param clazz
	 * @param page
	 * @param condition
	 * @return List<T>
	 * @throws QueryException
	 */
	public <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page,String condition) throws QueryException{
		return this.selectObjectPaginationList(clazz, page, null, null, condition);
	}

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
	public <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz,Page page,String condition,Object[] parameters) throws QueryException{
		return this.selectObjectPaginationList(clazz, page, null, null, condition, parameters);
	}

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
	public <T extends Object> List<T> selectObjectPaginationList(Class<T> clazz, Page page, String[] selectColumns, String table, String condition) throws QueryException {
		return this.selectObjectPaginationList(clazz, page, selectColumns, table, condition, null);
	}

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
		StringBuilder sqlConditions=new StringBuilder();
		if(condition!=null){
			sqlConditions.append(condition);
		}
		sqlConditions.append(" "+Constants.Database.MySql.PAGINATION+" ");
		sqlConditions.append(startRow+Constants.Symbol.COMMA+rowsPerPage);
		return this.selectObjectList(clazz,selectColumns,table, sqlConditions.toString(), parameters);
	}

	/**
	 * <p>Method: execute by sql ,for all sql</p>
	 * @param sql
	 * @throws QueryException
	 */
	public void executeBySql(String sql) throws QueryException{
		this.executeBySql(sql, null);
	}

	/**
	 * <p>Method: execute by sql ,for all sql,sql binding</p>
	 * @param sql
	 * @param parameters
	 * @throws QueryException
	 */
	public void executeBySql(String sql, Object[] parameters) throws QueryException {
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			this.executeBySql(connection,sql,parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
	}

	/**
	 * <p>Method: execute query by sql statement,use caution,must close the statement</p>
	 * @param sql
	 * @return ResultSet
	 * @throws QueryException
	 */
	public ResultSet executeQueryBySql(String sql) throws QueryException {
		return this.executeQueryBySql(sql, null);
	}

	/**
	 * <p>Method: execute query by sql statement,use caution,must close the statement</p>
	 * @param sql
	 * @param parameters
	 * @return ResultSet
	 * @throws QueryException
	 */
	public ResultSet executeQueryBySql(String sql, Object[] parameters) throws QueryException {
		ResultSet resultSet=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			resultSet=this.executeQueryBySql(connection, sql, parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return resultSet;
	}

	/**
	 * <p>Method: execute update</p>
	 * @param object
	 * @param table
	 * @param executeType
	 * @return int
	 * @throws QueryException
	 * @throws ConnectionPoolQueryException
	 */
	public <T extends Object> int executeUpdate(T object,String table,String condition,ExecuteType executeType) throws QueryException{
		int rows=0;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			rows=this.executeUpdate(connection, object, table,condition,executeType);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return rows;
	}

	/**
	 * <p>Method: execute update collection,transaction,not for sql binding</p>
	 * @param <T>
	 * @param collection
	 * @param table
	 * @param executeType
	 * @return int[]
	 * @throws QueryException
	 */
	protected <T extends Object> int[] executeUpdate(Collection<T> collection,String table,ExecuteType executeType) throws QueryException{
		int[] rows=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			rows=this.executeUpdate(connection,collection,table,executeType);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return rows;
	}

	/**
	 * <p>Method: execute update collection,transaction,for sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param collection
	 * @param clazz
	 * @param table
	 * @param executeType
	 * @return int[]
	 * @throws QueryException
	 */
	protected <T extends Object,M extends Object> int[] executeUpdate(Collection<T> collection,Class<M> clazz,String table,ExecuteType executeType) throws QueryException{
		int[] rows=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			rows=this.executeUpdate(connection,collection,clazz,table,executeType);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return rows;
	}

	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param sql include insert delete update
	 * @return int
	 * @throws QueryException 
	 */
	public int executeUpdateBySql(String sql) throws QueryException{
		return this.executeUpdateBySql(sql, null);
	}

	/**
	 * <p>Method: execute update by sql statement it is sql binding</p>
	 * @param sql include insert delete update
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public int executeUpdateBySql(String sql, Object[] parameters) throws QueryException{
		int updateResult=0;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			updateResult=this.executeUpdateBySql(connection, sql, parameters);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return updateResult;
	}

	/**
	 * <p>Method: execute batch,transaction</p>
	 * @param sqls
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(String[] sqls) throws QueryException{
		int[] results=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			results=this.executeBatch(connection, sqls);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return results;
	}

	/**
	 * <p>Method: execute batch,transaction</p>
	 * @param sql include insert update delete sql only the same sql many data
	 * @param parametersList
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(String sql, List<Object[]> parametersList) throws QueryException {
		int[] results=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			results=this.executeBatch(connection, sql, parametersList);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return results;
	}

	/**
	 * <p>Method: execute batch,transaction</p>
	 * @param batchObjectCollection
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(Collection<BatchObject> batchObjectCollection) throws QueryException {
		int[] results=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			results=this.executeBatch(connection, batchObjectCollection);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			this.connectionPool.releaseResource(connection);
		}
		return results;
	}

	/**
	 * <p>Method: table total rows</p>
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public int totalRows(String table) throws QueryException {
		return this.totalRows(table, null);
	}

	/**
	 * <p>count table total rows</p>
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public int totalRows(String table,String condition) throws QueryException {
		return this.totalRows(table, condition, null);
	}

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param table
	 * @param condition
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int totalRows(String table,String condition,Object[] parameters) throws QueryException{
		return this.totalRows(null, table, condition,parameters);
	}

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int totalRows(Class<T> clazz) throws QueryException {
		return this.totalRows(clazz, null);
	}

	/**
	 * <p>Method; get the total size,it is sql binding</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int totalRows(Class<T> clazz, String condition) throws QueryException {
		return this.totalRows(clazz, null ,condition);
	}

	/**
	 * <p>Method; get the total size</p>
	 * @param <T>
	 * @param clazz
	 * @param condition
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int totalRows(Class<T> clazz, String condition, Object[] parameters) throws QueryException {
		return this.totalRows(clazz, null, condition, parameters);
	}

	/**
	 * <p>Method; get the total size</p>
	 * @param <T>
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int totalRows(Class<T> clazz,String table,String condition) throws QueryException{
		return this.totalRows(clazz, table, condition, null);
	}

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
	public <T extends Object> int totalRows(Class<T> clazz,String table,String condition,Object[] parameters) throws QueryException{
		int totalRows=0;
		ResultSet resultSet=null;
		Connection connection=null;
		try{
			connection=this.connectionPool.getResource();
			String sql=null;
			if(clazz!=null){
				MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
				sql=SqlUtil.selectSql(new String[]{"COUNT(0) AS "+Constants.Database.COLUMN_NAME_TOTAL},table,condition,mappingBean);
			}else{
				sql=SqlUtil.selectSql(new String[]{"COUNT(0) AS "+Constants.Database.COLUMN_NAME_TOTAL},table,condition,null);
			}
			resultSet=super.executeQueryBySql(connection, sql, parameters);
			if(resultSet!=null&&resultSet.next()){
				totalRows=resultSet.getInt(Constants.Database.COLUMN_NAME_TOTAL);
			}
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			try{
				if(resultSet!=null){
					resultSet.getStatement().close();
					resultSet.close();
				}
			}catch(Exception e){
				throw new QueryException(e);
			}finally{
				this.connectionPool.releaseResource(connection);
			}
		}
		return totalRows;
	}

	/**
	 * <p>execute transaction</p>
	 * @param transaction
	 * @throws QueryException
	 */
	public void executeTransaction(Transaction transaction) throws QueryException {
		if(transaction!=null){
			boolean isFirstIn=false;
			Boolean customTransactionSign=TransactionManager.customTransactionSign.get();
			if(customTransactionSign==null||!customTransactionSign.booleanValue()){
				isFirstIn=true;
			}
			if(isFirstIn){
				TransactionManager.customTransactionSign.set(true);
				Connection connection=null;
				//beginTransaction
				try{
					connection=this.connectionPool.getResource();
					connection.setAutoCommit(false);
					transaction.execute();
					connection.commit();
				}catch(Exception e){
					try {
						connection.rollback();
					} catch (Exception ex) {
						throw new QueryException(ex);
					}
				}finally{
					//endTransaction
					try {
						connection.setAutoCommit(true);
					} catch (Exception e) {
						throw new QueryException(e);
					}finally{
						TransactionManager.customTransactionSign.set(false);
						this.connectionPool.releaseResource(connection);
					}
				}
			}else{
				try {
					transaction.execute();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
	}

	/**
	 * @param connectionPool the connectionPool to set
	 */
	public void setConnectionPool(ResourcePool<Connection> connectionPool) {
		this.connectionPool = connectionPool;
	}

	/**
	 * @return the connectionPool
	 */
	public ResourcePool<Connection> getConnectionPool() {
		return connectionPool;
	}
}
