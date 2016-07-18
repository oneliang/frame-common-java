package com.oneliang.frame.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.frame.jdbc.SqlUtil.SqlProcessor;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class BaseQueryImpl implements BaseQuery {
	
	private static final Logger logger=LoggerManager.getLogger(BaseQueryImpl.class);
	
	private static final SqlProcessor DEFAULT_SQL_PROCESSOR=new DefaultSqlProcessor();
	
	private SqlProcessor sqlProcessor=DEFAULT_SQL_PROCESSOR;

	/**
	 * <p>Method: execute by sql,for all sql</p>
	 * @param connection
	 * @param sql
	 * @throws QueryException
	 */
	public void executeBySql(Connection connection, String sql) throws QueryException {
		this.executeBySql(connection, sql, null);
	}

	/**
	 * <p>Method: execute by sql,for all sql</p>
	 * @param connection
	 * @param sql
	 * @param parameters
	 * @throws QueryException
	 */
	public void executeBySql(Connection connection, String sql, Object[] parameters) throws QueryException {
		PreparedStatement preparedStatement=null;
		try{
			sql=DatabaseMappingUtil.parseSql(sql);
			logger.info(sql);
			preparedStatement=connection.prepareStatement(sql);
			if(parameters!=null){
				int index=1;
				for(Object parameter:parameters){
					this.sqlProcessor.statementProcess(preparedStatement, index, parameter);
					index++;
				}
			}
			preparedStatement.execute();
		}catch (Exception e) {
			throw new QueryException(e);
		}finally{
			if(preparedStatement!=null){
				try {
					preparedStatement.close();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
	}

	/**
	 * <p>Through the class generate the sql</p>
	 * <p>Method: execute query base on the connection and class</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @return List<T>
	 * @throws QueryException 
	 */
	public <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz) throws QueryException{
		return executeQuery(connection,clazz,null);
	}
	
	/**
	 * <p>Through the class generate the sql</p>
	 * <p>Method: execute query base on connection and class and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param condition
	 * @return List<T>
	 * @throws QueryException 
	 */
	public <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String condition) throws QueryException{
		return executeQuery(connection,clazz,null,condition);
	}
	
	/**
	 * <p>Through the class generate the sql</p>
	 * <p>Method: execute query base on connection and class and table and condition</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param table
	 * @param condition
	 * @return List<T>
	 * @throws QueryException 
	 */
	public <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String table,String condition) throws QueryException{
		return executeQuery(connection,clazz,null,table,condition);
	}
	
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
	public <T extends Object> List<T> executeQuery(Connection connection,Class<T> clazz,String[] selectColumns,String table,String condition) throws QueryException{
		return this.executeQuery(connection, clazz, selectColumns, table, condition,null);
	}
	
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
	public <T extends Object> List<T> executeQuery(Connection connection, Class<T> clazz,String[] selectColumns, String table, String condition,Object[] parameters) throws QueryException {
		ResultSet resultSet=null;
		List<T> list=null;
		try{
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			String sql=SqlUtil.selectSql(selectColumns,table,condition,mappingBean);
			resultSet=this.executeQueryBySql(connection, sql,parameters);
			list=SqlUtil.resultSetToObjectList(resultSet,clazz,mappingBean,this.sqlProcessor);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			if(resultSet!=null){
				try {
					resultSet.getStatement().close();
					resultSet.close();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		return list;
	}

	/**
	 * <p>Method: execute query by id</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param id
	 * @return T
	 * @throws QueryException
	 */
	public <T extends Object> T executeQueryById(Connection connection, Class<T> clazz, Serializable id) throws QueryException {
		T object=null;
		List<T> list=null;
		ResultSet resultSet=null;
		try {
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			String sql=SqlUtil.classToSelectIdSql(clazz,mappingBean);
			resultSet=this.executeQueryBySql(connection, sql,new Object[]{id});
			list=SqlUtil.resultSetToObjectList(resultSet,clazz,mappingBean,this.sqlProcessor);
		}catch(Exception e) {
			throw new QueryException(e);
		}finally{
			if(resultSet!=null){
				try {
					resultSet.getStatement().close();
					resultSet.close();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		if(list!=null&&!list.isEmpty()){
			object=list.get(0);
		}
		return object;
	}

	/**
	 * <p>The base sql query</p>
	 * <p>Method: execute query base on the connection and sql command</p>
	 * @param connection
	 * @param clazz
	 * @param sql
	 * @return List
	 * @throws QueryException 
	 */
	public <T extends Object> List<T> executeQueryBySql(Connection connection,Class<T> clazz,String sql) throws QueryException{
		return this.executeQueryBySql(connection, clazz, sql, null);
	}
	
	/**
	 * <p>The base sql query</p>
	 * <p>Method: execute query base on the connection and sql command</p>
	 * @param connection
	 * @param clazz
	 * @param sql
	 * @param parameters
	 * @return List
	 * @throws QueryException 
	 */
	public <T> List<T> executeQueryBySql(Connection connection, Class<T> clazz,String sql, Object[] parameters) throws QueryException {
		ResultSet resultSet=null;
		List<T> list=null;
		try{
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			resultSet=this.executeQueryBySql(connection, sql,parameters);
			list=SqlUtil.resultSetToObjectList(resultSet,clazz,mappingBean,this.sqlProcessor);
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			if(resultSet!=null){
				try {
					resultSet.getStatement().close();
					resultSet.close();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		return list;
	}
	
	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * <p>Caution: use this method must get Statement from the ResultSet and close it and close the ResultSet</p>
	 * @param connection
	 * @param sql
	 * @return ResultSet
	 * @throws QueryException
	 */
	public ResultSet executeQueryBySql(Connection connection, String sql) throws QueryException {
		return this.executeQueryBySql(connection, sql, null);
	}
	
	/**
	 * <p>Method: execute query base on the connection and sql command</p>
	 * <p>Caution: use this method must get Statement from the ResultSet and close it and close the ResultSet</p>
	 * @param connection
	 * @param sql
	 * @param parameters
	 * @return ResultSet
	 * @throws QueryException
	 */
	public ResultSet executeQueryBySql(Connection connection,String sql,Object[] parameters) throws QueryException{
		ResultSet resultSet=null;
		try{
			sql=DatabaseMappingUtil.parseSql(sql);
			logger.info(sql);
			PreparedStatement preparedStatement=connection.prepareStatement(sql);
			if(parameters!=null){
				int index=1;
				for(Object parameter:parameters){
					this.sqlProcessor.statementProcess(preparedStatement, index, parameter);
					index++;
				}
			}
			resultSet=preparedStatement.executeQuery();
		}catch (Exception e) {
			throw new QueryException(e);
		}
		return resultSet;
	}
	
	/**
	 * <p>Method: execute insert</p>
	 * @param connection
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeInsert(Connection connection,T object) throws QueryException{
		return executeInsert(connection,object,null);
	}
	
	/**
	 * <p>Method: execute insert</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeInsert(Connection connection,T object,String table) throws QueryException{
		return this.executeUpdate(connection, object, table,null,ExecuteType.INSERT);
	}
	
	/**
	 * <p>Method: execute insert collection(list),transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeInsert(Connection connection,Collection<T> collection) throws QueryException{
		return this.executeInsert(connection, collection, null);
	}

	/**
	 * <p>Method: execute insert collection(list),transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeInsert(Connection connection,Collection<T> collection,String table) throws QueryException{
		return this.executeUpdate(connection, collection, table,ExecuteType.INSERT);
	}

	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeUpdate(Connection connection,T object) throws QueryException{
		return executeUpdate(connection,object,null);
	}
	
	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeUpdate(Connection connection,T object,String table) throws QueryException{
		return this.executeUpdate(connection, object, table,null);
	}
	
	/**
	 * <p>Method: execute update</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeUpdate(Connection connection,T object,String table,String condition) throws QueryException{
		return this.executeUpdate(connection, object, table,condition,ExecuteType.UPDATE_BY_ID);
	}

	/**
	 * <p>Method: execute update collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeUpdate(Connection connection,Collection<T> collection) throws QueryException{
		return this.executeUpdate(connection, collection, null);
	}

	/**
	 * <p>Method: execute update collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeUpdate(Connection connection,Collection<T> collection,String table) throws QueryException{
		return this.executeUpdate(connection, collection, table,ExecuteType.UPDATE_BY_ID);
	}

	/**
	 * <p>Method: execute delete with id</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param id
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDeleteById(Connection connection,Class<T> clazz,Serializable id) throws QueryException{
		String sql=null;
		try {
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			sql = SqlUtil.classToDeleteOneRowSql(clazz, id,mappingBean);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		int updateResult=this.executeUpdateBySql(connection, sql);
		return updateResult;
	}

	/**
	 * <p>Method: execute delete with multi id,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param ids
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDeleteByIds(Connection connection,Class<T> clazz,Serializable[] ids) throws QueryException{
		int updateResult=0;
		try {
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			String sql=SqlUtil.classToDeleteMultipleRowSql(clazz, ids,mappingBean);
			updateResult=this.executeUpdateBySql(connection, sql);
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return updateResult;
	}
	
	/**
	 * <p>Method: execute delete</p>
	 * @param connection
	 * @param object
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDelete(Connection connection,T object) throws QueryException{
		return executeDelete(connection,object,null);
	}
	
	/**
	 * <p>Method: execute delete</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDelete(Connection connection,T object,String table) throws QueryException{
		return this.executeDelete(connection, object, table,null);
	}
	
	/**
	 * <p>Method: execute delete,condition of auto generate include by id</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @param otherCondition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDelete(Connection connection,T object,String table,String otherCondition) throws QueryException{
		return this.executeUpdate(connection, object, table,otherCondition,ExecuteType.DELETE_BY_ID);
	}
	
	/**
	 * <p>Method: execute delete</p>
	 * @param <T>
	 * @param connection
	 * @param clazz
	 * @param condition
	 * @return int
	 * @throws QueryException
	 */
	public <T extends Object> int executeDelete(Connection connection,Class<T> clazz,String condition) throws QueryException{
		int result=0;
		try{
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			String sql=SqlUtil.deleteSql(null,condition, mappingBean);
			result=this.executeUpdateBySql(connection, sql);
		}catch (Exception e) {
			throw new QueryException(e);
		}
		return result;
	}

	/**
	 * <p>Method: execute delete collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeDelete(Connection connection, Collection<T> collection) throws QueryException {
		return this.executeDelete(connection, collection, null);
	}

	/**
	 * <p>Method: execute delete collection,transaction</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	public <T extends Object> int[] executeDelete(Connection connection, Collection<T> collection, String table) throws QueryException {
		return this.executeUpdate(connection, collection, table, ExecuteType.DELETE_BY_ID);
	}

	/**
	 * <p>Method: execute update include insert sql and update sql,for sql binding</p>
	 * @param connection
	 * @param object
	 * @param table
	 * @param executeType
	 * @return int
	 * @throws QueryException
	 */
	protected <T extends Object> int executeUpdate(Connection connection,T object,String table,String condition,ExecuteType executeType) throws QueryException{
		int rows=0;
		try {
			String sql=null;
			Class<?> clazz=object.getClass();
			MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
			List<Object> parameters=new ArrayList<Object>();
			switch(executeType){
			case INSERT:
				sql = SqlInjectUtil.objectToInsertSql(object,table,mappingBean,parameters);
				break;
			case UPDATE_BY_ID:
				sql = SqlInjectUtil.objectToUpdateSql(object,table,condition,true,mappingBean,parameters);
				break;
			case UPDATE_NOT_BY_ID:
				sql = SqlInjectUtil.objectToUpdateSql(object,table,condition,false,mappingBean,parameters);
				break;
			case DELETE_BY_ID:
				sql = SqlInjectUtil.objectToDeleteSql(object,table,condition,true,mappingBean,parameters);
				break;
			case DELETE_NOT_BY_ID:
				sql = SqlInjectUtil.objectToDeleteSql(object,table,condition,false,mappingBean,parameters);
				break;
			}
			rows=this.executeUpdateBySql(connection,sql,parameters.toArray());
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return rows;
	}

	/**
	 * <p>Method: execute update collection,transaction not for sql binding</p>
	 * @param <T>
	 * @param connection
	 * @param collection
	 * @param table
	 * @param executeType
	 * @return int[]
	 * @throws QueryException
	 */
	protected <T extends Object> int[] executeUpdate(Connection connection,Collection<T> collection,String table,ExecuteType executeType) throws QueryException{
		int[] rows=null;
		if(collection!=null&&!collection.isEmpty()){
			try {
				int i=0;
				String[] sqls=new String[collection.size()];
				for(T object:collection){
					Class<?> clazz=object.getClass();
					MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
					switch(executeType){
					case INSERT:
						sqls[i]=SqlUtil.objectToInsertSql(object,table,mappingBean,this.sqlProcessor);
						break;
					case UPDATE_BY_ID:
						sqls[i]=SqlUtil.objectToUpdateSql(object,table,null,true,mappingBean,this.sqlProcessor);
						break;
					case UPDATE_NOT_BY_ID:
						sqls[i]=SqlUtil.objectToUpdateSql(object,table,null,false,mappingBean,this.sqlProcessor);
						break;
					case DELETE_BY_ID:
						sqls[i]=SqlUtil.objectToDeleteSql(object, table, null, true,mappingBean,this.sqlProcessor);
						break;
					case DELETE_NOT_BY_ID:
						sqls[i]=SqlUtil.objectToDeleteSql(object, table, null, false,mappingBean,this.sqlProcessor);
						break;
					}
					i++;
				}
				rows=this.executeBatch(connection,sqls);
			} catch (Exception e) {
				throw new QueryException(e);
			}
		}
		return rows;
	}

	/**
	 * <p>Method: execute update collection,transaction,for preparedStatement sql binding</p>
	 * @param <T>
	 * @param <M>
	 * @param connection
	 * @param collection
	 * @param clazz mapping class
	 * @param table
	 * @return int[]
	 * @throws QueryException
	 */
	protected <T extends Object,M extends Object> int[] executeUpdate(Connection connection,Collection<T> collection,Class<M> clazz,String table,ExecuteType executeType) throws QueryException{
		int[] rows=null;
		PreparedStatement preparedStatement=null;
		if(collection!=null&&!collection.isEmpty()){
			boolean customTransaction=false;
			Boolean customTransactionSign=TransactionManager.customTransactionSign.get();
			if(customTransactionSign!=null&&customTransactionSign.booleanValue()){
				customTransaction=true;
			}
			try {
				MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
				List<String> fieldNameList=new ArrayList<String>();
				String sql=null;
				switch(executeType){
				case INSERT:
					sql=SqlInjectUtil.classToInsertSql(clazz, table, mappingBean, fieldNameList);
					break;
				case UPDATE_BY_ID:
					sql=SqlInjectUtil.classToUpdateSql(clazz, table, null, true, mappingBean, fieldNameList);
					break;
				case UPDATE_NOT_BY_ID:
					sql=SqlInjectUtil.classToUpdateSql(clazz, table, null, false, mappingBean, fieldNameList);
					break;
				case DELETE_BY_ID:
					sql=SqlInjectUtil.classToDeleteSql(clazz, table, null, true, mappingBean, fieldNameList);
					break;
				case DELETE_NOT_BY_ID:
					sql=SqlInjectUtil.classToDeleteSql(clazz, table, null, false, mappingBean, fieldNameList);
					break;
				}
				sql=DatabaseMappingUtil.parseSql(sql);
				logger.info(sql);
				if(!customTransaction){
					connection.setAutoCommit(false);
				}
				preparedStatement=connection.prepareStatement(sql);
				for(T object:collection){
					if(fieldNameList!=null){
						int index=1;
						for(String fieldName:fieldNameList){
							Object parameter=ObjectUtil.getterOrIsMethodInvoke(object, fieldName);
							this.sqlProcessor.statementProcess(preparedStatement, index, parameter);
							index++;
						}
						preparedStatement.addBatch();
					}
				}
				rows=preparedStatement.executeBatch();
				preparedStatement.clearBatch();
				if(!customTransaction){
					connection.commit();
				}
			}catch(Exception e){
				if(!customTransaction){
					try {
						connection.rollback();
					} catch (Exception ex) {
						throw new QueryException(ex);
					}
				}
				throw new QueryException(e);
			}finally{
				try{
					if(!customTransaction){
						connection.setAutoCommit(true);
					}
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		return rows;
	}

	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param connection
	 * @param sql include insert delete update
	 * @return int
	 * @throws QueryException
	 */
	public int executeUpdateBySql(Connection connection, String sql) throws QueryException {
		return this.executeUpdateBySql(connection, sql, null);
	}
	
	/**
	 * <p>Method: execute update by sql statement</p>
	 * @param connection
	 * @param sql include insert delete update
	 * @param parameters
	 * @return int
	 * @throws QueryException
	 */
	public int executeUpdateBySql(Connection connection,String sql,Object[] parameters) throws QueryException{
		PreparedStatement preparedStatement=null;
		int updateResult=0;
		try{
			sql=DatabaseMappingUtil.parseSql(sql);
			logger.info(sql);
			preparedStatement=connection.prepareStatement(sql);
			if(parameters!=null){
				int index=1;
				for(Object parameter:parameters){
					this.sqlProcessor.statementProcess(preparedStatement, index, parameter);
					index++;
				}
			}
			updateResult=preparedStatement.executeUpdate();
		}catch(Exception e){
			throw new QueryException(e);
		}finally{
			if(preparedStatement!=null){
				try {
					preparedStatement.close();
				} catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		return updateResult;
	}
	
	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param sqls
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(Connection connection,String[] sqls) throws QueryException{
		Statement statement=null;
		int[] results=null;
		boolean customTransaction=false;
		Boolean customTransactionSign=TransactionManager.customTransactionSign.get();
		if(customTransactionSign!=null&&customTransactionSign.booleanValue()){
			customTransaction=true;
		}
		try{
			if(!customTransaction){
				connection.setAutoCommit(false);
			}
			statement=connection.createStatement();
			for(String sql:sqls){
				sql=DatabaseMappingUtil.parseSql(sql);
				logger.info(sql);
				statement.addBatch(sql);
			}
			results=statement.executeBatch();
			if(!customTransaction){
				connection.commit();
			}
		}catch(Exception e){
			if(!customTransaction){
				try {
					connection.rollback();
				} catch (SQLException ex) {
					throw new QueryException(ex);
				}
			}
			throw new QueryException(e);
		}finally{
			try{
				if(!customTransaction){
					connection.setAutoCommit(true);
				}
				if(statement!=null){
					statement.close();
				}
			}catch (Exception e) {
				throw new QueryException(e);
			}
		}
		return results;
	}

	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param sql include insert update delete sql only the same sql many data
	 * @param parametersList
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(Connection connection, String sql, List<Object[]> parametersList) throws QueryException {
		int[] results=null;
		PreparedStatement preparedStatement=null;
		if(parametersList!=null&&!parametersList.isEmpty()){
			boolean customTransaction=false;
			Boolean customTransactionSign=TransactionManager.customTransactionSign.get();
			if(customTransactionSign!=null&&customTransactionSign.booleanValue()){
				customTransaction=true;
			}
			try {
				sql=DatabaseMappingUtil.parseSql(sql);
				logger.info(sql);
				if(!customTransaction){
					connection.setAutoCommit(false);
				}
				preparedStatement=connection.prepareStatement(sql);
				for(Object[] parameters:parametersList){
					if(parameters!=null){
						int index=1;
						for(Object parameter:parameters){
							this.sqlProcessor.statementProcess(preparedStatement, index, parameter);
							index++;
						}
						preparedStatement.addBatch();
					}
				}
				results=preparedStatement.executeBatch();
				preparedStatement.clearBatch();
				if(!customTransaction){
					connection.commit();
				}
			}catch(Exception e){
				if(!customTransaction){
					try {
						connection.rollback();
					} catch (Exception ex) {
						throw new QueryException(ex);
					}
				}
				throw new QueryException(e);
			}finally{
				try{
					if(!customTransaction){
						connection.setAutoCommit(true);
					}
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}catch (Exception e) {
					throw new QueryException(e);
				}
			}
		}
		return results;
	}

	/**
	 * <p>Method: execute batch by connection,transaction</p>
	 * @param connection
	 * @param batchObjectCollection
	 * @return int[]
	 * @throws QueryException
	 */
	public int[] executeBatch(Connection connection, Collection<BatchObject> batchObjectCollection) throws QueryException {
		int[] results=null;
		if(batchObjectCollection!=null&&!batchObjectCollection.isEmpty()){
			try {
				int i=0;
				String[] sqls=new String[batchObjectCollection.size()];
				for(BatchObject batchObject:batchObjectCollection){
					Object object=batchObject.getObject();
					ExecuteType executeType=batchObject.getExcuteType();
					String condition=batchObject.getCondition();
					Class<?> clazz=object.getClass();
					MappingBean mappingBean=ConfigurationFactory.findMappingBean(clazz);
					switch(executeType){
					case INSERT:
						sqls[i]=SqlUtil.objectToInsertSql(object,null,mappingBean,this.sqlProcessor);
						break;
					case UPDATE_BY_ID:
						sqls[i]=SqlUtil.objectToUpdateSql(object,null,condition,true,mappingBean,this.sqlProcessor);
						break;
					case UPDATE_NOT_BY_ID:
						sqls[i]=SqlUtil.objectToUpdateSql(object,null,condition,false,mappingBean,this.sqlProcessor);
						break;
					case DELETE_BY_ID:
						sqls[i]=SqlUtil.objectToDeleteSql(object,null,condition,true,mappingBean,this.sqlProcessor);
						break;
					case DELETE_NOT_BY_ID:
						sqls[i]=SqlUtil.objectToDeleteSql(object,null,condition,false,mappingBean,this.sqlProcessor);
						break;
					}
					i++;
				}
				results=this.executeBatch(connection,sqls);
			} catch (Exception e) {
				throw new QueryException(e);
			}
		}
		return results;
	}

	/**
	 * @param sqlProcessor the sqlProcessor to set
	 */
	public void setSqlProcessor(SqlProcessor sqlProcessor) {
		this.sqlProcessor = sqlProcessor;
	}
}
