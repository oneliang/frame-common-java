package com.oneliang.frame.servlet.action;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.oneliang.util.common.RequestUtil;
import com.oneliang.util.common.StringUtil;

public class ActionForwardBean implements Cloneable {

	public static final String TAG_FORWARD="forward";
	
	private String name=null;
	private String path=null;
	private String staticParameters=null;
	private String staticFilePath=null;
	private Map<String,String[]> parameterMap=new ConcurrentHashMap<String,String[]>();

	/**
	 * is contains parameters
	 * @param parameterMap
	 * @return boolean
	 */
	public boolean isContainsParameters(Map<String,String[]> parameterMap){
		boolean result=true;
		if(parameterMap!=null){
			if(!this.parameterMap.isEmpty()){
			    Iterator<Entry<String,String[]>> iterator=this.parameterMap.entrySet().iterator();
			    while(iterator.hasNext()){
			        Entry<String,String[]> entry=iterator.next();
			        String settingParameterKey=entry.getKey();
			        if(parameterMap.containsKey(settingParameterKey)){
                        String[] settingParameterValues=entry.getValue();
                        String[] parameterValues=parameterMap.get(settingParameterKey);
                        if(settingParameterValues!=null&&parameterValues!=null&&settingParameterValues.length>0&&parameterValues.length>0){
                            if(!StringUtil.isMatchPattern(parameterValues[0],settingParameterValues[0])){
                                result=false;
                            }
                        }
                    }else{
                        result=false;
                    }
                    if(!result){
                        break;
                    }
			    }
			}else{
				result=false;
			}
		}
		return result;
	}

	/**
	 * clone action forward bean
	 */
	protected ActionForwardBean clone(){
		ActionForwardBean actionForwardBean=new ActionForwardBean();
		actionForwardBean.setName(this.name);
		actionForwardBean.setPath(this.path);
		actionForwardBean.setStaticParameters(this.staticParameters);
		actionForwardBean.setStaticFilePath(this.staticFilePath);
		return actionForwardBean;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the staticParameters
	 */
	public String getStaticParameters() {
		return staticParameters;
	}
	/**
	 * @param staticParameters the staticParameters to set
	 */
	public void setStaticParameters(String staticParameters) {
		this.staticParameters=staticParameters;
		if(this.staticParameters!=null){
			Map<String,String[]> parameterMap=RequestUtil.parseParameterString(this.staticParameters);
			this.parameterMap.putAll(parameterMap);
		}
	}
	/**
	 * @return the staticFilePath
	 */
	public String getStaticFilePath() {
		return staticFilePath;
	}
	/**
	 * @param staticFilePath the staticFilePath to set
	 */
	public void setStaticFilePath(String staticFilePath) {
		this.staticFilePath = staticFilePath;
	}
	/**
	 * @return the parameterMap
	 */
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}
}
