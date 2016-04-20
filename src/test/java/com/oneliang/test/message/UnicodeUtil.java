package com.oneliang.test.message;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.oneliang.util.common.Unicode;

public class UnicodeUtil {

	public static void main(String[] args) throws Exception{
		FileInputStream fileInputStream=new FileInputStream("D:/Dandelion/workspace/frame-common-xml/src/com/lwx/test/message/language_zh_CN.properties");
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream,"UTF-8"));
		StringBuilder data=new StringBuilder();
		String str=null;
		while((str=bufferedReader.readLine())!=null){
			data.append(Unicode.toUnicode(str)+"\r\n");
		}
		bufferedReader.close();
		OutputStreamWriter outputStreamWriter=new OutputStreamWriter(new FileOutputStream("D:/Dandelion/workspace/frame-common-xml/src/com/lwx/test/message/language_zh_CN.properties.copy"));
		outputStreamWriter.write(data.toString());
		outputStreamWriter.flush();
		outputStreamWriter.close();
	}
}
