package com.oneliang.frame.bean;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.oneliang.util.common.ObjectUtil;

public class Tree<T extends Object>{

	protected TreeNode<T> root=null;

	/**
	 * create tree,the tree has the child node
	 * childFieldName.value==fartherFieldName.value prove the child
	 * 
	 * @param list
	 * @param fartherFieldName
	 * @param fartherValueSet
	 * @param childFieldName
	 * @return the tree node has the child
	 */
	public TreeNode<T> createTree(TreeNode<T> farther, List<T> list, String fartherFieldName, Object[] fartherValueSet, String childFieldName) {
		this.root=farther;
		List<T> copyOnWriteList=new CopyOnWriteArrayList<T>(list);
		Queue<TreeNode<T>> queue=new ConcurrentLinkedQueue<TreeNode<T>>();
		queue.add(farther);
		while(!queue.isEmpty()){
			TreeNode<T> treeNode=queue.poll();
			Object fartherObject = treeNode.getObject();
			for (T object : copyOnWriteList) {
				// parentId,id
				Object fartherIdValue=ObjectUtil.getterOrIsMethodInvoke(fartherObject, childFieldName);
				Object childParentValue=ObjectUtil.getterOrIsMethodInvoke(object, fartherFieldName);
				if (!checkValue(childParentValue, fartherValueSet)) {// prove child
					if (childParentValue!=null&&childParentValue.equals(fartherIdValue)) {
						TreeNode<T> node = new TreeNode<T>(object);
						node.setFartherNode(treeNode);
						node.setDepth(treeNode.getDepth() + 1);
						treeNode.addTreeNode(node);
						queue.add(node);
						copyOnWriteList.remove(object);
					}
				}
			}
		}
		return farther;
	}
	
	/**
	 * private method: check the fieldvalue is values or not
	 * 
	 * @param fieldValue
	 * @param values
	 * @return boolean
	 */
	static boolean checkValue(Object fieldValue, Object[] values) {
		boolean sign=false;
		for (Object value : values) {
			if (fieldValue!=null&&(fieldValue == value || fieldValue.equals(value))) {
				sign=true;
			}else if(fieldValue==null&&value==null){
				sign=true;
			}
		}
		return sign;
	}

	/**
	 * @return the root
	 */
	public TreeNode<T> getRoot() {
		return root;
	}
}
