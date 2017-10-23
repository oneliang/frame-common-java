package com.oneliang.test.orm;

import java.util.List;

import com.oneliang.test.orm.Entity.Column;

public interface BaseDatabaseOperate {

    /**
     * create database
     * 
     * @param databaseName
     */
    public abstract void createDatabase(String databaseName);

    /**
     * create table
     * 
     * @param tableName
     * @param columnWrapperList
     */
    public abstract void createTable(String tableName, List<ColumnWrapper> columnWrapperList);

    public static class ColumnWrapper {
        public final String fieldName;
        public final Column column;

        public ColumnWrapper(String fieldName, Column column) {
            this.fieldName = fieldName;
            this.column = column;
        }
    }

}
