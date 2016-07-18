package com.oneliang.frame.jdbc;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.Constant;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.file.FileUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;
import com.oneliang.util.resource.ResourcePool;
import com.oneliang.util.resource.ResourceSource;

@Deprecated
public class DatabaseContext extends AbstractContext {

	private static final Logger logger=LoggerManager.getLogger(DatabaseContext.class);

	protected static final Map<String,ResourcePool<Connection>> connectionPoolMap=new ConcurrentHashMap<String,ResourcePool<Connection>>();

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			String path=parameters;
			String tempClassesRealPath=classesRealPath;
			if(tempClassesRealPath==null){
				tempClassesRealPath=this.classLoader.getResource(StringUtil.BLANK).getPath();
			}
			path=tempClassesRealPath+path;
			Properties properties=FileUtil.getProperties(path);
			if(properties!=null){
				Iterator<Entry<Object,Object>> iterator=properties.entrySet().iterator();
				while(iterator.hasNext()){
				    Entry<Object,Object> entry=iterator.next();
					String key=entry.getKey().toString();
					int index=key.indexOf(Constant.Symbol.DOT);
					if(index>0){
						String poolName=key.substring(0, index);
						String propertyName=key.substring(index+1, key.length());
						String value=entry.getValue().toString();
						ResourcePool<Connection> pool=null;
						if(connectionPoolMap.containsKey(poolName)){
							pool=connectionPoolMap.get(poolName);
						}else{
							pool=new ConnectionPool();
							pool.setResourcePoolName(poolName);
							ResourceSource<Connection> dataSource=new ConnectionSource();
							pool.setResourceSource(dataSource);
							connectionPoolMap.put(poolName, pool);
						}
						if(propertyName.equals(ConnectionPool.CONNECTION_ALIVE_TIME)){
							pool.setResourceAliveTime(Long.parseLong(value));
						}else if(propertyName.equals(ConnectionPool.THREAD_SLEEP_TIME)){
							pool.setThreadSleepTime(Long.parseLong(value));
						}else if(propertyName.equals(ConnectionPool.INITIAL_CONNECTIONS)){
							pool.setMinResources(Integer.parseInt(value));
						}else if(propertyName.equals(ConnectionPool.MAX_CONNECTIONS)){
							pool.setMaxResources(Integer.parseInt(value));
						}else if(propertyName.equals(ConnectionSource.CONNECTION_SOURCE_NAME)){
							((ConnectionSource)pool.getResourceSource()).setConnectionSourceName(value);
						}else if(propertyName.equals(ConnectionSource.DRIVER)){
							((ConnectionSource)pool.getResourceSource()).setDriver(value);
						}else if(propertyName.equals(ConnectionSource.URL)){
							((ConnectionSource)pool.getResourceSource()).setUrl(value);
						}else if(propertyName.equals(ConnectionSource.USER)){
							((ConnectionSource)pool.getResourceSource()).setUser(value);
						}else if(propertyName.equals(ConnectionSource.PASSWORD)){
							((ConnectionSource)pool.getResourceSource()).setPassword(value);
						}
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * destroy
	 */
	public void destroy(){
		connectionPoolMap.clear();
	}

	/**
	 * initial connection pools
	 * @throws Exception
	 */
	public void initialConnectionPools() throws Exception{
		Iterator<Entry<String,ResourcePool<Connection>>> iterator=connectionPoolMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,ResourcePool<Connection>> entry=iterator.next();
			ResourcePool<Connection> pool=entry.getValue();
			try{
				pool.initialize();
			}catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
	}
	
	/**
	 * <p>Method: get the connection pool</p>
	 * @param poolName
	 * @return ResourcePool<Connection>
	 */
	public static ResourcePool<Connection> getConnectionPool(String poolName){
		return connectionPoolMap.get(poolName);
	}

	/**
	 * @return the connectionPoolMap
	 */
	public static Map<String, ResourcePool<Connection>> getConnectionPoolMap() {
		return connectionPoolMap;
	}
}
