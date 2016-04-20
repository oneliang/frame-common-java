package com.oneliang.frame.ioc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class IocBean{

	public static final String TAG_BEAN="bean";
	
	public static final String INJECT_TYPE_AUTO_BY_TYPE="autoByType";
	public static final String INJECT_TYPE_AUTO_BY_ID="autoById";
	public static final String INJECT_TYPE_MANUAL="manual";
	
	private String id=null;
	private String type=null;
	private String value=null;
	private boolean proxy=true;
	private String injectType=INJECT_TYPE_AUTO_BY_ID;
	private Class<?> beanClass=null;
	private Object beanInstance=null;
	private Object proxyInstance=null;
	private IocConstructorBean iocConstructorBean=null;
	private List<IocPropertyBean> iocPropertyBeanList=new CopyOnWriteArrayList<IocPropertyBean>();
	private List<IocAfterInjectBean> iocAfterInjectBeanList=new CopyOnWriteArrayList<IocAfterInjectBean>();
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the ioc property bean list
	 */
	public List<IocPropertyBean> getIocPropertyBeanList() {
		return iocPropertyBeanList;
	}
	
	/**
	 * @param iocPropertyBean
	 * @return boolean
	 */
	public boolean addIocPropertyBean(IocPropertyBean iocPropertyBean){
		return iocPropertyBeanList.add(iocPropertyBean);
	}
	/**
	 * @return the ioc after inject bean list
	 */
	public List<IocAfterInjectBean> getIocAfterInjectBeanList(){
		return iocAfterInjectBeanList;
	}
	/**
	 * @param iocAfterInjectBean
	 * @return boolean
	 */
	public boolean addIocAfterInjectBean(IocAfterInjectBean iocAfterInjectBean){
		return iocAfterInjectBeanList.add(iocAfterInjectBean);
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
	 * @return the proxy
	 */
	public boolean isProxy() {
		return proxy;
	}
	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the beanInstance
	 */
	public Object getBeanInstance() {
		return beanInstance;
	}
	/**
	 * @param beanInstance the beanInstance to set
	 */
	public void setBeanInstance(Object beanInstance) {
		this.beanInstance = beanInstance;
	}
	/**
	 * @return the proxyInstance
	 */
	public Object getProxyInstance() {
		return proxyInstance;
	}
	/**
	 * @param proxyInstance the proxyInstance to set
	 */
	public void setProxyInstance(Object proxyInstance) {
		this.proxyInstance = proxyInstance;
	}
	/**
	 * @return the iocConstructorBean
	 */
	public IocConstructorBean getIocConstructorBean() {
		return iocConstructorBean;
	}
	/**
	 * @param iocConstructorBean the iocConstructorBean to set
	 */
	public void setIocConstructorBean(IocConstructorBean iocConstructorBean) {
		this.iocConstructorBean = iocConstructorBean;
	}
	/**
	 * @return the injectType
	 */
	public String getInjectType() {
		return injectType;
	}
	/**
	 * @param injectType the injectType to set
	 */
	public void setInjectType(String injectType) {
		this.injectType = injectType;
	}
	/**
	 * @return the beanClass
	 */
	public Class<?> getBeanClass() {
		return beanClass;
	}
	/**
	 * @param beanClass the beanClass to set
	 */
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
}
