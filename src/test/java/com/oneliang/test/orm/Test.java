package com.oneliang.test.orm;

import com.oneliang.frame.jdbc.ConnectionSource;
import com.oneliang.util.logging.BaseLogger;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class Test {

    public static void main(String[] args) throws Exception {
        LoggerManager.registerLogger("*", new BaseLogger(Logger.Level.DEBUG));
        ConnectionSource connectionSource = new ConnectionSource();
        connectionSource.setConnectionSourceName("orm");
        connectionSource.setDriver("com.mysql.jdbc.Driver");
        connectionSource.setUser("root");
        connectionSource.setPassword("123456");
        connectionSource.setUrl("jdbc:mysql://localhost:3306/mysql?useUnicode=true&amp;characterEncoding=UTF-8&amp;useCursorFetch=true&amp;defaultFetchSize=100");
        MySqlDatabaseOperator mySqlDatabaseOperator = new MySqlDatabaseOperator();
        mySqlDatabaseOperator.setConnectionSource(connectionSource);

        mySqlDatabaseOperator.createDatabase(OrmDatabase.class);
    }
}
