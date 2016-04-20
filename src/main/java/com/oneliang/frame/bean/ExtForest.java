package com.oneliang.frame.bean;

import java.util.List;

import com.oneliang.Constant;

public class ExtForest<T extends Object> extends Forest<T> {

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
			ExtTree<T> tree=new ExtTree<T>();
			root=tree.createTree(root, list, fartherFieldName, fartherValueSet, childFieldName);
			this.treeList.add(tree);
		}
		return this.treeList;
	}
	
	/**
	 * generate json forest
	 * @param idField
	 * @param textField
	 * @param hrefField
	 * @return String
	 */
	public String generateJsonForest(String idField,String textField,String hrefField,String hrefTarget){
		StringBuilder string=new StringBuilder();
		int index=0;
		int lastIndex=this.treeList.size()-1;
		for(Tree<T> tree:this.treeList){
			string.append(((ExtTree<T>)tree).generateJsonTree(idField, textField, hrefField,hrefTarget));
			if(index<lastIndex){
				string.append(Constant.Symbol.COMMA);
			}
			index++;
		}
		return Constant.Symbol.MIDDLE_BRACKET_LEFT+string.toString()+Constant.Symbol.MIDDLE_BRACKET_RIGHT;
	}
}
