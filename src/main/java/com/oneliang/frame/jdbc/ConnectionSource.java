package com.oneliang.frame.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

import com.oneliang.Constant;
import com.oneliang.util.log.Logger;
import com.oneliang.util.resource.ResourceSource;

/**
 * ConnectionSource bean describe the database detail,include driver,url,user,password
 * four important property
 * 
 * @author Dandelion
 * @since 2008-08-22
 */
public class ConnectionSource extends ResourceSource<Connection>{

	private static final Logger logger=Logger.getLogger(ConnectionSource.class);

	public static final String CONNECTION_SOURCE_NAME="connectionSourceName";
	public static final String DRIVER="driver";
	public static final String URL="url";
	public static final String USER="user";
	public static final String PASSWORD="password";

	/**
	 * data base properties
	 */
	private String connectionSourceName=null;
	private String driver = null;
	private String url = null;
	private String user = null;
	private String password = null;

	/**
	 * @return the connectionSourceName
	 */
	public String getConnectionSourceName() {
		return connectionSourceName;
	}

	/**
	 * @param connectionSourceName the connectionSourceName to set
	 */
	public void setConnectionSourceName(String connectionSourceName) {
		this.connectionSourceName = connectionSourceName;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
		if(this.driver!=null){
			try {
				Thread.currentThread().getContextClassLoader().loadClass(this.driver).newInstance();
			} catch (Exception e) {
				logger.error(Constant.Base.EXCEPTION, e);
			}
		}
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * <p>
	 * Method: initial the connection operate,load the config file or use the
	 * default file
	 * </p>
	 * 
	 * This method initial the config file
	 */
	public synchronized Connection getResource(){
		Connection connection=null;
		try {
			connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (Exception e) {
			logger.error(Constant.Base.EXCEPTION, e);
		}
		return connection;
	}
}
