package com.oneliang.test.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
        List<ColumnWrapper> columnWrapperList = new ArrayList<ColumnWrapper>();
        for (Field entityField : entityFieldArray) {
            Entity.Column[] columnAnnotationArray = entityField.getAnnotationsByType(Entity.Column.class);
            if (columnAnnotationArray == null || columnAnnotationArray.length == 0) {
                continue;
            }
            String fieldName = entityField.getName();
            Entity.Column columnAnnotation = columnAnnotationArray[0];
            columnWrapperList.add(new ColumnWrapper(fieldName, columnAnnotation));
        }
        this.createTable(table, columnWrapperList);
    }

    /**
     * destroy database
     * 
     * @param clazz
     */
    public void destroyDatabase(Class<?> clazz) {
    }

}
