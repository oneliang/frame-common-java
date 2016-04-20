package com.oneliang.frame.servlet.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.StringUtil;

public class InterceptorContext extends AbstractContext {

	protected static final Map<String,GlobalInterceptorBean> globalInterceptorBeanMap=new ConcurrentHashMap<String,GlobalInterceptorBean>();
	protected static final Map<String,InterceptorBean> interceptorBeanMap=new ConcurrentHashMap<String,InterceptorBean>();
	protected static final List<Interceptor> beforeGlobalInterceptorList=new CopyOnWriteArrayList<Interceptor>();
	protected static final List<Interceptor> afterGlobalInterceptorList=new CopyOnWriteArrayList<Interceptor>();

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
				//global interceptor list
				NodeList globalInterceptorElementList=root.getElementsByTagName(GlobalInterceptorBean.TAG_GLOBAL_INTERCEPTOR);
				if(globalInterceptorElementList!=null){
					int globalInterceptorLength=globalInterceptorElementList.getLength();
					for(int index=0;index<globalInterceptorLength;index++){
						Node globalInterceptorElement=globalInterceptorElementList.item(index);
						GlobalInterceptorBean globalInterceptor=new GlobalInterceptorBean();
						NamedNodeMap attributeMap=globalInterceptorElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(globalInterceptor, attributeMap);
						Interceptor interceptorInstance=null;
						interceptorInstance=(Interceptor)(this.classLoader.loadClass(globalInterceptor.getType()).newInstance());
						globalInterceptor.setInterceptorInstance(interceptorInstance);
						globalInterceptorBeanMap.put(globalInterceptor.getId(),globalInterceptor);
						objectMap.put(globalInterceptor.getId(),interceptorInstance);
						String mode=globalInterceptor.getMode();
						if(mode!=null){
							if(mode.equals(GlobalInterceptorBean.INTERCEPTOR_MODE_BEFORE)){
								beforeGlobalInterceptorList.add(interceptorInstance);
							}else if(mode.equals(GlobalInterceptorBean.INTERCEPTOR_MODE_AFTER)){
								afterGlobalInterceptorList.add(interceptorInstance);
							}
						}
					}
				}
				//interceptor list
				NodeList interceptorElementList=root.getElementsByTagName(InterceptorBean.TAG_INTERCEPTOR);
				if(interceptorElementList!=null){
					int interceptorElementLength=interceptorElementList.getLength();
					for(int index=0;index<interceptorElementLength;index++){
						Node interceptorElement=interceptorElementList.item(index);
						InterceptorBean interceptor=new InterceptorBean();
						NamedNodeMap attributeMap=interceptorElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(interceptor, attributeMap);
						Interceptor interceptorInstance=null;
						interceptorInstance=(Interceptor)(this.classLoader.loadClass(interceptor.getType()).newInstance());
						interceptor.setInterceptorInstance(interceptorInstance);
						interceptorBeanMap.put(interceptor.getId(),interceptor);
						objectMap.put(interceptor.getId(),interceptorInstance);
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
		globalInterceptorBeanMap.clear();
		interceptorBeanMap.clear();
		beforeGlobalInterceptorList.clear();
		afterGlobalInterceptorList.clear();
	}

	/**
	 * @return the beforeGlobalInterceptorList
	 */
	public List<Interceptor> getBeforeGlobalInterceptorList() {
		return beforeGlobalInterceptorList;
	}

	/**
	 * @return the afterGlobalInterceptorList
	 */
	public List<Interceptor> getAfterGlobalInterceptorList() {
		return afterGlobalInterceptorList;
	}
}
