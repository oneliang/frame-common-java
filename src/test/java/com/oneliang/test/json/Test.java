package com.oneliang.test.json;

import java.lang.reflect.Method;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.json.JsonObject;
import com.oneliang.util.json.JsonUtil;

public class Test {

    public static void main(String[] args) throws Exception{
        String json="{id:100,name:'name'}";
        JsonObject jsonObject=new JsonObject(json);
        Method[] methods=TestPo.class.getMethods();
        Object object=TestPo.class.newInstance();
        for(Method method:methods){
            String methodName=method.getName();
            String fieldName=null;
            if(methodName.startsWith(Constants.Method.PREFIX_SET)){
                fieldName=ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName);
            }
            if(fieldName!=null){
                Class<?>[] classes=method.getParameterTypes();
                Object value=null;
                if(classes.length==1){
                    value=ClassUtil.changeType(classes[0], new String[]{jsonObject.get(fieldName).toString()});
                    method.invoke(object, value);
                }
            }
        }
        System.out.println(object);
        String array="[{id:1,name:'aa'},{id:2,name:'bb'}]";
        List<TestPo> list=JsonUtil.jsonToObjectList(array, TestPo.class);
        System.out.println(list.size());
        list.get(0).setName("aaabbb\"");
        list.get(0).setName(list.get(0).getName().replace("\"", "\\\""));
        System.out.println(list.get(0).getName());
        System.out.println(array=JsonUtil.iterableToJson(list));
        list=JsonUtil.jsonToObjectList(array, TestPo.class);
        System.out.println(list.size());
    }
}
