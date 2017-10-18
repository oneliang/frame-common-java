package com.oneliang.test.orm;

public interface DatabaseMapper {

    /**
     * get type
     * 
     * @param clazz
     * @return String
     */
    public abstract String getType(Class<?> clazz);
}
