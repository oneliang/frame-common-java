package com.oneliang.test.orm;

import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.ClassUtil.ClassType;

public class MySqlDatabaseMapper implements DatabaseMapper {

    /**
     * get type
     * 
     * @param clazz
     * @return String
     */
    public String getType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        ClassType classType = ClassUtil.getClassType(clazz);
        String value = null;
        if (classType != null) {
            switch (classType) {
            case JAVA_LANG_STRING:
                value = "VARCHAR";
                break;
            case INT:
            case JAVA_LANG_INTEGER:
                value = "INT";
                break;
            default:
                break;
            }
        }
        return value;
    }
}
