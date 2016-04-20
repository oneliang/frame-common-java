package com.oneliang.test.aop;

public class TestBean implements TestInterface{

	private TestInterface testInterface=null;
	public String test(){
		return "test()";
	}
	
	public String setTestInterface(TestInterface testInterface){
		this.testInterface=testInterface;
		return "setTestInterface(TestInterface testInterface)";
	}
}
