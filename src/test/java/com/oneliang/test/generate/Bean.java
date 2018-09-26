package com.oneliang.test.generate;

import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.util.generate.Template;
import com.oneliang.util.generate.Template.Parameter;

public class Bean {

    private String packageName = null;
    private String className = null;
    private List<Field> fieldList = null;

    public static class Field {
        private String type = null;
        private String name = null;
        private String value = null;

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value
         *            the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

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

    /**
     * @return the fieldList
     */
    public List<Field> getFieldList() {
        return fieldList;
    }

    /**
     * @param fieldList
     *            the fieldList to set
     */
    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public static void main(String[] args) {
        final String templateFile = "/D:/Dandelion/java/workspace/frame-common-java/src/com/lwx/test/generate/bean.tmpl";
        final String toDirectory = "/D:/Dandelion/java/workspace/frame-common-java/src/";
        Template template = new Template();
        Parameter parameter = new Parameter();
        Bean bean = new Bean();
        bean.setClassName("TestBean");
        bean.setPackageName("com.lwx.test.generate");
        List<Field> fieldList = new ArrayList<Field>();
        Field field = new Field();
        field.setType("Integer");
        field.setName("id");
        field.setValue("null");
        fieldList.add(field);
        field = new Field();
        field.setType("String");
        field.setName("name");
        field.setValue("null");
        fieldList.add(field);
        bean.setFieldList(fieldList);
        parameter.setTemplateFile(templateFile);
        parameter.setToFile(toDirectory + bean.getPackageName().replace(Constants.Symbol.DOT, Constants.Symbol.SLASH_LEFT) + Constants.Symbol.SLASH_LEFT + bean.getClassName() + ".java");
        parameter.setObject(bean);
        System.out.println(parameter.getToFile());
        template.generate(parameter);
    }
}
