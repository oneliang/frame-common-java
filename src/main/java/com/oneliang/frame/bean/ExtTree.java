package com.oneliang.frame.bean;

import java.lang.reflect.Method;
import java.util.List;

import com.oneliang.Constant;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.common.StringUtil;

public class ExtTree<T extends Object> extends Tree<T> {
	
	/**
	 * ext tree json field
	 */
	private static final String ID="id";
	private static final String TEXT="text";
	private static final String HREF="href";
	private static final String HREF_TARGET="hrefTarget";
	private static final String LEAF="leaf";
	private static final String CHILDREN="children";

	/**
	 * for ext use,json tree
	 * @param idField
	 * @param textField
	 * @param hrefField
	 * @return String
	 */
	public String generateJsonTree(String idField,String textField,String hrefField,String hrefTarget){
		return this.generateJsonTree(this.root, idField, textField, hrefField, hrefTarget);
	}

	/**
	 * for ext use,json tree
	 * @param root
	 * @param idField
	 * @param textField
	 * @param hrefField
	 * @return String
	 */
	public String generateJsonTree(TreeNode<T> root,String idField,String textField,String hrefField,String hrefTarget){
		String idMethodName=ObjectUtil.fieldNameToMethodName(Constant.Method.PREFIX_GET,idField);
		String textMethodName=ObjectUtil.fieldNameToMethodName(Constant.Method.PREFIX_GET,textField);
		String hrefMethodName=ObjectUtil.fieldNameToMethodName(Constant.Method.PREFIX_GET,hrefField);
		StringBuilder string=new StringBuilder();
		try{
			T object = root.getObject();
			Method method=object.getClass().getMethod(idMethodName, new Class[]{});
			String idValue=ObjectUtil.nullToBlank(method.invoke(object, new Object[]{})).toString();
			method=object.getClass().getMethod(textMethodName, new Class[]{});
			String textValue=ObjectUtil.nullToBlank(method.invoke(object, new Object[]{})).toString();
			String hrefValue=null;
			if(StringUtil.isNotBlank(hrefField)){
				method=object.getClass().getMethod(hrefMethodName, new Class[]{});
				hrefValue=ObjectUtil.nullToBlank(method.invoke(object, new Object[]{})).toString();
			}
			string.append(Constant.Symbol.BIG_BRACKET_LEFT);
			string.append(ID+Constant.Symbol.COLON+"'"+idValue+"'"+Constant.Symbol.COMMA);
			string.append(TEXT+Constant.Symbol.COLON+"'"+textValue+"'"+Constant.Symbol.COMMA);
			if(!root.isLeaf()){
				List<TreeNode<T>> childList = root.getChildNodeList();
				string.append(CHILDREN+Constant.Symbol.COLON+Constant.Symbol.MIDDLE_BRACKET_LEFT);
				int index=0;
				int lastIndex=childList.size()-1;
				for (TreeNode<T> node : childList) {
					string.append(generateJsonTree(node,idField,textField,hrefField,hrefTarget));
					if(index<lastIndex){
						string.append(Constant.Symbol.COMMA);
					}
					index++;
				}
				string.append(Constant.Symbol.MIDDLE_BRACKET_RIGHT);
			}else{
				if(StringUtil.isNotBlank(hrefValue)){
					string.append(HREF+Constant.Symbol.COLON+"'"+hrefValue+"'"+Constant.Symbol.COMMA);
				}
				if(StringUtil.isNotBlank(hrefTarget)){
					string.append(HREF_TARGET+Constant.Symbol.COLON+"'"+hrefTarget+"'"+Constant.Symbol.COMMA);
				}
				string.append(LEAF+Constant.Symbol.COLON+"'"+true+"'");
			}
			string.append(Constant.Symbol.BIG_BRACKET_RIGHT);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return string.toString();
	}
}
