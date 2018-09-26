package com.oneliang.frame.servlet.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oneliang.Constants;
import com.oneliang.StaticVar;
import com.oneliang.frame.servlet.ActionUtil;

public abstract class AbstractFileDownloadAction extends CommonAction{

	/**
	 * download file
	 * @param filename
	 * @return boolean
	 * @throws ActionExecuteException
	 */
	protected boolean download(String filename) throws ActionExecuteException {
		HttpServletRequest request=(HttpServletRequest)ActionUtil.getServletRequest();
		HttpServletResponse response=(HttpServletResponse)ActionUtil.getServletResponse();
		return this.download(request, response, filename);
	}
	
	/**
	 * download file
	 * @param request
	 * @param response
	 * @param filename
	 * @throws ActionExecuteException
	 */
	protected boolean download(ServletRequest request,ServletResponse response,String filename) throws ActionExecuteException{
		boolean result=false;
		response.setContentType(Constants.Http.ContentType.APPLICATION_X_DOWNLOAD);
		String filePath=StaticVar.DOWNLOAD_FOLDER+filename;
		InputStream inputStream=null;
		OutputStream outputStream=null;
		try {
			
			String newFilename=new String(filename.getBytes(Constants.Encoding.GB2312),Constants.Encoding.ISO88591);
			((HttpServletResponse)response).addHeader(Constants.Http.HeaderKey.CONTENT_DISPOSITION, "attachment;filename="+ newFilename);
			outputStream=response.getOutputStream();
			inputStream=new FileInputStream(filePath);
			byte[] buffer=new byte[Constants.Capacity.BYTES_PER_KB];
			int length=-1;
			while((length=inputStream.read(buffer,0,buffer.length))!=-1){
				outputStream.write(buffer,0,length);
			}
			outputStream.flush();
			result=true;
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		} finally {
			try{
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
			}catch (Exception e) {
				throw new ActionExecuteException(e);
			}
		}
		return result;
	}
}
