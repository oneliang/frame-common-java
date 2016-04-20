package com.oneliang.frame.bean;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.oneliang.Constant;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.json.JsonUtil;
import com.oneliang.util.json.JsonUtil.JsonProcessor;

public class JsonTree<T extends Object> extends Tree<T> {

	/**
	 * json tree,the name of has children field
	 */
	public String hasChildren="hasChildren";
	/**
	 * json tree ,the name of children's field
	 */
	public String children="children";
	
	/**
	 * json tree,key means the json's properties,value means the value of object's field
	 * @param keyValueMap
	 * @return String
	 */
	public String generateJsonTree(Map<String,String> keyValueMap){
		return generateJsonTree(this.root,keyValueMap,JsonUtil.DEFAULT_JSON_PROCESSOR);
	}

	/**
	 * json tree,key means the json's properties,value means the value of object's field
	 * @param keyValueMap
	 * @param jsonProcessor
	 * @return String
	 */
	public String generateJsonTree(Map<String,String> keyValueMap,JsonProcessor jsonProcessor){
		return generateJsonTree(this.root,keyValueMap,jsonProcessor);
	}

	/**
	 * json tree,key means the json's properties,value means the value of object's field
	 * @param root
	 * @param keyValueMap
	 * @param jsonProcessor
	 * @return String
	 */
	private String generateJsonTree(TreeNode<T> root,Map<String,String> keyValueMap,JsonProcessor jsonProcessor){
		String string=null;
		StringBuilder stringBuilder=new StringBuilder();
		T object = root.getObject();
		Iterator<Entry<String,String>> iterator=keyValueMap.entrySet().iterator();
		stringBuilder.append(Constant.Symbol.BIG_BRACKET_LEFT);
		while(iterator.hasNext()){
			Entry<String,String> entry=iterator.next();
		    String key=entry.getKey();
			String fieldName=entry.getValue();
			Object methodReturnValue=ObjectUtil.getterOrIsMethodInvoke(object, fieldName);
			if(jsonProcessor!=null){
				methodReturnValue=jsonProcessor.process(fieldName,methodReturnValue,false);
			}
			stringBuilder.append(Constant.Symbol.DOUBLE_QUOTES+key+Constant.Symbol.DOUBLE_QUOTES+Constant.Symbol.COLON+methodReturnValue.toString());
            if(iterator.hasNext()){
            	stringBuilder.append(Constant.Symbol.COMMA);
            }
		}
		if(!root.isLeaf()){
			List<TreeNode<T>> childList = root.getChildNodeList();
			stringBuilder.append(Constant.Symbol.COMMA+Constant.Symbol.DOUBLE_QUOTES+hasChildren+Constant.Symbol.DOUBLE_QUOTES+Constant.Symbol.COLON+true);
			stringBuilder.append(Constant.Symbol.COMMA+Constant.Symbol.DOUBLE_QUOTES+children+Constant.Symbol.DOUBLE_QUOTES+Constant.Symbol.COLON+Constant.Symbol.MIDDLE_BRACKET_LEFT);
			int index=0;
			int lastIndex=childList.size()-1;
			for (TreeNode<T> node : childList) {
				stringBuilder.append(generateJsonTree(node,keyValueMap,jsonProcessor));
				if(index<lastIndex){
					stringBuilder.append(Constant.Symbol.COMMA);
				}
				index++;
			}
			stringBuilder.append(Constant.Symbol.MIDDLE_BRACKET_RIGHT);
		}else{
			stringBuilder.append(Constant.Symbol.COMMA+Constant.Symbol.DOUBLE_QUOTES+hasChildren+Constant.Symbol.DOUBLE_QUOTES+Constant.Symbol.COLON+false);
		}
		stringBuilder.append(Constant.Symbol.BIG_BRACKET_RIGHT);
		string=stringBuilder.toString();
		return string;
	}
}
