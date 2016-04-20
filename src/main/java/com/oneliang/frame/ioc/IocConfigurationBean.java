package com.oneliang.frame.ioc;

public class IocConfigurationBean{

	public static final String TAG_CONFIGURATION="configuration";
	static final String INJECT_TYPE_AUTO_BY_TYPE=IocBean.INJECT_TYPE_AUTO_BY_TYPE;
	static final String INJECT_TYPE_AUTO_BY_ID=IocBean.INJECT_TYPE_AUTO_BY_ID;
//	static final String INJECT_TYPE_MANUAL="manual";
	
	private String objectInjectType=INJECT_TYPE_AUTO_BY_ID;

	/**
	 * @return the objectInjectType
	 */
	public String getObjectInjectType() {
		return objectInjectType;
	}
	/**
	 * @param objectInjectType the objectInjectType to set
	 */
	public void setObjectInjectType(String objectInjectType) {
		this.objectInjectType = objectInjectType;
	}

}
