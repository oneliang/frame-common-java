package com.oneliang.frame.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oneliang.Constants;
import com.oneliang.util.common.ObjectUtil;
import com.oneliang.util.logging.Logger;
import com.oneliang.util.logging.LoggerManager;

public class Forest<T extends Object> {

	private static final Logger logger=LoggerManager.getLogger(Forest.class);

	protected List<Tree<T>> treeList=new CopyOnWriteArrayList<Tree<T>>();

	/**
	 * create tree root list,like forest,many roots in the the list,but the root
	 * has no child
	 * 
	 * @param list
	 * @param fartherFieldName
	 * @param fartherValueSet
	 * @return List<TreeNode<T>>
	 */
	protected List<TreeNode<T>> createTreeRootList(List<T> list,String fartherFieldName, Object[] fartherValueSet) {
		List<TreeNode<T>> treeRootList = new ArrayList<TreeNode<T>>();
		try {
			for (T object : list) {
				if(fartherFieldName!=null&&fartherFieldName.length()>0){
					String fartherMethodName=ObjectUtil.fieldNameToMethodName(Constants.Method.PREFIX_GET, fartherFieldName);
					Method fartherMethod=object.getClass().getMethod(fartherMethodName,new Class[]{});
					Object value = fartherMethod.invoke(object, new Object[]{});
					if (Tree.checkValue(value, fartherValueSet)) {
						TreeNode<T> node = new TreeNode<T>(object);
						treeRootList.add(node);
					}
				}
			}
		} catch (Exception e) {
			logger.error(Constants.Base.EXCEPTION, e);
		}
		return treeRootList;
	}
	
	/**
	 * create the forest
	 * @param list
	 * @param fartherFieldName
	 * @param fartherValueSet
	 * @param childFieldName
	 * @return List<Tree<T>>
	 */
	public List<Tree<T>> createForest(List<T> list, String fartherFieldName, Object[] fartherValueSet,String childFieldName){
		List<TreeNode<T>> rootList=this.createTreeRootList(list, fartherFieldName, fartherValueSet);
		for(TreeNode<T> root:rootList){
			Tree<T> tree=new Tree<T>();
			root=tree.createTree(root, list, fartherFieldName, fartherValueSet, childFieldName);
			this.treeList.add(tree);
		}
		return this.treeList;
	}

	/**
	 * @return the treeList
	 */
	public List<Tree<T>> getTreeList() {
		return treeList;
	}
}
