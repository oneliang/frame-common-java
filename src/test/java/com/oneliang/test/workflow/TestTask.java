package com.oneliang.test.workflow;

import com.oneliang.frame.workflow.TaskContext;

public class TestTask {

	public static void main(String[] args){
		String path="bin/com/lwx/frame/config/task.xml";
		TaskContext taskContext=new TaskContext();
		try {
			taskContext.initialize(path);
			taskContext.processorInject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(taskContext.getTaskClone());
	}
}
