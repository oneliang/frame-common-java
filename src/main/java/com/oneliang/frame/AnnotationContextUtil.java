package com.oneliang.frame;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oneliang.Constant;
import com.oneliang.exception.FileLoadException;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.jar.JarClassLoader;
import com.oneliang.util.jar.JarUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public final class AnnotationContextUtil{

	private static final Logger logger=LoggerManager.getLogger(AnnotationContextUtil.class);

	private static final String SIGN_ROOT="$ROOT";
	private static final String PARAMETER_TYPE="-T=";
	private static final String PARAMETER_PACKAGE="-P=";
	private static final String PARAMETER_FILE="-F=";

	/**
	 * <p>
	 * Method:use for AnnotationActionContext,AnnotationIocContext,AnnotationInterceptorContext,AnnotationMappingContext
	 * </p>
	 * 
	 * @param parameters
	 * @param classLoader
	 * @param classesRealPath
	 * @param jarClassLoader
	 * @param projectRealPath
	 * @param annotationClass
	 * @return List<Class<?>>
	 * @throws ClassNotFoundException
	 * @throws FileLoadException
	 */
	public static List<Class<?>> parseAnnotationContextParameter(final String parameters, final ClassLoader classLoader, String classesRealPath, final JarClassLoader jarClassLoader, final String projectRealPath, final Class<? extends Annotation> annotationClass) throws ClassNotFoundException,FileLoadException{
		List<Class<?>> classList=null;
		String[] parameterArray=parameters.split(Constant.Symbol.COMMA);
		if(parameterArray!=null){
			if(parameterArray.length==1){
				String path=parameters;
				String tempClassesRealPath=classesRealPath;
				if(tempClassesRealPath==null){
					tempClassesRealPath=classLoader.getResource(StringUtil.BLANK).getPath();
				}
				path=tempClassesRealPath+path;
				classList=searchClassList(tempClassesRealPath,path,annotationClass);
			}else{
				String type=null;
				String packageName=null;
				String file=null;
				for(String parameter:parameterArray){
					if(parameter.startsWith(PARAMETER_TYPE)){
						type=parameter.replaceFirst(PARAMETER_TYPE, StringUtil.BLANK);
					}else if(parameter.startsWith(PARAMETER_PACKAGE)){
						packageName=parameter.replaceFirst(PARAMETER_PACKAGE, StringUtil.BLANK);
					}else if(parameter.startsWith(PARAMETER_FILE)){
						file=parameter.replaceFirst(PARAMETER_FILE, StringUtil.BLANK);
					}
				}
				if(type!=null&&type.equalsIgnoreCase(Constant.File.JAR)){
					if(file.startsWith(SIGN_ROOT)){
						file=file.replace(SIGN_ROOT, projectRealPath);
					}
					classList=JarUtil.searchClassList(jarClassLoader, file, packageName, annotationClass);
				}
			}
		}
		return classList;
	}

	/**
	 * search class list
	 * @param classesRealPath
	 * @param searchClassPath
	 * @param annotationClass
	 * @return List<Class<?>>
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> searchClassList(final String classesRealPath,final String searchClassPath,final Class<? extends Annotation> annotationClass) throws ClassNotFoundException{
		final List<Class<?>> classList=new ArrayList<Class<?>>();
		File classesRealPathFile=new File(classesRealPath);
		File classPathFile=new File(searchClassPath);
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
				if(filename.endsWith(Constant.Symbol.DOT+Constant.File.CLASS)){
					String filePath=file.getAbsolutePath();
					String className=filePath.substring(classesRealPathFile.getAbsolutePath().length()+1,filePath.length()-(Constant.Symbol.DOT+Constant.File.CLASS).length()).replace(File.separator, Constant.Symbol.DOT);
					Class<?> clazz=Thread.currentThread().getContextClassLoader().loadClass(className);
					if(clazz.isAnnotationPresent(annotationClass)){
						logger.info(clazz);
						classList.add(clazz);
					}
				}
			}
		}
		return classList;
	}
}
