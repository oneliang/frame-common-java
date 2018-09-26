package com.oneliang.frame.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

import com.oneliang.Constants;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.frame.ioc.aop.AfterReturningProcessor;
import com.oneliang.frame.ioc.aop.AfterThrowingProcessor;
import com.oneliang.frame.ioc.aop.AopInvocationHandler;
import com.oneliang.frame.ioc.aop.BeforeInvokeProcessor;
import com.oneliang.frame.ioc.aop.InvokeProcessor;
import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.ProxyUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

/**
 * so far,only this context use proxy 
 * @author Dandelion
 */
public class IocContext extends AbstractContext{

	private static final Logger logger=LoggerManager.getLogger(IocContext.class);
	
	protected static final IocConfigurationBean iocConfigurationBean=new IocConfigurationBean();
	protected static final Map<String,IocBean> iocBeanMap=new ConcurrentHashMap<String,IocBean>();

	/**
	 * initialize
	 * @param parameters
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
				//configuration
				NodeList configurationElementList=root.getElementsByTagName(IocConfigurationBean.TAG_CONFIGURATION);
				if(configurationElementList!=null&&configurationElementList.getLength()>0){
					NamedNodeMap configurationAttributeMap=configurationElementList.item(0).getAttributes();
					JavaXmlUtil.initializeFromAttributeMap(iocConfigurationBean, configurationAttributeMap);
				}
				//ioc bean
				NodeList beanElementList=root.getElementsByTagName(IocBean.TAG_BEAN);
				//xml to object
				if(beanElementList!=null){
					int beanElementLength=beanElementList.getLength();
					for(int index=0;index<beanElementLength;index++){
						Node beanElement=beanElementList.item(index);
						//bean
						IocBean iocBean=new IocBean();
						NamedNodeMap attributeMap=beanElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(iocBean, attributeMap);
						//constructor
						NodeList childNodeList=beanElement.getChildNodes();
						if(childNodeList!=null){
							int childNodeLength=childNodeList.getLength();
							for(int childNodeIndex=0;childNodeIndex<childNodeLength;childNodeIndex++){
								Node childNode=childNodeList.item(childNodeIndex);
								String nodeName=childNode.getNodeName();
								if(nodeName.equals(IocConstructorBean.TAG_CONSTRUCTOR)){
									IocConstructorBean iocConstructorBean=new IocConstructorBean();
									NamedNodeMap iocConstructorAttributeMap=childNode.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(iocConstructorBean, iocConstructorAttributeMap);
									iocBean.setIocConstructorBean(iocConstructorBean);
								}
								//property
								else if(nodeName.equals(IocPropertyBean.TAG_PROPERTY)){
									IocPropertyBean iocPropertyBean=new IocPropertyBean();
									NamedNodeMap iocPropertyAttributeMap=childNode.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(iocPropertyBean, iocPropertyAttributeMap);
									iocBean.addIocPropertyBean(iocPropertyBean);
								}
								//after inject
								else if(nodeName.equals(IocAfterInjectBean.TAG_AFTER_INJECT)){
									IocAfterInjectBean iocAfterInjectBean=new IocAfterInjectBean();
									NamedNodeMap iocAfterInjectAttributeMap=childNode.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(iocAfterInjectBean, iocAfterInjectAttributeMap);
									iocBean.addIocAfterInjectBean(iocAfterInjectBean);
								}
							}
						}
						if(!iocBeanMap.containsKey(iocBean.getId())){
							iocBeanMap.put(iocBean.getId(),iocBean);
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
		iocBeanMap.clear();
	}

	/**
	 * ioc bean object instantiated
	 * @throws Exception
	 */
	protected void iocBeanObjectInstantiated() throws Exception{
		Iterator<Entry<String,IocBean>> iterator=iocBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,IocBean> entry=iterator.next();
			IocBean iocBean=entry.getValue();
			if(iocBean.getIocConstructorBean()!=null){
				iocBeanObjectInstantiatedByConstructor(iocBean);
			}else{
				iocBeanObjectInstantiatedByDefaultConstructor(iocBean);
			}
		}
	}

	/**
	 * instantiated one ioc bean by constructor
	 * @param iocBean
	 * @throws Exception
	 */
	private void iocBeanObjectInstantiatedByConstructor(IocBean iocBean) throws Exception{
	    IocConstructorBean iocConstructorBean=iocBean.getIocConstructorBean();
	    String type=StringUtil.nullToBlank(iocBean.getType());
	    Class<?> beanClass=iocBean.getBeanClass();
        String constructorTypes=iocConstructorBean.getTypes();
        String constructorReferences=iocConstructorBean.getReferences();
        String[] constructorTypeArray=constructorTypes.split(Constants.Symbol.COMMA);
        String[] constructorReferenceArray=constructorReferences.split(Constants.Symbol.COMMA);
        Class<?>[] constructorTypeClassArray=new Class<?>[constructorTypeArray.length];
        Object[] constructorReferenceObjectArray=new Object[constructorReferenceArray.length];
        int index=0;
        for(String constructorType:constructorTypeArray){
            constructorTypeClassArray[index++]=ClassUtil.getClass(this.classLoader, constructorType);
        }
        index=0;
        for(String constructorReference:constructorReferenceArray){
            IocBean referenceObject=iocBeanMap.get(constructorReference);
            if(referenceObject!=null){
                Object referenceProxyObject=referenceObject.getProxyInstance();
                if(referenceProxyObject==null){
                    iocBeanObjectInstantiatedByDefaultConstructor(referenceObject);
                    referenceProxyObject=referenceObject.getProxyInstance();
                }
                constructorReferenceObjectArray[index++]=referenceProxyObject;
            }else{
                constructorReferenceObjectArray[index++]=null;
            }
        }
        Constructor<?> constructor=null;
        if(beanClass==null){
        	constructor=this.classLoader.loadClass(type).getConstructor(constructorTypeClassArray);
        }else{
        	constructor=beanClass.getConstructor(constructorTypeClassArray);
        }
        Object beanInstance=constructor.newInstance(constructorReferenceObjectArray);
        iocBean.setBeanInstance(beanInstance);
        if(iocBean.isProxy()){
        	ClassLoader classLoader=null;
			if(beanClass==null){
				classLoader=this.classLoader;
			}else{
				classLoader=beanClass.getClassLoader();
			}
            Object proxyInstance=ProxyUtil.newProxyInstance(classLoader, beanInstance, new AopInvocationHandler<Object>(beanInstance));
            iocBean.setProxyInstance(proxyInstance);
        }else{
            iocBean.setProxyInstance(beanInstance);
        }
	}

	/**
	 * instantiated one ioc bean by default constructor
	 * @param iocBean
	 * @throws Exception
	 */
	private void iocBeanObjectInstantiatedByDefaultConstructor(IocBean iocBean) throws Exception{
		String type=StringUtil.nullToBlank(iocBean.getType());
		Class<?> beanClass=iocBean.getBeanClass();
		String value=StringUtil.nullToBlank(iocBean.getValue());
		Object beanInstance=iocBean.getBeanInstance();
		if(beanInstance==null){
			String iocBeanId=iocBean.getId();
			if(objectMap.containsKey(iocBeanId)){//object map contain,prove duplicate config in ioc,copy to ioc bean
				beanInstance=objectMap.get(iocBeanId);
				iocBean.setBeanInstance(beanInstance);
				iocBean.setProxyInstance(beanInstance);
			}else{//normal config
				if(ClassUtil.isBaseClass(type)||ClassUtil.isSimpleClass(type)){
					Class<?> clazz=ClassUtil.getClass(this.classLoader, type);
					beanInstance=ClassUtil.changeType(clazz, new String[]{value});
				}else{
					if(beanClass==null){
						beanInstance=this.classLoader.loadClass(type).newInstance();
					}else{
						beanInstance=beanClass.newInstance();
					}
					//aop interceptor
					if(beanInstance instanceof BeforeInvokeProcessor){
						AopInvocationHandler.addBeforeInvokeProcessor((BeforeInvokeProcessor)beanInstance);
					}
					if(beanInstance instanceof AfterReturningProcessor){
						AopInvocationHandler.addAfterReturningProcessor((AfterReturningProcessor)beanInstance);
					}
					if(beanInstance instanceof AfterThrowingProcessor){
						AopInvocationHandler.addAfterThrowingProcessor((AfterThrowingProcessor)beanInstance);
					}
					if(beanInstance instanceof InvokeProcessor){
						AopInvocationHandler.setInvokeProcessor((InvokeProcessor)beanInstance);
					}
				}
				iocBean.setBeanInstance(beanInstance);
				if(iocBean.isProxy()){
					ClassLoader classLoader=null;
					if(beanClass==null){
						classLoader=this.classLoader;
					}else{
						classLoader=beanClass.getClassLoader();
					}
					Object proxyInstance=ProxyUtil.newProxyInstance(classLoader, beanInstance, new AopInvocationHandler<Object>(beanInstance));
					iocBean.setProxyInstance(proxyInstance);
				}else{
					iocBean.setProxyInstance(beanInstance);
				}
			}
			logger.info(iocBean.getType()+"<->id:"+iocBeanId+"<->proxy:"+iocBean.getProxyInstance()+"<->instance:"+iocBean.getBeanInstance());
		}
	}

	/**
	 * inject,only this injection put the proxy instance to object map
	 * @throws Exception
	 */
	public void inject() throws Exception{
		//instantiated all ioc bean
		iocBeanObjectInstantiated();
		//inject
		String objectInjectType=iocConfigurationBean.getObjectInjectType();
		if(objectInjectType!=null){
			if(objectInjectType.equals(IocConfigurationBean.INJECT_TYPE_AUTO_BY_ID)){
				Iterator<Entry<String,Object>> iterator=objectMap.entrySet().iterator();
				while(iterator.hasNext()){
				    Entry<String,Object> entry=iterator.next();
					Object object=entry.getValue();
					this.autoInjectObjectById(object);
				}
			}else if(objectInjectType.equals(IocConfigurationBean.INJECT_TYPE_AUTO_BY_TYPE)){
			    Iterator<Entry<String,Object>> iterator=objectMap.entrySet().iterator();
                while(iterator.hasNext()){
                    Entry<String,Object> entry=iterator.next();
                    Object object=entry.getValue();
					this.autoInjectObjectByType(object);
				}
			}
			Iterator<Entry<String,IocBean>> iterator=iocBeanMap.entrySet().iterator();
			while(iterator.hasNext()){
			    Entry<String,IocBean> entry=iterator.next();
				IocBean iocBean=entry.getValue();
				if(iocBean!=null){
					String injectType=iocBean.getInjectType();
					if(injectType!=null){
						String iocBeanId=iocBean.getId();
						Object beanInstance=iocBean.getBeanInstance();
						if(injectType.equals(IocBean.INJECT_TYPE_AUTO_BY_ID)){
							this.autoInjectObjectById(beanInstance);
						}else if(injectType.equals(IocBean.INJECT_TYPE_AUTO_BY_TYPE)){
							this.autoInjectObjectByType(beanInstance);
						}else if(injectType.equals(IocBean.INJECT_TYPE_MANUAL)){
							this.manualInject(iocBean);
						}
						if(!objectMap.containsKey(iocBeanId)){
							objectMap.put(iocBeanId, iocBean.getProxyInstance());
						}
					}
				}
			}
		}
	}
	
	/**
	 * auto inject object by type
	 * @throws Exception
	 */
	protected void autoInjectObjectByType(Object object) throws Exception{
		if(object!=null){
			Method[] objectMethods=object.getClass().getMethods();
			for(Method method:objectMethods){
				String methodName=method.getName();
				if(methodName.startsWith(Constants.Method.PREFIX_SET)){
					Class<?>[] types=method.getParameterTypes();
					if(types!=null&&types.length==1){
						Class<?> parameterClass=types[0];
						String parameterClassName=parameterClass.getName();
						Iterator<Entry<String,IocBean>> iterator=iocBeanMap.entrySet().iterator();
						while(iterator.hasNext()){
						    Entry<String,IocBean> entry=iterator.next();
							IocBean iocBean=entry.getValue();
							Object beanInstance=iocBean.getBeanInstance();
							Object proxyInstance=iocBean.getProxyInstance();
							String beanInstanceClassName=beanInstance.getClass().getName();
							if(parameterClassName.equals(beanInstanceClassName)){
								logger.info(object.getClass().getName()+"<-"+beanInstance.getClass().getName());
								method.invoke(object,proxyInstance);
							}else{
								Class<?>[] interfaces=beanInstance.getClass().getInterfaces();
								if(interfaces!=null){
									for(Class<?> interfaceClass:interfaces){
										String beanInstanceClassInterfaceName=interfaceClass.getName();
										if(parameterClassName.equals(beanInstanceClassInterfaceName)){
											logger.info(object.getClass().getName()+"<-"+beanInstance.getClass().getName());
											method.invoke(object,proxyInstance);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * auto inject object by id
	 * @throws Exception
	 */
	public void autoInjectObjectById(Object object) throws Exception{
		if(object!=null){
			Method[] objectMethods=object.getClass().getMethods();
			for(Method method:objectMethods){
				String methodName=method.getName();
				if(methodName.startsWith(Constants.Method.PREFIX_SET)){
					Class<?>[] types=method.getParameterTypes();
					if(types!=null&&types.length==1){
						String fieldName=ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName);
						IocBean iocBean=iocBeanMap.get(fieldName);
						if(iocBean!=null){
							Object proxyInstance=iocBean.getProxyInstance();
							logger.info(object.getClass().getName()+"<-"+iocBean.getType());
							method.invoke(object,proxyInstance);
						}
					}
				}
			}
		}
	}

	/**
	 * manual inject,must config all bean in ioc
	 * @throws Exception
	 */
	protected void manualInject(IocBean iocBean) throws Exception{
		if(iocBean!=null){
			String id=iocBean.getId();
			List<IocPropertyBean> iocPropertyBeanList=iocBean.getIocPropertyBeanList();
			for(IocPropertyBean iocPropertyBean:iocPropertyBeanList){
				String propertyName=iocPropertyBean.getName();
				String referenceBeanId=iocPropertyBean.getReference();
				if(iocBeanMap.containsKey(referenceBeanId)){
					Object beanInstance=iocBean.getBeanInstance();
					Method[] objectMethods=beanInstance.getClass().getMethods();
					for(Method method:objectMethods){
						String methodName=method.getName();
						if(methodName.startsWith(Constants.Method.PREFIX_SET)){
							String fieldName=ObjectUtil.methodNameToFieldName(Constants.Method.PREFIX_SET, methodName);
							if(propertyName.equals(fieldName)){
								Class<?>[] types=method.getParameterTypes();
								if(types!=null&&types.length==1){
									IocBean referenceObject=iocBeanMap.get(referenceBeanId);
									if(referenceObject!=null){
										Object proxyInstance=referenceObject.getProxyInstance();
										logger.info(iocBean.getType()+"<-"+referenceObject.getType());
										method.invoke(beanInstance, proxyInstance);
									}
								}
							}
						}
					}
				}
			}
			if(!objectMap.containsKey(id)){
				objectMap.put(id, iocBean.getProxyInstance());
			}
		}
	}

	/**
	 * after inject
	 * @throws Exception
	 */
	public void afterInject() throws Exception{
		Iterator<Entry<String,IocBean>> iterator=iocBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,IocBean> entry=iterator.next();
			IocBean iocBean=entry.getValue();
			List<IocAfterInjectBean> iocAfterInjectBeanList=iocBean.getIocAfterInjectBeanList();
			for(IocAfterInjectBean iocAfterInjectBean:iocAfterInjectBeanList){
				Object object=iocBean.getProxyInstance();
				Method method=object.getClass().getMethod(iocAfterInjectBean.getMethod());
				method.invoke(object);
			}
		}
	}

	/**
	 * put to ioc bean map
	 * @param key
	 * @param iocBean
	 */
	public void putToIocBeanMap(String key,IocBean iocBean) {
		iocBeanMap.put(key,iocBean);
	}
}
