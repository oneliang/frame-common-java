package com.oneliang.frame.bean;

import java.util.List;

public class HtmlForest<T extends Object> extends Forest<T> {

	/**
	 * create the forest
	 * @param list
	 * @param fartherFieldName
	 * @param fartherValueSet
	 * @param childFieldName
	 * @return List<Tree<T>>
	 */
	public List<Tree<T>> createForest(List<T> list, String fartherFieldName, Object[] fartherValueSet, String childFieldName) {
		List<TreeNode<T>> rootList=this.createTreeRootList(list, fartherFieldName, fartherValueSet);
		for(TreeNode<T> root:rootList){
			HtmlTree<T> tree=new HtmlTree<T>();
			root=tree.createTree(root, list, fartherFieldName, fartherValueSet, childFieldName);
			this.treeList.add(tree);
		}
		return this.treeList;
	}
	
	/**
	 * html forest
	 * @param label
	 * @param nodeFields
	 * @param nodeStyles
	 * @param subDivFields
	 * @param subDivStyles
	 * @param leafField
	 * @param jsOpenFunction
	 * @return generate html forest
	 */
	public String generateHtmlForest(String label, String[] nodeFields, String[] nodeStyles, String[] subDivFields, String[] subDivStyles, String leafField, String jsOpenFunction) {
		StringBuilder string = new StringBuilder();
		for(Tree<T> tree:this.treeList){
			string.append(((HtmlTree<T>)tree).generateHtmlTree(label, nodeFields, nodeStyles, subDivFields, subDivStyles, leafField, jsOpenFunction));
		}
		return string.toString();
	}
}
