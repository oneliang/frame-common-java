package com.oneliang.test.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class TestClassLoader extends URLClassLoader{

	static String classAPath="file:/D:/haha/";
	static String classBPath="file:/D:/";
	static String classAFile="com.lwx.frame.A";
	static String classBFile="com.lwx.frame.B";

	ChildClassLoader childClassLoader=null;
//	Map<String,Class<?>> loadedClassMap=new HashMap<String,Class<?>>();

	public TestClassLoader(URL[] urls) {
		super(urls);
	}
	
	public TestClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> clazz=null;
		try{
//			if(this.loadedClassMap.containsKey(name)){
//				clazz=this.loadedClassMap.get(name);
//			}else{
				System.out.println("ParentClassLoader:"+name);
				clazz=super.loadClass(name);
//				loadedClassMap.put(name, clazz);//only cache class loaded by this loader
//			}
		}catch(Exception e){
			try {
				if(this.childClassLoader==null){
					this.childClassLoader = new ChildClassLoader(new URL[]{new URL(classBPath)});
					this.childClassLoader.parentClassLoader=this;
				}
			} catch (MalformedURLException malformedURLException) {
				malformedURLException.printStackTrace();
			}
//			if(this.childClassLoader.loadedClassMap.containsKey(name)){
//				clazz=this.childClassLoader.loadedClassMap.get(name);
//			}else{
				clazz=this.childClassLoader.loadClass(name);
//			}
		}
		return clazz;
	}
	
	public static void main(String[] args){
		System.out.println(System.getProperty("user.dir").replace("\\", "/"));
		
		try {
			TestClassLoader loaderA=new TestClassLoader(new URL[]{new URL(classAPath)});
			
			Class<?> classA=loaderA.loadClass(classAFile);
			Object objectA=classA.newInstance();
			System.out.println(objectA.getClass().getClassLoader());
			Object objectB=objectA.getClass().getMethod("getB",new Class<?>[]{}).invoke(objectA, new Object[]{});
			System.out.println(objectB.getClass().getClassLoader());
//			Class.forName(className)
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
