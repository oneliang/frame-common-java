package com.oneliang.frame.autogen.protobuf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.oneliang.Constants;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.autogen.AutoGenerateContext;
import com.oneliang.frame.autogen.protobuf.ProtobufTemplateBean.Field;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.generate.Template.Parameter;

public class ProtobufAutoGenerateContext extends AutoGenerateContext {

    private static final String PARAMETER_TO_FOLDER = "-TO=";
    private static final String PARAMETER_TEMPLATE_FILE = "-T=";
    private static final String PARAMETER_PROTO_FILE = "-P=";
    private static final String PACKAGE = "package";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_REGEX = MESSAGE + "\\s+(\\w+)\\s*\\{";
    private static final String FIELD_REGEX = "(\\w+)\\s+(\\w+)\\s+(\\w+)\\s*=\\s*\\d+";

    public void initialize(String parameters) {
        try {
            String[] parameterArray = parameters.split(Constants.Symbol.COMMA);
            String toFolder = null;
            String templateFile = null;
            String protoFile = null;
            if (parameterArray != null) {
                for (String parameter : parameterArray) {
                    if (parameter.startsWith(PARAMETER_TO_FOLDER)) {
                        toFolder = parameter.replaceFirst(PARAMETER_TO_FOLDER, StringUtil.BLANK);
                    } else if (parameter.startsWith(PARAMETER_TEMPLATE_FILE)) {
                        templateFile = parameter.replaceFirst(PARAMETER_TEMPLATE_FILE, StringUtil.BLANK);
                    } else if (parameter.startsWith(PARAMETER_PROTO_FILE)) {
                        protoFile = parameter.replaceFirst(PARAMETER_PROTO_FILE, StringUtil.BLANK);
                    }
                }
            }
            String path = protoFile;
            String tempClassesRealPath = classesRealPath;
            if (tempClassesRealPath == null) {
                tempClassesRealPath = Thread.currentThread().getContextClassLoader().getResource(StringUtil.BLANK).getPath();
            }
            path = tempClassesRealPath + path;
            InputStream inputStream = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            Map<String, ProtobufTemplateBean> protobufTemplateBeanMap = new HashMap<String, ProtobufTemplateBean>();
            String packageName = null;
            ProtobufTemplateBean protobufTemplateBean = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtil.isNotBlank(line)) {
                    if (line.startsWith(PACKAGE)) {
                        packageName = line.replace(PACKAGE, StringUtil.BLANK).replace(Constants.Symbol.SEMICOLON, StringUtil.BLANK).trim();
                    } else if (StringUtil.isMatchRegex(line, MESSAGE_REGEX)) {
                        List<String> groupList = StringUtil.parseRegexGroup(line.trim(), MESSAGE_REGEX);
                        if (groupList != null && groupList.size() == 1) {
                            String className = groupList.get(0);
                            protobufTemplateBean = new ProtobufTemplateBean();
                            protobufTemplateBean.setClassName(className);
                            protobufTemplateBeanMap.put(className, protobufTemplateBean);
                        }
                    } else if (StringUtil.isMatchRegex(line, FIELD_REGEX)) {
                        List<String> groupList = StringUtil.parseRegexGroup(line.trim(), FIELD_REGEX);
                        if (protobufTemplateBean != null && groupList != null && groupList.size() == 3) {
                            String protobufType = groupList.get(0);
                            Field field = new Field();
                            if (protobufType.equals("repeated")) {
                                String protobufFieldType = groupList.get(1);
                                TypeValue javaType = changeToJavaType(protobufFieldType, true);
                                field.setType("List<" + javaType.type + ">");
                                field.setRawType(javaType.type);
                                field.setName(groupList.get(2));
                                field.setValue("new java.util.ArrayList<" + javaType.type + ">()");
                                protobufTemplateBean.setNeedList(true);
                            } else {
                                String protobufFieldType = groupList.get(1);
                                TypeValue javaType = changeToJavaType(protobufFieldType, false);
                                String type = javaType.type;
                                String value = javaType.value;
                                field.setType(type);
                                field.setName(groupList.get(2));
                                field.setValue(value);
                                if (javaType.needList) {
                                    protobufTemplateBean.setNeedList(true);
                                }
                            }
                            protobufTemplateBean.addField(field);
                        }
                    }
                }
            }
            Iterator<Entry<String, ProtobufTemplateBean>> iterator = protobufTemplateBeanMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, ProtobufTemplateBean> protobufTemplateBeanEntry = iterator.next();
                protobufTemplateBean = protobufTemplateBeanEntry.getValue();
                protobufTemplateBean.setPackageName(packageName);
                protobufTemplateBean.setTemplateFile(templateFile);
                protobufTemplateBean.setToFolder(toFolder);
                Parameter parameter = new Parameter();
                parameter.setTemplateFile(projectRealPath + protobufTemplateBean.getTemplateFile());
                parameter.setObject(protobufTemplateBean);
                parameter.setToFile(projectRealPath + protobufTemplateBean.getToFolder() + Constants.Symbol.SLASH_LEFT + protobufTemplateBean.getPackageName().replace(Constants.Symbol.DOT, Constants.Symbol.SLASH_LEFT) + Constants.Symbol.SLASH_LEFT + protobufTemplateBean.getClassName() + Constants.Symbol.DOT + Constants.File.JAVA);
                template.generate(parameter);
            }
        } catch (Exception e) {
            throw new InitializeException(parameters, e);
        }
    }

    private TypeValue changeToJavaType(String protobufType, boolean rawType) {
        TypeValue javaType = null;
        if (protobufType != null) {
            javaType = new TypeValue();
            if (protobufType.equals("int32")) {
                javaType.type = rawType ? "Integer" : "int";
                javaType.value = String.valueOf(0);
            } else if (protobufType.equals("int64")) {
                javaType.type = rawType ? "Long" : "long";
                javaType.value = String.valueOf(0);
            } else if (protobufType.equals("string")) {
                javaType.type = "String";
                javaType.value = StringUtil.NULL;
            } else if (protobufType.equals("bytes")) {
                javaType.type = "com.lwx.protobuf.ByteString";
                javaType.value = StringUtil.NULL;
            } else if (protobufType.equals("bool")) {
                javaType.type = rawType ? "Boolean" : "boolean";
                javaType.value = String.valueOf(false);
            } else if (protobufType.equals("float")) {
                javaType.type = rawType ? "Float" : "float";
                javaType.value = "0f";
            } else if (protobufType.equals("double")) {
                javaType.type = rawType ? "Double" : "double";
                javaType.value = "0d";
            } else {
                javaType.type = protobufType;
                javaType.value = "new " + protobufType + "()";
                javaType.needList = true;
            }
        }
        return javaType;
    }

    private static class TypeValue {
        String type = null;
        String value = null;
        boolean needList = false;
    }

    public void destroy() {
    }
}
