package com.oneliang.frame.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oneliang.Constants;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.file.FileUtil;

public class MessageContext extends AbstractContext {

	private static final char FILE_PATH='/';
	private static final char UNDERLINE='_';
	private static final String PARAMETER_PATH="-P=";
	private static final String PARAMETER_RECURSION="-R";
	private static final String PARAMETER_FILE="-F=";

	protected static final Map<String,Properties> messagePropertiesMap=new ConcurrentHashMap<String,Properties>();

	public void initialize(final String parameters) {
		try{
			String[] parameterArray=parameters.split(Constants.Symbol.COMMA);
			if(parameterArray!=null){
				boolean isRecursion=false;
				String directoryPath=null;
				String matchPatternName=null;
				for(String parameter:parameterArray){
					if(parameter.equals(PARAMETER_RECURSION)){
						isRecursion=true;
					}else if(parameter.startsWith(PARAMETER_PATH)){
						directoryPath=parameter.replaceFirst(PARAMETER_PATH,StringUtil.BLANK);
					}else if(parameter.startsWith(PARAMETER_FILE)){
						matchPatternName=parameter.replaceFirst(PARAMETER_FILE,StringUtil.BLANK);
					}
				}
				String fullDirectoryPath=classesRealPath!=null?(classesRealPath+directoryPath):directoryPath;
				File directoryFile=new File(fullDirectoryPath);
				loadPropertiesFile(directoryFile,matchPatternName,isRecursion);
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * destroy
	 */
	public void destroy(){
		messagePropertiesMap.clear();
	}

	/**
	 * loadPropertiesFile,none recursion version
	 * @param directoryFile
	 * @param matchPatternName
	 * @param isRecursion
	 * @throws IOException 
	 */
	private void loadPropertiesFile(final File directoryFile,final String matchPatternName,final boolean isRecursion) throws IOException{
		Queue<File> queue=new ConcurrentLinkedQueue<File>();
		queue.add(directoryFile);
		boolean sign=false;
		while(!queue.isEmpty()){
			File file=queue.poll();
			String filename=file.getName();
			if(file.isDirectory()&&!sign){
				File[] fileList=file.listFiles();
				if(fileList!=null){
					for(File subFile:fileList){
						queue.add(subFile);
					}
					//is not recursion
					if(!isRecursion){
						sign=true;
					}
				}
			}else if(file.isFile()){
				if(StringUtil.isMatchPattern(filename,matchPatternName)){
					Properties properties=FileUtil.getProperties(file);
					if(properties!=null){
						String key=filename.substring(filename.indexOf(UNDERLINE)+1, filename.lastIndexOf(Constants.Symbol.DOT));
						if(messagePropertiesMap.containsKey(key)){
							Properties messageProperties=messagePropertiesMap.get(key);
							messageProperties.putAll(properties);
						}else{
							messagePropertiesMap.put(key, properties);
						}
					}
				}
			}
		}
	}

	/**
	 * recursion version
	 * @param directoryFile
	 * @param directoryPath
	 * @param matchPatternName
	 * @param isRecursion
	 * @throws Exception
	 */
	private void loadPropertiesFile(File directoryFile,String directoryPath,String matchPatternName,boolean isRecursion) throws Exception{
		if(directoryFile.isDirectory()){
			File[] fileList=directoryFile.listFiles();
			if(fileList!=null){
				for(File file:fileList){
					String filename=file.getName();
					if(file.isFile()){
						if(StringUtil.isMatchPattern(filename,matchPatternName)){
							Properties properties=FileUtil.getProperties(directoryPath+filename);
							if(properties!=null){
								String key=filename.substring(filename.indexOf(UNDERLINE)+1, filename.lastIndexOf(Constants.Symbol.DOT));
								if(messagePropertiesMap.containsKey(key)){
									Properties messageProperties=messagePropertiesMap.get(key);
									messageProperties.putAll(properties);
								}else{
									messagePropertiesMap.put(key, properties);
								}
							}
						}
					}else{
						if(isRecursion){
							loadPropertiesFile(file,directoryPath+filename+FILE_PATH,matchPatternName,isRecursion);
						}
					}
				}
			}
		}
	}

	/**
	 * get message properties
	 * @param localeKey
	 * @return Properties
	 */
	public static Properties getMessageProperties(String localeKey){
		return messagePropertiesMap.get(localeKey);
	}
}