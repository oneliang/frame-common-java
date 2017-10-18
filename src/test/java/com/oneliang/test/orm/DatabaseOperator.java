package com.oneliang.test.orm;

import java.lang.reflect.Field;

import com.oneliang.Constant;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public abstract class DatabaseOperator implements DatabaseOperate {

    protected Logger logger = LoggerManager.getLogger(DatabaseOperator.class);

    /**
     * create database
     * 
     * @param clazz
     */
    public void createDatabase(Class<?> clazz) {
        this.createDatabase(clazz, null);
    }

    /**
     * create database
     * 
     * @param clazz
     * @param databaseName
     */
    public void createDatabase(Class<?> clazz, final String databaseName) {
        if (clazz == null) {
            return;
        }
        Database[] databaseAnnotationArray = clazz.getAnnotationsByType(Database.class);
        if (databaseAnnotationArray == null || databaseAnnotationArray.length == 0) {
            return;
        }
        Database databaseAnnotation = databaseAnnotationArray[0];
        this.createDatabase(databaseAnnotation, databaseName);
    }

    /**
     * create database
     * 
     * @param database
     * @param databaseName
     */
    private void createDatabase(Database database, String databaseName) {
        String databaseRealName = databaseName;
        if (StringUtil.isBlank(databaseRealName)) {
            databaseRealName = database.name();
        }
        if (StringUtil.isBlank(databaseRealName)) {
            throw new NullPointerException("database name can not be empty");
        }
        this.createDatabase(databaseRealName);
        Class<?>[] entityClassArray = database.entities();
        if (entityClassArray == null || entityClassArray.length == 0) {
            return;
        }
        for (Class<?> entityClass : entityClassArray) {
            this.createTable(entityClass);
        }

    }

    /**
     * create table
     * 
     * @param clazz
     */
    public void createTable(Class<?> clazz) {
        this.createTable(clazz, null);
    }

    /**
     * create table
     * 
     * @param clazz
     * @param tableName
     */
    public void createTable(Class<?> clazz, String tableName) {
        if (clazz == null) {
            return;
        }
        Entity[] entityAnnotationArray = clazz.getAnnotationsByType(Entity.class);
        if (entityAnnotationArray == null || entityAnnotationArray.length == 0) {
            return;
        }
        Entity entityAnnotation = entityAnnotationArray[0];
        String table = entityAnnotation.table();
        if (StringUtil.isBlank(table)) {
            table = clazz.getSimpleName();
        }
        Field[] entityFieldArray = clazz.getDeclaredFields();
        if (entityFieldArray == null || entityFieldArray.length == 0) {
            return;
        }
        logger.info(String.format("Reading entity:%s,table:%s", clazz, table));
        StringBuilder columnStringBuilder = new StringBuilder();
        StringBuilder primaryKeyStringBuilder = new StringBuilder();
        for (Field entityField : entityFieldArray) {
            Entity.Column[] columnAnnotationArray = entityField.getAnnotationsByType(Entity.Column.class);
            if (columnAnnotationArray == null || columnAnnotationArray.length == 0) {
                continue;
            }
            Entity.Column columnAnnotation = columnAnnotationArray[0];
            boolean isId = columnAnnotation.isId();
            String columnName = columnAnnotation.name();
            String fieldName = entityField.getName();
            if (StringUtil.isBlank(columnName)) {
                columnName = fieldName;
            }
            Class<?> fieldType = entityField.getType();
            String columnType = null;
            if (databaseMapper == null) {
                columnType = fieldType.getSimpleName();
            } else {
                columnType = databaseMapper.getType(fieldType);
            }
            columnStringBuilder.append("");
            if (isId) {
                if (primaryKeyStringBuilder.length() > 0) {
                    primaryKeyStringBuilder.append(Constant.Symbol.COMMA);
                }
                primaryKeyStringBuilder.append(columnName);
            }
            columnStringBuilder.append(columnName + " " + columnType + "(20),");
            logger.info(String.format("Reading entity column,field name:%s,field type:%s,column name:%s,column type:%s", fieldName, fieldType, columnName, columnType));
        }
        if (columnStringBuilder.length() > 0) {
            columnStringBuilder.delete(columnStringBuilder.length() - 1, columnStringBuilder.length());
        }
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("CREATE TABLE " + table + "(");
        sqlStringBuilder.append(columnStringBuilder);
        if (StringUtil.isNotBlank(primaryKeyStringBuilder.toString())) {
            columnStringBuilder.append(",PRIMARY KEY(");
            columnStringBuilder.append(primaryKeyStringBuilder);
            columnStringBuilder.append(")");
            sqlStringBuilder.append(columnStringBuilder);
        }
        sqlStringBuilder.append(")");
        logger.info(sqlStringBuilder);
    }

    /**
     * destroy database
     * 
     * @param clazz
     */
    public void destroyDatabase(Class<?> clazz) {
    }

}
