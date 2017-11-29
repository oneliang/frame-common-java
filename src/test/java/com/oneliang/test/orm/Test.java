package com.oneliang.test.orm;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constant;
import com.oneliang.frame.jdbc.ConnectionSource;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.generate.Template;
import com.oneliang.util.json.DefaultJsonProcessor;
import com.oneliang.util.json.JsonUtil;
import com.oneliang.util.logging.BaseLogger;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class Test {

    private static void createDao(Class<?> daoClass) throws Exception {

        if (daoClass == null) {
            return;
        }
        if (!daoClass.isInterface()) {
            return;
        }
        Method[] methods = daoClass.getDeclaredMethods();
        List<DaoBean.Method> methodList = new ArrayList<DaoBean.Method>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Dao.Query.class)) {
                Dao.Query[] queryAnnotationArray = method.getAnnotationsByType(Dao.Query.class);
                if (queryAnnotationArray == null || queryAnnotationArray.length == 0) {
                    continue;
                }
                String sql = queryAnnotationArray[0].value();
                String name = method.getName();
                Parameter[] parameterArray = method.getParameters();
                if (parameterArray == null || parameterArray.length == 0) {
                    continue;
                }
                String returnType = method.getGenericReturnType().toString();
                List<String> parameterList = new ArrayList<String>();
                for (Parameter parameter : parameterArray) {
                    parameterList.add(parameter.getParameterizedType().toString());
                }
                methodList.add(new DaoBean.Method(sql, name, returnType, parameterList));
            }
        }
        String projectRealPath = new File(StringUtil.BLANK).getAbsolutePath();
        Template template = new Template();
        Template.Parameter parameter = new Template.Parameter();
        DaoBean daoBean = new DaoBean();
        daoBean.setMethodList(methodList);
        daoBean.setPackageName("com.oneliang.test.orm");
        daoBean.setClassName("UserDaoImpl");
        String templateFile = projectRealPath + Constant.Symbol.SLASH_LEFT + "src/test/java/com/oneliang/test/orm/dao.tmpl";
        String toFile = projectRealPath + Constant.Symbol.SLASH_LEFT + "src/test/java/" + daoBean.getPackageName().replace(Constant.Symbol.DOT, Constant.Symbol.SLASH_LEFT) + Constant.Symbol.SLASH_LEFT + daoBean.getClassName() + Constant.Symbol.DOT + Constant.File.JAVA;
        parameter.setTemplateFile(templateFile);
        parameter.setToFile(toFile);
        parameter.setObject(daoBean);
        parameter.setJsonProcessor(new DefaultJsonProcessor() {
            public <T> String process(Class<?> clazz, String fieldName, Object value, boolean ignoreFirstLetterCase) {
                if (clazz != null && clazz.equals(DaoBean.Method.class) && fieldName.equals("parameterList") && value != null && value instanceof List) {
                    List<Object> valueList = (List<Object>) value;
                    return JsonUtil.baseArrayToJson(valueList.toArray(new Object[] {}));
                }
                return super.process(clazz, fieldName, value, ignoreFirstLetterCase);
            }
        });
        template.generate(parameter);
    }

    public static void main(String[] args) throws Exception {
        LoggerManager.registerLogger("*", new BaseLogger(Logger.Level.DEBUG));

        createDao(UserDao.class);
        System.exit(0);
        ConnectionSource connectionSource = new ConnectionSource();
        connectionSource.setConnectionSourceName("orm");
        connectionSource.setDriver("com.mysql.jdbc.Driver");
        connectionSource.setUser("root");
        connectionSource.setPassword("123456");
        connectionSource.setUrl("jdbc:mysql://localhost:3306/mysql?useUnicode=true&amp;characterEncoding=UTF-8&amp;useCursorFetch=true&amp;defaultFetchSize=100");
        MySqlDatabaseOperator mySqlDatabaseOperator = new MySqlDatabaseOperator();
        mySqlDatabaseOperator.setConnectionSource(connectionSource);

        mySqlDatabaseOperator.createDatabase(OrmDatabase.class);
    }
}
