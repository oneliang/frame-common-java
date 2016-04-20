package com.oneliang.test.annotation;

import java.lang.reflect.Method;


public class TestAnnotation {
	public static void main(String[] args){
		doAnnotationMethod(TestBean.class);
	}
	private static <T extends Object> void doAnnotationMethod(Class<T> clazz){
		try {
			Object object=clazz.newInstance();
			Method[] methods=clazz.getMethods();
			for(Method method:methods){
				if(method.isAnnotationPresent(Test.class)){
					System.out.println(method.invoke(object, new Object[]{}));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
