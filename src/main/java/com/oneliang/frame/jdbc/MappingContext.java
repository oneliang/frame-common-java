package com.oneliang.frame.jdbc;

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
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.StringUtil;

public class MappingContext extends AbstractContext {

	protected static final Map<String,MappingBean> classNameMappingBeanMap=new ConcurrentHashMap<String,MappingBean>();
	protected static final Map<String,MappingBean> simpleNameMappingBeanMap=new ConcurrentHashMap<String,MappingBean>();

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
				NodeList beanElementList=root.getElementsByTagName(MappingBean.TAG_BEAN);
				if(beanElementList!=null){
					int length=beanElementList.getLength();
					for(int index=0;index<length;index++){
						Node beanElement=beanElementList.item(index);
						MappingBean mappingBean=new MappingBean();
						NamedNodeMap attributeMap=beanElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(mappingBean, attributeMap);
						//bean column
						NodeList childNodeList=beanElement.getChildNodes();
						if(childNodeList!=null){
							int childNodeLength=childNodeList.getLength();
							for(int childNodeIndex=0;childNodeIndex<childNodeLength;childNodeIndex++){
								Node childNode=childNodeList.item(childNodeIndex);
								String nodeName=childNode.getNodeName();
								if(nodeName.equals(MappingColumnBean.TAG_COLUMN)){
									MappingColumnBean mappingColumnBean=new MappingColumnBean();
									NamedNodeMap childNodeAttributeMap=childNode.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(mappingColumnBean, childNodeAttributeMap);
									mappingBean.addMappingColumnBean(mappingColumnBean);
								}
							}
						}
						String className=mappingBean.getType();
						classNameMappingBeanMap.put(className, mappingBean);
						simpleNameMappingBeanMap.put(this.classLoader.loadClass(className).getSimpleName(), mappingBean);
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
		classNameMappingBeanMap.clear();
		simpleNameMappingBeanMap.clear();
	}

	/**
	 * findMappingBean
	 * @param <T>
	 * @param clazz
	 * @return MappingBean
	 */
	public <T extends Object> MappingBean findMappingBean(Class<T> clazz){
		MappingBean bean=null;
		if(clazz!=null){
			String className=clazz.getName();
			bean=classNameMappingBeanMap.get(className);
		}
		return bean;
	}
	
	/**
	 * @param name full name or simple name
	 * @return MappingBean
	 */
	public MappingBean findMappingBean(String name){
		MappingBean bean=null;
		if(name!=null){
			bean=classNameMappingBeanMap.get(name);
			if(bean==null){
				bean=simpleNameMappingBeanMap.get(name);
			}
		}
		return bean;
	}

	/**
	 * get mapping bean entry set
	 * @return Set<Entry<String,MappingBean>>
	 */
	public Set<Entry<String,MappingBean>> getMappingBeanEntrySet(){
		return classNameMappingBeanMap.entrySet();
	}
}
