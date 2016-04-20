package com.oneliang.frame.configuration;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.frame.Context;
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.log.Logger;

public class ConfigurationContext extends AbstractContext {

	private static final Logger logger=Logger.getLogger(ConfigurationContext.class);

	protected static final Map<String,ConfigurationBean> configurationBeanMap=new ConcurrentHashMap<String,ConfigurationBean>();
	protected final Map<String,ConfigurationBean> selfConfigurationBeanMap=new ConcurrentHashMap<String,ConfigurationBean>();
	protected boolean initialized=false;

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			String path=parameters;
			String tempClassesRealPath=classesRealPath;
			if(tempClassesRealPath==null){
				tempClassesRealPath=this.classLoader.getResource(StringUtil.BLANK).getPath();
			}
			path=tempClassesRealPath+path;
			Document document=JavaXmlUtil.parse(path);
			if(document!=null){
				Element root=document.getDocumentElement();
				NodeList configurationList=root.getElementsByTagName(ConfigurationBean.TAG_CONFIGURATION);
				if(configurationList!=null){
					int length=configurationList.getLength();
					for(int index=0;index<length;index++){
						ConfigurationBean configurationBean=new ConfigurationBean();
						Node configurationNode=configurationList.item(index);
						NamedNodeMap configurationAttributesMap=configurationNode.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(configurationBean,configurationAttributesMap);
						Context context=(Context)(this.classLoader.loadClass(configurationBean.getContextClass()).newInstance());
						logger.log("Context:"+context.getClass().getName()+",id:"+configurationBean.getId()+" is initializing.");
						if(context instanceof AbstractContext){
							AbstractContext abstractContext=(AbstractContext)context;
							abstractContext.setProjectRealPath(this.projectRealPath);
							abstractContext.setClassesRealPath(this.classesRealPath);
						}
						context.initialize(configurationBean.getParameters());
						configurationBean.setContextInstance(context);
						configurationBeanMap.put(configurationBean.getId(),configurationBean);
						this.selfConfigurationBeanMap.put(configurationBean.getId(),configurationBean);
					}
				}
			}
			this.initialized=true;
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * destroy,only destroy self,recursion
	 */
	public void destroy(){
		Iterator<Entry<String,ConfigurationBean>> iterator=this.selfConfigurationBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
            Entry<String,ConfigurationBean> entry=iterator.next();
            ConfigurationBean configurationBean=entry.getValue();
            configurationBean.getContextInstance().destroy();
		}
		this.selfConfigurationBeanMap.clear();
	}

	/**
	 * destroy all,include destroy all configuration context,all configuration bean and all object
	 */
	public void destroyAll(){
		this.destroy();
		configurationBeanMap.clear();
		objectMap.clear();
	}

	/**
	 * find context
	 * @param id
	 * @return Context
	 */
	public Context findContext(String id){
		Context context=null;
		if(configurationBeanMap.containsKey(id)){
			ConfigurationBean configurationBean=configurationBeanMap.get(id);
			context=configurationBean.getContextInstance();
		}
		return context;
	}

	/**
	 * find context
	 * @param <T>
	 * @param clazz
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T extends Context> T findContext(Class<T> clazz){
		T object=null;
		Iterator<Entry<String,ConfigurationBean>> iterator=configurationBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,ConfigurationBean> entry=iterator.next();
		    ConfigurationBean configurationBean=entry.getValue();
		    Context context=configurationBean.getContextInstance();
		    if(ObjectUtil.isEntity(context,clazz)){
		    	object=(T)context;
		    	break;
		    }
		}
		return object;
	}

	/**
	 * get configuration bean entry set
	 * @return the configurationBeanMap
	 */
	public Set<Entry<String,ConfigurationBean>> getConfigurationBeanEntrySet() {
		return configurationBeanMap.entrySet();
	}

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }
}
