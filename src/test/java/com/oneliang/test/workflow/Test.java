package com.oneliang.test.workflow;

import com.oneliang.frame.workflow.TaskNode;
import com.oneliang.frame.workflow.TaskNodeRelation;

public class Test {
	public static void main(String[] args){
		TaskNode t1=new TaskNode();
		t1.setId("1");
		t1.setName("T1");
		TaskNode t2=new TaskNode();
		t2.setId("2");
		t2.setName("T2");
		TaskNode t3=new TaskNode();
		t3.setId("3");
		t3.setName("T3");
		TaskNode t4=new TaskNode();
		t4.setId("4");
		t4.setName("T4");
		TaskNode t5=new TaskNode();
		t5.setId("5");
		t5.setName("T5");
		
		/**
		 *   1 2 3 4 5
		 * 1 0 1 0 0 0
		 * 2 0 0 1 1 0
		 * 3 1 0 0 0 1
		 * 4 0 0 0 0 1
		 * 5 0 0 0 0 0
		 */
		TaskNode[] taskNode={t1,t2,t3,t4,t5};
		TaskNodeRelation[][] relation=new TaskNodeRelation[taskNode.length][taskNode.length];
		/**
		 * initial the relation array
		 */
		for(int i=0;i<relation.length;i++){
			for(int j=0;j<relation[i].length;j++){
				relation[i][j]=new TaskNodeRelation();
			}
		}
		
		relation[0][1].setRelative(true);
		relation[1][2].setRelative(true);
		relation[1][3].setRelative(true);
		relation[2][0].setRelative(true);
		relation[2][4].setRelative(true);
		relation[3][4].setRelative(true);
		
		for(int i=0;i<relation.length;i++){
			for(int j=0;j<relation[i].length;j++){
				System.out.print(relation[i][j].getRelative()+" ");
			}
			System.out.println();
		}
		
		StringBuilder stringBuffer=new StringBuilder();
		stringBuffer.append("<root>");
		for(int i=0;i<relation.length;i++){
			stringBuffer.append("<node id=\""+taskNode[i].getId()+"\" ");
			stringBuffer.append("name=\""+taskNode[i].getName()+"\">");
			for(int j=0;j<relation[i].length;j++){
				if(relation[i][j].getRelative()){
					stringBuffer.append("<node id=\""+taskNode[j].getId()+"\" ");
					stringBuffer.append("name=\""+taskNode[j].getName()+"\">");
					stringBuffer.append(taskNode[j].getName());
					stringBuffer.append("</node>");
				}
			}
			stringBuffer.append("</node>");
		}
		stringBuffer.append("</root>");
		System.out.println(stringBuffer.toString());
	}
}
