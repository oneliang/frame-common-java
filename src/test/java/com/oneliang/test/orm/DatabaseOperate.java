package com.oneliang.test.orm;

public interface DatabaseOperate extends BaseDatabaseOperate {
    /**
     * create database
     * 
     * @param clazz
     */
    public abstract void createDatabase(Class<?> clazz);

    /**
     * create database
     * 
     * @param clazz
     * @param databaseName
     */
    public abstract void createDatabase(Class<?> clazz, final String databaseName);

    /**
     * create table
     * 
     * @param clazz
     */
    public abstract void createTable(Class<?> clazz);

    /**
     * create table
     * 
     * @param clazz
     * @param tableName
     */
    public abstract void createTable(Class<?> clazz, final String tableName);

    /**
     * destroy database
     * 
     * @param clazz
     */
    public abstract void destroyDatabase(Class<?> clazz);
}
