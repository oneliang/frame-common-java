package com.oneliang.test.orm;

import java.sql.Connection;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.frame.jdbc.BaseQuery;
import com.oneliang.frame.jdbc.BaseQueryImpl;
import com.oneliang.frame.jdbc.ConnectionSource;
import com.oneliang.util.common.StringUtil;

public class MySqlDatabaseOperator extends DatabaseOperator {

    private BaseQuery baseQuery = new BaseQueryImpl();
    private ConnectionSource connectionSource = null;

    public void createDatabase(String databaseName) {
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        this.executeBatch(new String[]{sql});
    }

    /**
     * create table
     * 
     * @param tableName
     * @param columnWrapperList
     */
    public void createTable(String tableName, List<ColumnWrapper> columnWrapperList) {
        if (tableName == null || columnWrapperList == null) {
            logger.info("table name or column list is null.");
            return;
        }
        StringBuilder columnStringBuilder = new StringBuilder();
        StringBuilder primaryKeyStringBuilder = new StringBuilder();
        for (ColumnWrapper columnWrapper : columnWrapperList) {
            String fieldName = columnWrapper.fieldName;
            Entity.Column columnAnnotation = columnWrapper.column;
            boolean isId = columnAnnotation.isId();
            String columnName = columnAnnotation.name();
            if (StringUtil.isBlank(columnName)) {
                columnName = fieldName;
            }

            String condition = null;
            Entity.Column.Condition[] conditionAnnotationArray = columnAnnotation.condition();
            if (conditionAnnotationArray != null) {
                for (Entity.Column.Condition conditionAnnotation : conditionAnnotationArray) {
                    String key = conditionAnnotation.key();
                    String value = conditionAnnotation.value();
                    if (key != null && key.equals(Constants.Database.MYSQL)) {
                        condition = value;
                    }
                }
            }
            if (StringUtil.isBlank(condition)) {
                logger.error("condition is empty.");
                throw new NullPointerException(String.format("condition is empty,table:%s,field:%s,comlumn:%s", tableName, fieldName, columnName));
            }
            columnStringBuilder.append("");
            if (isId) {
                if (primaryKeyStringBuilder.length() > 0) {
                    primaryKeyStringBuilder.append(Constants.Symbol.COMMA);
                }
                primaryKeyStringBuilder.append(columnName);
            }
            columnStringBuilder.append(columnName + " " + condition + Constants.Symbol.COMMA);
            logger.info(String.format("Reading entity column,field name:%s,column name:%s", fieldName, columnName));
        }

        if (columnStringBuilder.length() > 0) {
            columnStringBuilder.delete(columnStringBuilder.length() - 1, columnStringBuilder.length());
        }
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE " + tableName + "(");
        sqlStringBuilder.append(columnStringBuilder);
        if (StringUtil.isNotBlank(primaryKeyStringBuilder.toString())) {
            sqlStringBuilder.append(",PRIMARY KEY(");
            sqlStringBuilder.append(primaryKeyStringBuilder);
            sqlStringBuilder.append(")");
        }
        sqlStringBuilder.append(")");
        logger.info(sqlStringBuilder);
        this.executeBatch(new String[] { "USE orm", sqlStringBuilder.toString() });
    }

    /**
     * execute batch
     * 
     * @param sqls
     */
    private void executeBatch(String[] sqls) {
        Connection connection = null;
        try {
            connection = connectionSource.getResource();
            if (connection != null) {
                baseQuery.executeBatch(connection, sqls);
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
