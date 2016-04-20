package com.oneliang.test.autogen.protobuf;

import java.io.File;

import com.oneliang.frame.autogen.protobuf.ProtobufAutoGenerateContext;
import com.oneliang.util.common.StringUtil;

public class ProtobufAutoGenerate {

	public static void main(String[] args) {
		try {
			String projectRealPath = new File(StringUtil.BLANK).getAbsolutePath();
			String classRealPath = projectRealPath + "/src";
			
			ProtobufAutoGenerateContext protobufAutoGenerateContext = new ProtobufAutoGenerateContext();
			protobufAutoGenerateContext.setProjectRealPath(projectRealPath);
			protobufAutoGenerateContext.setClassesRealPath(classRealPath);
			String templateFile="/src/com/lwx/frame/autogen/protobuf/protobuf.tmpl";
			String toFolder="/resource";
			String protoFile="/com/lwx/test/autogen/protobuf/ExampleProtoFile.proto";
			protobufAutoGenerateContext.initialize("-T="+templateFile+",-P="+protoFile+",-TO="+toFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
