package com.oneliang.frame.jdbc;

import java.sql.Connection;

import com.oneliang.util.resource.ResourcePool;
import com.oneliang.util.resource.ResourcePoolException;

/**
 * class ConnectionPool,manager one dataSource connection
 * 
 * @author Dandelion
 * @since 2008-08-25
 */
public class ConnectionPool extends ResourcePool<Connection> {

	public static final String INITIAL_CONNECTIONS="initialConnections";
	public static final String MAX_CONNECTIONS="maxConnections";
	public static final String CONNECTION_ALIVE_TIME="connectionAliveTime";
	public static final String THREAD_SLEEP_TIME="threadSleepTime";

	private ConnectionPoolProcessor connectionPoolProcessor=null;

	public void initialize() {
		super.initialize();
		if(this.connectionPoolProcessor!=null){
			this.connectionPoolProcessor.afterInitialize();
		}
	}

	public Connection getResource() throws ResourcePoolException {
		Connection connection=null;
		Boolean customTransaction=TransactionManager.customTransactionSign.get();
		if(customTransaction!=null&&customTransaction.booleanValue()){
			if(TransactionManager.customTransactionConnection.get()!=null){
				connection=TransactionManager.customTransactionConnection.get();
			}else{
				connection=super.getResource();
				TransactionManager.customTransactionConnection.set(connection);
			}
		}else{
			connection=super.getResource();
		}
		return connection;
	}

	public void releaseResource(Connection resource){
		Boolean customTransaction=TransactionManager.customTransactionSign.get();
		if(customTransaction==null||!customTransaction.booleanValue()){
			super.releaseResource(resource);
			if(TransactionManager.customTransactionConnection.get()!=null){
				TransactionManager.customTransactionConnection.set(null);
			}
		}
	}

	/**
	 * close the connection
	 */
	protected void destroyResource(Connection resource) throws ResourcePoolException{
		if(resource!=null){
			try{
				resource.close();
			} catch (Exception e) {
				throw new ResourcePoolException(e);
			}finally{
				try {
					resource.close();
				} catch (Exception e) {
					throw new ResourcePoolException(e);
				}
			}
		}
	}

	/**
	 * @param connectionPoolProcessor the connectionPoolProcessor to set
	 */
	public void setConnectionPoolProcessor(ConnectionPoolProcessor connectionPoolProcessor) {
		this.connectionPoolProcessor = connectionPoolProcessor;
	}

	public abstract interface ConnectionPoolProcessor{

		/**
		 * after initialize
		 */
		public abstract void afterInitialize();
	}
}
