package com.oneliang.test.orm;

import java.sql.Connection;

import com.oneliang.frame.jdbc.BaseQuery;
import com.oneliang.frame.jdbc.BaseQueryImpl;
import com.oneliang.frame.jdbc.ConnectionSource;

public class MySqlDatabaseOperator extends DatabaseOperator {

    private BaseQuery baseQuery = new BaseQueryImpl();
    private ConnectionSource connectionSource = null;

    public void createDatabase(String databaseName) {
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        Connection connection = null;
        try {
            connection = connectionSource.getResource();
            if (connection != null) {
                baseQuery.executeBySql(connection, sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param connectionSource
     *            the connectionSource to set
     */
    public void setConnectionSource(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }
}
