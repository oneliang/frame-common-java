package com.oneliang.frame.uri;

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

import com.oneliang.Constant;
import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.StringUtil;

public class UriMappingContext extends AbstractContext {

	private static final String REGEX = "\\{[\\w]*\\}";
	private static final String FIRST_REGEX="\\{";

	protected static final Map<String,UriMappingBean> uriMappingBeanMap=new ConcurrentHashMap<String,UriMappingBean>();

	public void initialize(String parameters) {
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
				NodeList uriBeanElementList=root.getElementsByTagName(UriMappingBean.TAG_URI);
				if(uriBeanElementList!=null){
					int length=uriBeanElementList.getLength();
					for(int index=0;index<length;index++){
						Node beanElement=uriBeanElementList.item(index);
						UriMappingBean uriMappingBean=new UriMappingBean();
						NamedNodeMap attributeMap=beanElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(uriMappingBean, attributeMap);
						uriMappingBeanMap.put(uriMappingBean.getFrom(), uriMappingBean);
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * find uri to
	 * @param uriFrom
	 * @return String
	 */
	public static String findUriTo(String uriFrom){
		String uriTo=null;
		Iterator<Entry<String,UriMappingBean>> iterator=uriMappingBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,UriMappingBean> entry=iterator.next();
			String from=entry.getKey();
			String fromRegex=Constant.Symbol.XOR+from+Constant.Symbol.DOLLAR;
			if(StringUtil.isMatchRegex(uriFrom, fromRegex)){
				uriTo=entry.getValue().getTo();
				List<String> groupList=StringUtil.parseRegexGroup(uriFrom, fromRegex);
				List<String> parameterList=StringUtil.parseStringGroup(uriTo, REGEX, FIRST_REGEX, StringUtil.BLANK, 1);
				for(String parameter:parameterList){
					uriTo=uriTo.replaceFirst(REGEX,groupList.get(Integer.parseInt(parameter)));
				}
				break;
			}
		}
		return uriTo;
	}

	public void destroy() {
		uriMappingBeanMap.clear();
	}
}
