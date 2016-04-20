package com.oneliang.test.encrypt;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.oneliang.util.common.StringUtil;
import com.oneliang.util.encrypt.DecryptProcessor;
import com.oneliang.util.encrypt.Encrypt;
import com.oneliang.util.encrypt.EncryptProcessor;
import com.oneliang.util.file.FileUtil;

public class TestEncrypt implements EncryptProcessor,DecryptProcessor{

	public String encryptProcess(String source) throws Exception{
		if(source!=null){
			long time=System.currentTimeMillis();
			char[] charArray=source.toCharArray();
			for(char c:charArray){
				System.out.print(Integer.toHexString(c).toUpperCase());
			}
		}
		return source;
	}
	
	public String decryptProcess(String source) throws Exception {
		return null;
	}
	
	public static void main(String[] args){
		StringBuilder string=new StringBuilder();
		string.append("VERSION:1.0;");
		string.append("BUSES:2000;");
		string.append("梁文翔");
		System.out.println(string.toString());
		String licenseFile="E:/LICENSE.txt";
		try {
			FileUtil.createFile(licenseFile);
			OutputStream outputStream=new FileOutputStream(licenseFile);
			String licenseString=Encrypt.encrypt(string.toString(), new TestEncrypt());
			outputStream.write(StringUtil.nullToBlank(licenseString).getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
