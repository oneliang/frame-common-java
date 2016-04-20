package com.oneliang.frame.bean;

import java.util.List;
import java.util.Map;

import com.oneliang.Constant;
import com.oneliang.util.json.JsonUtil;
import com.oneliang.util.json.JsonUtil.JsonProcessor;

public class JsonForest<T extends Object> extends Forest<T> {

	/**
	 * json tree,the name of has children field
	 */
	public String hasChildren="hasChildren";
	/**
	 * json forest ,the name of children's field
	 */
	public String children="children";
	
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
			JsonTree<T> tree=new JsonTree<T>();
			tree.hasChildren=this.hasChildren;
			tree.children=this.children;
			root=tree.createTree(root, list, fartherFieldName, fartherValueSet, childFieldName);
			this.treeList.add(tree);
		}
		return this.treeList;
	}

	/**
	 * json forest,key means the json's properties,value means the value of object's field
	 * @param keyValueMap
	 * @return String
	 */
	public String generateJsonForest(Map<String,String> keyValueMap){
		return generateJsonForest(keyValueMap, JsonUtil.DEFAULT_JSON_PROCESSOR);
	}

	/**
	 * json forest,key means the json's properties,value means the value of object's field
	 * @param keyValueMap
	 * @param jsonProcessor
	 * @return String
	 */
	public String generateJsonForest(Map<String,String> keyValueMap,JsonProcessor jsonProcessor){
		StringBuilder string=new StringBuilder();
		int index=0;
		int lastIndex=this.treeList.size()-1;
		for(Tree<T> tree:this.treeList){
			string.append(((JsonTree<T>)tree).generateJsonTree(keyValueMap,jsonProcessor));
			if(index<lastIndex){
				string.append(Constant.Symbol.COMMA);
			}
			index++;
		}
		return Constant.Symbol.MIDDLE_BRACKET_LEFT+string.toString()+Constant.Symbol.MIDDLE_BRACKET_RIGHT;
	}
}
