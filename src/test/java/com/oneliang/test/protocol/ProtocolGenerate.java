package com.oneliang.test.protocol;

import java.util.ArrayList;
import java.util.List;

import com.oneliang.Constants;
import com.oneliang.test.protocol.ProtocolTemplateBean.Field;
import com.oneliang.util.generate.Template;
import com.oneliang.util.generate.Template.Parameter;

public class ProtocolGenerate {

	public static void main(String[] args) {
		final String templateFile = "/D:/Dandelion/java/workspace/frame-common-java/src/com/lwx/test/protocol/protocol.tmpl";
		final String toDirectory = "/D:/Dandelion/java/workspace/frame-common-java/src/";
		Template template = new Template();
        Parameter parameter = new Parameter();
		ProtocolTemplateBean bean = new ProtocolTemplateBean();
		bean.setClassName("Protocol");
		bean.setPackageName("com.lwx.test.protocol");
		List<Field> fieldList = new ArrayList<Field>();
		Field field = new Field();
		field.setType("byte");
		field.setName("headLen");
		field.setValue("0");
		field.setByteCount(1);
		fieldList.add(field);

		field = new Field();
		field.setType("byte[]");
		field.setName("head");
		field.setValue("null");
		field.setByteCount(2);
		fieldList.add(field);

		field = new Field();
		field.setType("byte");
		field.setName("dataLen");
		field.setValue("0");
		field.setByteCount(1);
		fieldList.add(field);

		field = new Field();
		field.setType("byte[]");
		field.setName("dataHead");
		field.setValue("null");
		field.setByteCount(4);
		fieldList.add(field);

		field = new Field();
		field.setType("byte");
		field.setName("type");
		field.setValue("-1");
		field.setByteCount(1);
		fieldList.add(field);

		field = new Field();
		field.setType("byte[]");
		field.setName("tagInfo");
		field.setValue("null");
		field.setByteCount(4);
		fieldList.add(field);

		field = new Field();
		field.setType("byte[]");
		field.setName("random");
		field.setValue("null");
		field.setByteCount(4);
		fieldList.add(field);

		field = new Field();
		field.setType("byte[]");
		field.setName("time");
		field.setValue("null");
		field.setByteCount(6);
		fieldList.add(field);

		field = new Field();
		field.setType("byte");
		field.setName("battery");
		field.setValue("0");
		field.setByteCount(1);
		fieldList.add(field);

		field = new Field();
		field.setType("byte");
		field.setName("txPower");
		field.setValue("0");
		field.setByteCount(1);
		fieldList.add(field);
		bean.setFieldList(fieldList);
		parameter.setTemplateFile(templateFile);
		parameter.setToFile(toDirectory + bean.getPackageName().replace(Constants.Symbol.DOT, Constants.Symbol.SLASH_LEFT) + Constants.Symbol.SLASH_LEFT + bean.getClassName() + ".java");
		parameter.setObject(bean);
		System.out.println(parameter.getToFile());
		template.generate(parameter);
	}
}
