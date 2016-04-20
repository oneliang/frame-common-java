package com.oneliang.frame.bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * class menu node like tree node
 * @author Dandelion
 * @since 2008-10-17
 */
public class TreeNode<T extends Object>{

	private TreeNode<T> fartherNode=null;
	private T object=null;
	private List<TreeNode<T>> childNodeList=new CopyOnWriteArrayList<TreeNode<T>>();
	private int depth=0;
	
	/**
	 * <p>Method: constructor of TreeNode</p>
	 * @param object
	 */
	public TreeNode(T object){
		this.object=object;
	}
	
	/**
	 * the tree node is root or not
	 * @return true or false
	 */
	public boolean isRoot(){
		boolean result=false;
		if(this.fartherNode==null){
			result=true;
		}
		return result;
	}
	
	/**
	 * the tree node is leaf or not
	 * @return true or false
	 */
	public boolean isLeaf(){
		boolean result=false;
		if(this.childNodeList.isEmpty()){
			result=true;
		}
		return result;
	}
	
	/**
	 * <p>Method: add a TreeNode</p>
	 * @param treeNode
	 * @return this.TreeNode<T>
	 */
	public TreeNode<T> addTreeNode(TreeNode<T> treeNode){
		if(this.equals(treeNode)){
			throw new RuntimeException("it can not add itself!");
		}
		childNodeList.add(treeNode);
		return this;
	}
	
	/**
	 * <p>Method: remove a TreeNode</p>
	 * @param treeNode
	 * @return this.TreeNode<T>
	 */
	public TreeNode<T> removeTreeNode(TreeNode<T> treeNode){
		if(this.equals(treeNode)){
			throw new RuntimeException("it can not remove itself!");
		}
		childNodeList.remove(treeNode);
		return this;
	}
	
	/**
	 * @return the fartherNode
	 */
	public TreeNode<T> getFartherNode() {
		return fartherNode;
	}
	/**
	 * @param fartherNode the fartherNode to set
	 */
	public void setFartherNode(TreeNode<T> fartherNode) {
		this.fartherNode = fartherNode;
	}
	/**
	 * @return the object
	 */
	public T getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(T object) {
		this.object = object;
	}
	/**
	 * @return the childNodeList
	 */
	public List<TreeNode<T>> getChildNodeList() {
		return childNodeList;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}
