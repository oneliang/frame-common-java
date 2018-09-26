package com.oneliang.frame.servlet.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oneliang.Constants;
import com.oneliang.frame.servlet.action.ActionInterface.HttpRequestMethod;
import com.oneliang.util.common.StringUtil;

public class ActionBean{

	private static final String REGEX="\\{[\\w]*\\}";
	private static final String FIRST_REGEX="\\{";
	
	public static final String TAG_ACTION="action";
	
	private String id=null;
	private String type=null;
	private String path=null;
	private String httpRequestMethods=null;
	private int httpRequestMethodsCode=HttpRequestMethod.GET.getCode()|HttpRequestMethod.POST.getCode(); 
	private Object actionInstance=null;
	private List<ActionInterceptorBean> actionInterceptorBeanList=new CopyOnWriteArrayList<ActionInterceptorBean>();
	private List<ActionInterceptorBean> beforeActionInterceptorBeanList=new CopyOnWriteArrayList<ActionInterceptorBean>();
	private List<ActionInterceptorBean> afterActionInterceptorBeanList=new CopyOnWriteArrayList<ActionInterceptorBean>();
	private List<ActionForwardBean> actionForwardBeanList=new CopyOnWriteArrayList<ActionForwardBean>();
	
	/**
	 * find forward path
	 * @param forward
	 * @return forward path
	 */
	public String findForwardPath(String forward){
		String forwardPath=null;
		for(ActionForwardBean actionForwardBean:actionForwardBeanList){
			String forwardName=actionForwardBean.getName();
			if(forwardName!=null&&forwardName.equals(forward)){
				forwardPath=actionForwardBean.getPath();
				break;
			}
		}
		return forwardPath;
	}

	/**
	 * find action forward bean by static parameter
	 * @param parameterMap
	 * @return boolean
	 */
	public ActionForwardBean findActionForwardBeanByStaticParameter(Map<String,String[]> parameterMap){
		ActionForwardBean forwardBean=null;
		for(ActionForwardBean actionForwardBean:actionForwardBeanList){
			if(actionForwardBean.isContainsParameters(parameterMap)){
				forwardBean=actionForwardBean.clone();
				this.replaceActionForwardBeanStaticFilePath(forwardBean,parameterMap);
				break;
			}
		}
		return forwardBean;
	}

	/**
	 * replace action forward bean static file path
	 * @param actionForwardBean
	 * @param parameterMap
	 */
	private void replaceActionForwardBeanStaticFilePath(ActionForwardBean actionForwardBean,Map<String,String[]> parameterMap){
		if(actionForwardBean!=null&&parameterMap!=null){
			String staticFilePath=actionForwardBean.getStaticFilePath();
			String staticFilePathResult=staticFilePath;
			List<String> groupList=StringUtil.parseStringGroup(staticFilePath, REGEX, FIRST_REGEX, StringUtil.BLANK, 1);
			if(groupList!=null){
				for(String group:groupList){
					String[] parameterValues=parameterMap.get(group);
					if(parameterValues!=null&&parameterValues.length>0){
						staticFilePathResult=staticFilePathResult.replaceFirst(REGEX, parameterValues[0]);
					}
				}
			}
			actionForwardBean.setStaticFilePath(staticFilePathResult);
		}
	}

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
	 * @return the actionInstance
	 */
	public Object getActionInstance() {
		return actionInstance;
	}
	/**
	 * @param actionInstance the actionInstance to set
	 */
	public void setActionInstance(Object actionInstance) {
		this.actionInstance = actionInstance;
	}

	/**
	 * addInterceptor
	 * @param actionInterceptorBean
	 */
	public void addActionBeanInterceptor(ActionInterceptorBean actionInterceptorBean) {
		if(actionInterceptorBean!=null){
			String interceptorMode=actionInterceptorBean.getMode();
			if(interceptorMode!=null){
				if(interceptorMode.equals(ActionInterceptorBean.INTERCEPTOR_MODE_BEFORE)){
					this.beforeActionInterceptorBeanList.add(actionInterceptorBean);
				}else if(interceptorMode.equals(ActionInterceptorBean.INTERCEPTOR_MODE_AFTER)){
					this.afterActionInterceptorBeanList.add(actionInterceptorBean);
				}
			}
			this.actionInterceptorBeanList.add(actionInterceptorBean);
		}
	}

	/**
	 * @return the action interceptor bean list
	 */
	public List<ActionInterceptorBean> getActionInterceptorBeanList() {
		return this.actionInterceptorBeanList;
	}

	/**
	 * add action forward bean
	 * @param actionForwardBean
	 * @return boolean
	 */
	public boolean addActionForwardBean(ActionForwardBean actionForwardBean){
		return this.actionForwardBeanList.add(actionForwardBean);
	}
	
	/**
	 * @return the forwardList
	 */
	public List<ActionForwardBean> getActionForwardBeanList() {
		return actionForwardBeanList;
	}

	/**
	 * @return the beforeInterceptorBeanList
	 */
	public List<ActionInterceptorBean> getBeforeActionInterceptorBeanList() {
		return beforeActionInterceptorBeanList;
	}

	/**
	 * @return the afterInterceptorBeanList
	 */
	public List<ActionInterceptorBean> getAfterActionInterceptorBeanList() {
		return afterActionInterceptorBeanList;
	}

	/**
	 * @return the httpRequestMethods
	 */
	public String getHttpRequestMethods() {
		return httpRequestMethods;
	}

	/**
	 * @param httpRequestMethods the httpRequestMethods to set
	 */
	public void setHttpRequestMethods(String httpRequestMethods) {
		this.httpRequestMethods = httpRequestMethods;
		if(StringUtil.isNotBlank(this.httpRequestMethods)){
			this.httpRequestMethodsCode=0;
			String[] httpRequestMethodArray=this.httpRequestMethods.split(Constants.Symbol.COMMA);
			for(String httpRequestMethod:httpRequestMethodArray){
				if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.PUT)){
					this.httpRequestMethodsCode|=HttpRequestMethod.PUT.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.DELETE)){
					this.httpRequestMethodsCode|=HttpRequestMethod.DELETE.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.GET)){
					this.httpRequestMethodsCode|=HttpRequestMethod.GET.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.POST)){
					this.httpRequestMethodsCode|=HttpRequestMethod.POST.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.HEAD)){
					this.httpRequestMethodsCode|=HttpRequestMethod.HEAD.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.OPTIONS)){
					this.httpRequestMethodsCode|=HttpRequestMethod.OPTIONS.getCode();
				}else if(httpRequestMethod.equalsIgnoreCase(Constants.Http.RequestMethod.TRACE)){
					this.httpRequestMethodsCode|=HttpRequestMethod.TRACE.getCode();
				}
			}
		}
	}

	/**
	 * is contain http request method
	 * @param httpRequestMethod
	 * @return boolean
	 */
	public boolean isContainHttpRequestMethod(HttpRequestMethod httpRequestMethod){
		boolean result=false;
		if(httpRequestMethod!=null){
			result=httpRequestMethod.getCode()==(this.httpRequestMethodsCode&httpRequestMethod.getCode())?true:false;
		}
		return result;
	}

	/**
	 * @return the httpRequestMethodsCode
	 */
	public int getHttpRequestMethodsCode() {
		return httpRequestMethodsCode;
	}
}
