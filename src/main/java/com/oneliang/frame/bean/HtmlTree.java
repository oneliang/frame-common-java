package com.oneliang.frame.bean;

import java.util.List;

import com.oneliang.util.common.TagUtil;

public class HtmlTree<T extends Object> extends Tree<T> {

	/**
	 * html tree
	 * @param label
	 * @param nodeFields
	 * @param nodeStyles
	 * @param subDivFields
	 * @param subDivStyles
	 * @param leafField
	 * @param jsOpenFunction
	 * @return generate html tree
	 */
	public String generateHtmlTree(String label,String[] nodeFields,
			String[] nodeStyles, String[] subDivFields, String[] subDivStyles,
			String leafField, String jsOpenFunction) {
		return this.generateHtmlTree(this.root, label, nodeFields, nodeStyles, subDivFields, subDivStyles, leafField, jsOpenFunction);
	}
	
	/**
	 * html tree
	 * @param root
	 * @param label
	 * @param nodeFields
	 * @param nodeStyles
	 * @param subDivFields
	 * @param subDivStyles
	 * @param leafField
	 * @param jsOpenFunction
	 * @return generate html tree
	 */
	private String generateHtmlTree(TreeNode<T> root, String label, String[] nodeFields, String[] nodeStyles, String[] subDivFields, String[] subDivStyles, String leafField, String jsOpenFunction) {
		StringBuilder string = new StringBuilder();
		T object = root.getObject();
		int depth = root.getDepth();
		if (!root.isLeaf()) {
			String nodeField = nodeFields[depth];
			String nodeStyle = nodeStyles[depth];
			nodeField = TagUtil.fieldReplace(nodeField, object, null);
			nodeStyle = " " + TagUtil.fieldReplace(nodeStyle, object, null);
			String openFunction = jsOpenFunction;
			openFunction = TagUtil.fieldReplace(openFunction, object, null);
			String div = "<"+label+nodeStyle + " " + openFunction + ">"
					+ nodeField + "</"+label+">";
			string.append(div);

			String divField = subDivFields[depth];
			String divStyle = subDivStyles[depth];
			divField = TagUtil.fieldReplace(divField, object, null);
			divStyle = " " + TagUtil.fieldReplace(divStyle, object, null);
			string.append("<"+label+divStyle + ">");
			string.append(divField);
			List<TreeNode<T>> childList = root.getChildNodeList();
			for (TreeNode<T> node : childList) {
				string.append(generateHtmlTree(node,label,nodeFields,nodeStyles,subDivFields,subDivStyles,leafField,jsOpenFunction));
			}
			string.append("</"+label+">");
		} else {
			String leafStyle = nodeStyles[depth];
			leafField = TagUtil.fieldReplace(leafField, object, null);
			leafStyle = " " + TagUtil.fieldReplace(leafStyle, object, null);
			String div = "<"+label+leafStyle + ">" + leafField + "</"+label+">";
			string.append(div);
		}
		return string.toString();
	}

	public static void main(String[] args) {
		// Tree<TreeNode<Object>> tree = new Tree<TreeNode<Object>>();
		String[] fartherValueSet = { null, "" };
		String fartherField = "";
		for (String value : fartherValueSet) {
			if (fartherField == value || fartherField.equals(value)) {
			}
		}
	}
}
