package com.oneliang.test.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class ChildClassLoader extends URLClassLoader {

	TestClassLoader parentClassLoader=null;
//	Map<String,Class<?>> loadedClassMap=new HashMap<String,Class<?>>();

	public ChildClassLoader(URL[] urls) {
		super(urls);
	}
	
	public ChildClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> clazz=null;
		try{
//			if(this.loadedClassMap.containsKey(name)){
//				clazz=this.loadedClassMap.get(name);
//			}else{
				clazz=super.loadClass(name);
//				System.out.println("ChildClassLoader:"+name);
//				loadedClassMap.put(name, clazz);//only cache class loaded by this loader
//			}
		}catch(Exception e){
//			if(this.parentClassLoader.loadedClassMap.containsKey(name)){
//				clazz=this.parentClassLoader.loadedClassMap.get(name);
//				System.out.println("ChildClassLoader:"+clazz);
//			}
			clazz=this.parentClassLoader.loadClass(name);
		}
		return clazz;
	}
}
