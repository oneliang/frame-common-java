package com.oneliang.frame.servlet.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

public class ActionContext extends AbstractContext {

	protected static final Map<String,ActionBean> actionBeanMap=new ConcurrentHashMap<String,ActionBean>();
	protected static final Map<String,List<ActionBean>> pathActionBeanMap=new ConcurrentHashMap<String,List<ActionBean>>();
	protected static final Map<String,GlobalForwardBean> globalForwardBeanMap=new ConcurrentHashMap<String,GlobalForwardBean>();
	protected static final Map<String,String> globalForwardMap=new ConcurrentHashMap<String,String>();
	protected static final GlobalExceptionForwardBean globalExceptionForwardBean=new GlobalExceptionForwardBean();
	
	/**
	 * initialize
	 * @param parameters
	 */
	public void initialize(final String parameters) {
		try{
			String tempClassesRealPath=classesRealPath;
			if(tempClassesRealPath==null){
				tempClassesRealPath=this.classLoader.getResource(StringUtil.BLANK).getPath();
			}
			String path=tempClassesRealPath+parameters;
			Document document=JavaXmlUtil.parse(path);
			if(document!=null){
				Element root=document.getDocumentElement();
				//global forward list
				NodeList globalForwardElementList=root.getElementsByTagName(GlobalForwardBean.TAG_GLOBAL_FORWARD);
				if(globalForwardElementList!=null){
					int length=globalForwardElementList.getLength();
					for(int index=0;index<length;index++){
						GlobalForwardBean globalForwardBean=new GlobalForwardBean();
						Node globalForward=globalForwardElementList.item(index);
						NamedNodeMap attributeMap=globalForward.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(globalForwardBean, attributeMap);
						String globalForwardBeanName=globalForwardBean.getName();
						globalForwardBeanMap.put(globalForwardBeanName,globalForwardBean);
						globalForwardMap.put(globalForwardBeanName, globalForwardBean.getPath());
					}
				}
				//global exception forward
				NodeList globalExceptionForwardElementList=root.getElementsByTagName(GlobalExceptionForwardBean.TAG_GLOBAL_EXCEPTION_FORWARD);
				if(globalExceptionForwardElementList!=null&&globalExceptionForwardElementList.getLength()>0){
					NamedNodeMap attributeMap=globalExceptionForwardElementList.item(0).getAttributes();
					JavaXmlUtil.initializeFromAttributeMap(globalExceptionForwardBean, attributeMap);
				}
				//action list
				NodeList actionElementList=root.getElementsByTagName(ActionBean.TAG_ACTION);
				//xml to object
				if(actionElementList!=null){
					int length=actionElementList.getLength();
					for(int index=0;index<length;index++){
						Node actionElement=actionElementList.item(index);
						//action bean
						ActionBean actionBean=new ActionBean();
						NamedNodeMap attributeMap=actionElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(actionBean, attributeMap);
						//node list
						NodeList childNodeElementList=actionElement.getChildNodes();
						if(childNodeElementList!=null){
							int childNodeLength=childNodeElementList.getLength();
							for(int nodeIndex=0;nodeIndex<childNodeLength;nodeIndex++){
								Node childNodeElement=childNodeElementList.item(nodeIndex);
								String childNodeElementName=childNodeElement.getNodeName();
								//interceptorList
								if(childNodeElementName.equals(ActionInterceptorBean.TAG_INTERCEPTOR)){
									ActionInterceptorBean actionInterceptorBean=new ActionInterceptorBean();
									NamedNodeMap interceptorAttributeMap=childNodeElement.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(actionInterceptorBean, interceptorAttributeMap);
									actionBean.addActionBeanInterceptor(actionInterceptorBean);
								}
								//forwardList
								else if(childNodeElementName.equals(ActionForwardBean.TAG_FORWARD)){
									ActionForwardBean actionForwardBean=new ActionForwardBean();
									NamedNodeMap forwardAttributeMap=childNodeElement.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(actionForwardBean, forwardAttributeMap);
									actionBean.addActionForwardBean(actionForwardBean);
								}
							}
						}
						ActionInterface actionInstance=null;
						if(!objectMap.containsKey(actionBean.getId())){
							actionInstance=(ActionInterface)(this.classLoader.loadClass(actionBean.getType()).newInstance());
							objectMap.put(actionBean.getId(),actionInstance);
						}else{
							actionInstance=(ActionInterface)objectMap.get(actionBean.getId());
						}
						actionBean.setActionInstance(actionInstance);
						actionBeanMap.put(actionBean.getId(), actionBean);
						List<ActionBean> actionBeanList=null;
						if(pathActionBeanMap.containsKey(actionBean.getPath())){
							actionBeanList=pathActionBeanMap.get(actionBean.getPath());
						}else{
							actionBeanList=new ArrayList<ActionBean>();
							pathActionBeanMap.put(actionBean.getPath(), actionBeanList);
						}
						actionBeanList.add(actionBean);
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
		actionBeanMap.clear();
		pathActionBeanMap.clear();
		globalForwardBeanMap.clear();
		globalForwardMap.clear();
	}

	/**
	 * interceptor inject
	 */
	public void interceptorInject(){
		Iterator<Entry<String,ActionBean>> iterator=actionBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,ActionBean> entry=iterator.next();
		    ActionBean actionBean=entry.getValue();
            List<ActionInterceptorBean> actionInterceptorBeanList=actionBean.getActionInterceptorBeanList();
            for(ActionInterceptorBean actionInterceptorBean:actionInterceptorBeanList){
                if(objectMap.containsKey(actionInterceptorBean.getId())){
                    Interceptor interceptorInstance=(Interceptor)objectMap.get(actionInterceptorBean.getId());
                    actionInterceptorBean.setInterceptorInstance(interceptorInstance);
                }
            }
		}
	}

	/**
	 * find action bean
	 * @param path
	 * @return List<ActionBean>
	 */
	public List<ActionBean> findActionBeanList(String path){
		List<ActionBean> actionBeanList=null;
		if(pathActionBeanMap.containsKey(path)){
			actionBeanList=pathActionBeanMap.get(path);
		}
		return actionBeanList;
	}

	/**
	 * find action
	 * @param beanId
	 * @return action object
	 */
	public ActionInterface findAction(String beanId){
		ActionInterface action=null;
		if(objectMap.containsKey(beanId)){
			action=(ActionInterface)objectMap.get(beanId);
		}
		return action;
	}
	
	/**
	 * find global forward
	 * @param name forward name
	 * @return forward path
	 */
	public String findGlobalForwardPath(String name){
		String path=null;
		if(name!=null){
			path=globalForwardMap.get(name);
		}
		return path;
	}

	/**
	 * @return the globalExceptionForwardBean
	 */
	public String getGlobalExceptionForwardPath() {
		String path=null;
		if(globalExceptionForwardBean!=null){
			path=globalExceptionForwardBean.getPath();
		}
		return path;
	}
}
