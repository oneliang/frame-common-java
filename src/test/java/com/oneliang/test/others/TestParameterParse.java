package com.oneliang.test.others;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.StringUtil;

public class TestParameterParse {

	public String getString(){
		return "test";
	}
	
	public String test(String a,Integer b){
		return a+b;
	}
	Integer a=new Integer(0);
	public int a(){
		try{
			return a++;
		}finally{
			System.out.println((a+=2)+"--finally");
		}
	}
	/**
	 * System.out.println(Character.TYPE.equals(char.class));
	 * System.out.println(Byte.TYPE.equals(byte.class));
	 * System.out.println(Short.TYPE.equals(short.class));
	 * System.out.println(Integer.TYPE.equals(int.class));
	 * System.out.println(Long.TYPE.equals(long.class));
	 * System.out.println(Float.TYPE.equals(float.class));
	 * System.out.println(Double.TYPE.equals(double.class));
	 * System.out.println(Boolean.TYPE.equals(boolean.class));
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		TestParameterParse test=new TestParameterParse();
		System.out.println(test.a());
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		String paramString="class=com.lwx.test.loader.Test&method=test(java.lang.String=a,java.lang.Integer=10)";
		String[] stringArray=paramString.split("&");
		Map<String,String> map=new HashMap<String,String>();
		for(String string:stringArray){
			int equalIndex=string.indexOf('=');
			if(equalIndex>0){
				String key=string.substring(0, equalIndex);
				String value=string.substring(equalIndex+1,string.length());
				map.put(key, value);
			}
		}
		Set<String> set=map.keySet();
		for(String key:set){
			String string=map.get(key);
			System.out.println(key+"--"+string);
		}
		String clazz=map.get("class");
		String method=map.get("method");
		String methodName=method.replaceAll("\\([\\s|\\S]*\\)", "");
		String methodParameterString=method.substring(methodName.length(),method.length());
		methodParameterString=methodParameterString.replace("(", "").replace(")", "");
		Class<?>[] methodParameterClassArray=null;
		Object[] methodParameterValueArray=null;
		if(StringUtil.isNotBlank(methodParameterString)){
			String[] methodParameterArray=methodParameterString.split(",");
			methodParameterClassArray=new Class<?>[methodParameterArray.length];
			methodParameterValueArray=new Object[methodParameterArray.length];
			int i=0;
			for(String methodParameter:methodParameterArray){
				int equalIndex=methodParameter.indexOf('=');
				if(equalIndex>0){
					String methodParameterClassName=methodParameter.substring(0, equalIndex);
					Class<?> methodParameterClass=null;
					if(methodParameterClassName.equals(char.class.getName())){
						methodParameterClass=Character.TYPE;
					}else if(methodParameterClassName.equals(byte.class.getName())){
						methodParameterClass=Byte.TYPE;
					}else if(methodParameterClassName.equals(short.class.getName())){
						methodParameterClass=Short.TYPE;
					}else if(methodParameterClassName.equals(int.class.getName())){
						methodParameterClass=Integer.TYPE;
					}else if(methodParameterClassName.equals(long.class.getName())){
						methodParameterClass=Long.TYPE;
					}else if(methodParameterClassName.equals(float.class.getName())){
						methodParameterClass=Float.TYPE;
					}else if(methodParameterClassName.equals(double.class.getName())){
						methodParameterClass=Double.TYPE;
					}else if(methodParameterClassName.equals(boolean.class.getName())){
						methodParameterClass=Boolean.TYPE;
					}else{
						methodParameterClass=Thread.currentThread().getContextClassLoader().loadClass(methodParameterClassName);
					}
					
					methodParameterClassArray[i]=methodParameterClass;
					String methodParameterValueString=methodParameter.substring(equalIndex+1,methodParameter.length());
					Object methodParameterValue=ClassUtil.changeType(methodParameterClass, new String[]{methodParameterValueString});
					methodParameterValueArray[i]=methodParameterValue;
				}else{
					methodParameterClassArray[i]=Thread.currentThread().getContextClassLoader().loadClass(methodParameter);
					methodParameterValueArray[i]=null;
				}
				i++;
			}
		}
		Object object=Thread.currentThread().getContextClassLoader().loadClass(clazz).newInstance();
		Method objectMethod=object.getClass().getMethod(methodName,methodParameterClassArray);
		System.out.println(objectMethod.invoke(object, methodParameterValueArray));
	}
}
