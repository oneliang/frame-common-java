package com.oneliang.frame.workflow;

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

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AbstractContext;
import com.oneliang.util.common.JavaXmlUtil;
import com.oneliang.util.common.StringUtil;

public class TaskContext extends AbstractContext {

	private Task task=null;
	private Map<String,TaskNodeBean> taskNodeBeanMap=new ConcurrentHashMap<String,TaskNodeBean>();
	private Map<String,ProcessorBean> processorBeanMap=new ConcurrentHashMap<String,ProcessorBean>();

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
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
				//processor
				NodeList processorElementList=root.getElementsByTagName(ProcessorBean.TAG_PROCESSOR);
				if(processorElementList!=null){
					int length=processorElementList.getLength();
					for(int index=0;index<length;index++){
						ProcessorBean processorBean=new ProcessorBean();
						Node processorElement=processorElementList.item(index);
						NamedNodeMap attributeMap=processorElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(processorBean, attributeMap);
						Processor processor=(Processor)(this.classLoader.loadClass(processorBean.getType()).newInstance());
						processorBean.setProcessorInstance(processor);
						String processorBeanId=processorBean.getId();
						processorBeanMap.put(processorBeanId, processorBean);
						if(!objectMap.containsKey(processorBeanId)){
							objectMap.put(processorBeanId, processor);
						}
					}
				}
				//taskNode
				NodeList taskNodeElementList=root.getElementsByTagName(TaskNodeBean.TAG_TASKNODE);
				if(taskNodeElementList!=null){
					int length=taskNodeElementList.getLength();
					for(int index=0;index<length;index++){
						TaskNodeBean taskNodeBean=new TaskNodeBean();
						Node taskNodeElement=taskNodeElementList.item(index);
						NamedNodeMap attributeMap=taskNodeElement.getAttributes();
						JavaXmlUtil.initializeFromAttributeMap(taskNodeBean, attributeMap);
						taskNodeBeanMap.put(taskNodeBean.getId(), taskNodeBean);
						TaskNode taskNode=new TaskNode();
						taskNode.setId(taskNodeBean.getId());
						taskNode.setCommand(taskNodeBean.getCommand());
						taskNode.setName(taskNodeBean.getName());
						taskNodeBean.setTaskNodeInstance(taskNode);
						NodeList childNodeList=taskNodeElement.getChildNodes();
						if(childNodeList!=null){
							int childNodeListLength=childNodeList.getLength();
							for(int childNodeListIndex=0;childNodeListIndex<childNodeListLength;childNodeListIndex++){
								Node childNodeElement=childNodeList.item(childNodeListIndex);
								String nodeName=childNodeElement.getNodeName();
								if(nodeName.equals(NextTaskNodeBean.TAG_NEXTTASKNODE)){
									NextTaskNodeBean nextTaskNodeBean=new NextTaskNodeBean();
									NamedNodeMap nextTaskNodeBeanAttributeMap=childNodeElement.getAttributes();
									JavaXmlUtil.initializeFromAttributeMap(nextTaskNodeBean, nextTaskNodeBeanAttributeMap);
									taskNodeBean.addNextTaskNodeBean(nextTaskNodeBean);
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}

	/**
	 * destory
	 */
	public void destroy(){
		taskNodeBeanMap.clear();
		processorBeanMap.clear();
	}

	/**
	 * processor inject
	 */
	public void processorInject(){
		//task node processor initial
		Iterator<Entry<String,TaskNodeBean>> iterator=taskNodeBeanMap.entrySet().iterator();
		while(iterator.hasNext()){
		    Entry<String,TaskNodeBean> entry=iterator.next();
			TaskNodeBean taskNodeBean=entry.getValue();
			String processorId=taskNodeBean.getProcessorId();
			if(objectMap.containsKey(processorId)){
				TaskNode taskNode=taskNodeBean.getTaskNodeInstance();
				if(taskNode!=null){
					taskNode.setProcessor((Processor)objectMap.get(processorId));
				}
			}
		}
		//after processor inject in task node,generate the task
		generateTask();
	}
	
	/**
	 * generate the task
	 */
	private void generateTask(){
		this.task=new Task();
		Iterator<Entry<String,TaskNodeBean>> iterator=taskNodeBeanMap.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String,TaskNodeBean> entry=iterator.next();
            TaskNodeBean taskNodeBean=entry.getValue();
			TaskNode taskNode=taskNodeBean.getTaskNodeInstance();
			taskNode.setTask(this.task);
			if(taskNodeBean.getIsRoot()){
				task.addRootTaskNode(taskNode);
			}
			List<NextTaskNodeBean> nextTaskNodeBeanList=taskNodeBean.getNextTaskNodeBeanList();
			if(nextTaskNodeBeanList!=null){
				for(NextTaskNodeBean nextTaskNodeBean:nextTaskNodeBeanList){
					String nextTaskNodeBeanId=nextTaskNodeBean.getId();
					TaskNodeBean tempTaskNodeBean=taskNodeBeanMap.get(nextTaskNodeBeanId);
					taskNode.addNextTaskNode(tempTaskNodeBean.getTaskNodeInstance());
				}
			}
		}
	}
	
	/**
	 * get task clone
	 * @return Task
	 */
	public Task getTaskClone(){
		Task task=null;
		if(this.task!=null){
			task=this.task.clone();
		}
		return task;
	}
}
