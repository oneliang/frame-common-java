package com.oneliang.test.validate;

import java.util.List;

import com.oneliang.util.validate.Validator;
import com.oneliang.util.validate.Validator.ViolateConstrain;

public class TestValidator {

	public static void main(String[] args) throws Exception {
		Test test=new Test();
		test.setTest("test12");
		List<ViolateConstrain> violateConstrainList=Validator.validate(test);
		if(violateConstrainList!=null&&!violateConstrainList.isEmpty()){
			for(ViolateConstrain violateConstrain:violateConstrainList){
				System.out.println(violateConstrain.getResult());
			}
		}
	}
}
