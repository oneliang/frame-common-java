package com.oneliang.test.orm;

import java.util.List;

public interface BaseDatabaseOperate {

    /**
     * create database
     * 
     * @param databaseName
     */
    public abstract void createDatabase(String databaseName);

    public abstract void createTable(String tableName, List<Column> columnList);

    public static class Column {
        public final String name;
        public final boolean isId;

        public Column(String name, boolean isId) {
            this.name = name;
            this.isId = isId;
        }
    }
}
