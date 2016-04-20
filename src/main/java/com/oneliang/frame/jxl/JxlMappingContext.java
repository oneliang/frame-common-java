package com.oneliang.frame.jxl;

import java.util.Map;
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
import com.oneliang.util.jxl.JxlMappingBean;
import com.oneliang.util.jxl.JxlMappingColumnBean;

public class JxlMappingContext extends AbstractContext {

	protected static final Map<String,JxlMappingBean> typeImportJxlMappingBeanMap=new ConcurrentHashMap<String,JxlMappingBean>();
	protected static final Map<String,JxlMappingBean> nameImportJxlMappingBeanMap=new ConcurrentHashMap<String,JxlMappingBean>();
	protected static final Map<String,JxlMappingBean> typeExportJxlMappingBeanMap=new ConcurrentHashMap<String,JxlMappingBean>();
	protected static final Map<String,JxlMappingBean> nameExportJxlMappingBeanMap=new ConcurrentHashMap<String,JxlMappingBean>();

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
				NodeList beanElementList=root.getElementsByTagName(JxlMappingBean.TAG_BEAN);
				if(beanElementList!=null){
					int length=beanElementList.getLength();
					for(int index=0;index<length;index++){
						Node beanElement=beanElementList.item(index);
						JxlMappingBean jxlMappingBean=new JxlMappingBean();
						NamedNodeMap attributeMap=beanElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(jxlMappingBean, attributeMap);
						//bean column
						NodeList childNodeList=beanElement.getChildNodes();
						if(childNodeList!=null){
							int childNodeLength=childNodeList.getLength();
							for(int childNodeIndex=0;childNodeIndex<childNodeLength;childNodeIndex++){
								Node childNode=childNodeList.item(childNodeIndex);
								String nodeName=childNode.getNodeName();
								if(nodeName.equals(JxlMappingColumnBean.TAG_COLUMN)){
									JxlMappingColumnBean jxlMappingColumnBean=new JxlMappingColumnBean();
									NamedNodeMap childNodeAttributeMap=childNode.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(jxlMappingColumnBean, childNodeAttributeMap);
									jxlMappingBean.addJxlMappingColumnBean(jxlMappingColumnBean);
								}
							}
						}
						String useFor=jxlMappingBean.getUseFor();
						String type=jxlMappingBean.getType();
						if(useFor.equals(JxlMappingBean.USE_FOR_IMPORT)){
							typeImportJxlMappingBeanMap.put(type, jxlMappingBean);
							nameImportJxlMappingBeanMap.put(this.classLoader.loadClass(type).getSimpleName(), jxlMappingBean);
						}else if(useFor.equals(JxlMappingBean.USE_FOR_EXPORT)){
							typeExportJxlMappingBeanMap.put(type, jxlMappingBean);
							nameExportJxlMappingBeanMap.put(this.classLoader.loadClass(type).getSimpleName(), jxlMappingBean);
						}
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
		typeImportJxlMappingBeanMap.clear();
		nameImportJxlMappingBeanMap.clear();
		typeExportJxlMappingBeanMap.clear();
		nameExportJxlMappingBeanMap.clear();
	}

	/**
	 * findImportJxlMappingBean
	 * @param <T>
	 * @param clazz
	 * @return JxlMappingBean
	 */
	public <T extends Object> JxlMappingBean findImportJxlMappingBean(Class<T> clazz){
		JxlMappingBean bean=null;
		if(clazz!=null){
			String className=clazz.getName();
			bean=typeImportJxlMappingBeanMap.get(className);
		}
		return bean;
	}
	
	/**
	 * @param name full name or simple name
	 * @return JxlMappingBean
	 * @throws Exception
	 */
	public JxlMappingBean findImportJxlMappingBean(String name) throws Exception{
		JxlMappingBean bean=null;
		if(name!=null){
			bean=typeImportJxlMappingBeanMap.get(name);
			if(bean==null){
				bean=nameImportJxlMappingBeanMap.get(name);
			}
		}
		return bean;
	}
	
	/**
	 * findExportJxlMappingBean
	 * @param <T>
	 * @param clazz
	 * @return JxlMappingBean
	 */
	public <T extends Object> JxlMappingBean findExportJxlMappingBean(Class<T> clazz){
		JxlMappingBean bean=null;
		if(clazz!=null){
			String className=clazz.getName();
			bean=typeExportJxlMappingBeanMap.get(className);
		}
		return bean;
	}
	
	/**
	 * @param name full name or simple name
	 * @return JxlMappingBean
	 * @throws Exception
	 */
	public JxlMappingBean findExportJxlMappingBean(String name) throws Exception{
		JxlMappingBean bean=null;
		if(name!=null){
			bean=typeExportJxlMappingBeanMap.get(name);
			if(bean==null){
				bean=nameExportJxlMappingBeanMap.get(name);
			}
		}
		return bean;
	}
}
