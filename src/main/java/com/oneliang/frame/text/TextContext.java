package com.oneliang.frame.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oneliang.Constants;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.StringUtil;

public class TextContext extends AbstractContext{

	private static final String TEXT_SUFFIX=".txt";
	private static final Map<String,List<Option>> textMap=new ConcurrentHashMap<String,List<Option>>();
	private static final Map<String,Map<String,Option>> textOptionMap=new ConcurrentHashMap<String,Map<String,Option>>();
	private static final Map<String,File> fileMap=new ConcurrentHashMap<String, File>();

	/**
	 * <p>
	 * Method:initialize
	 * </p>
	 * 
	 * @param parameters
	 * @throws Exception
	 */
	public void initialize(final String parameters) {
		try{
			String path=parameters;
			String tempClassesRealPath=classesRealPath;
			if(tempClassesRealPath==null){
				tempClassesRealPath=this.classLoader.getResource(StringUtil.BLANK).getPath();
			}
			path=tempClassesRealPath+path;
			File rootFilePath=new File(path);
			Queue<File> queue=new ConcurrentLinkedQueue<File>();
			queue.add(rootFilePath);
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
					if(filename.endsWith(TEXT_SUFFIX)){
						String key=filename.substring(0, filename.lastIndexOf(TEXT_SUFFIX));
						BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file),Constants.Encoding.UTF8));
						String line=null;
						List<Option> optionList=new ArrayList<Option>();
						Map<String,Option> optionMap=new HashMap<String,Option>();
						while((line=bufferedReader.readLine())!=null){
							String[] arrays=line.split(Constants.Symbol.EQUAL);
							if(arrays.length>=2){
								Option option=new Option();
								option.setLabel(arrays[0]);
								option.setValue(arrays[1]);
								String[] otherInformation=new String[arrays.length-2];
								System.arraycopy(arrays,2,otherInformation,0,otherInformation.length);
								option.setOtherInformation(otherInformation);
								optionList.add(option);
								optionMap.put(option.getLabel(),option);
							}
						}
						bufferedReader.close();
						if(optionMap.size()>0){
							textOptionMap.put(key,optionMap);
						}
						if(optionList.size()>0){
							textMap.put(key,optionList);
						}
						fileMap.put(key, file);
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * destroy
	 */
	public void destroy(){
		textMap.clear();
		textOptionMap.clear();
		fileMap.clear();
	}

	/**
	 * get option list
	 * @param key
	 * @return List<Option>
	 */
	public static List<Option> getOptionList(String key){
		return textMap.get(key);
	}

	/**
	 * get option map
	 * @param key
	 * @return Map<String,Option>
	 */
	public static Map<String,Option> getOptionMap(String key){
		return textOptionMap.get(key);
	}

	/**
	 * get file
	 * @param key
	 * @return File
	 */
	public static File getFile(String key){
		return fileMap.get(key);
	}

	public static class Option{

		private String label=null;
		private String value=null;
		private String[] otherInformation=null;
		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
		/**
		 * @param label the label to set
		 */
		public void setLabel(String label) {
			this.label = label;
		}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		/**
		 * @return the otherInformation
		 */
		public String[] getOtherInformation() {
			return otherInformation;
		}
		/**
		 * @param otherInformation the otherInformation to set
		 */
		public void setOtherInformation(String[] otherInformation) {
			this.otherInformation = otherInformation;
		}
	}
}
