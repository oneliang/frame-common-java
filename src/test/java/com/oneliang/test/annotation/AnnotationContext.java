package com.oneliang.test.annotation;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class AnnotationContext {

	public static void main(String[] args) throws Exception{
		long begin=System.currentTimeMillis();
		String classPath=System.getProperty("user.dir")+"/bin";
		System.out.println(classPath);
		File classPathFile=new File(classPath);
		Queue<File> queue=new ConcurrentLinkedQueue<File>();
		queue.add(classPathFile);
		while(!queue.isEmpty()){
			File file=queue.poll();
			String filename=file.getName();
			if(file.isDirectory()){
				File[] fileList=file.listFiles();
				if(fileList!=null){
					for(File subFile:fileList){
						queue.add(subFile);
					}
				}
			}else if(file.isFile()){
				if(filename.endsWith(".class")){
					String path=file.getAbsolutePath();
					String classString=path.substring(classPathFile.getAbsolutePath().length()+1,path.length()-".class".length()).replace(File.separator, ".");
					System.out.println(classString);
					Class<?> clazz=ClassLoader.getSystemClassLoader().loadClass(classString);
					System.out.println(clazz);
				}
			}
		}
		System.out.println(System.currentTimeMillis()-begin);
	}
}
