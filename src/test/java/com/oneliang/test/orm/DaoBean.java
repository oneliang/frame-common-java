package com.oneliang.test.orm;

import java.util.List;

public class DaoBean {

    private String packageName = null;
    private String className = null;
    private List<Method> methodList = null;

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName
     *            the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className
     *            the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    public static class Method {
        private String sql;
        private String name;
        private String returnType;
        private List<String> parameterList;

        public Method(String sql, String name, String returnType, List<String> parameterList) {
            this.sql = sql;
            this.name = name;
            this.returnType = returnType;
            this.parameterList = parameterList;
        }

        /**
         * @return the sql
         */
        public String getSql() {
            return sql;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the returnType
         */
        public String getReturnType() {
            return returnType;
        }

        /**
         * @return the parameterList
         */
        public List<String> getParameterList() {
            return parameterList;
        }
    }

    /**
     * @return the methodList
     */
    public List<Method> getMethodList() {
        return methodList;
    }

    /**
     * @param methodList the methodList to set
     */
    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }
}
