package com.oneliang.frame.servlet.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oneliang.Constants;
import com.oneliang.StaticVar;
import com.oneliang.frame.servlet.ActionUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.upload.FileUpload;
import com.oneliang.util.upload.FileUploadResult;

public abstract class AbstractFileUploadAction extends CommonAction{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4867033632772727310L;

	/**
	 * file upload from request,just for form submit
	 * @return List<FileUploadResult>
	 * @throws Exception
	 */
	protected List<FileUploadResult> upload() throws Exception {
		return this.upload(null);
	}

	/**
	 * file upload from request,just for form submit
	 * @param saveFilenames
	 * @return List<FileUploadResult>
	 * @throws ActionExecuteException
	 */
	protected List<FileUploadResult> upload(String[] saveFilenames) throws ActionExecuteException {
		HttpServletRequest request=(HttpServletRequest)ActionUtil.getServletRequest();
		HttpServletResponse response=(HttpServletResponse)ActionUtil.getServletResponse();
		return this.upload(request,response,saveFilenames);
	}
	
	/**
	 * file upload from request,just for form submit
	 * @param request
	 * @param response
	 * @return List<FileUploadResult>
	 * @throws ActionExecuteException
	 */
	protected List<FileUploadResult> upload(ServletRequest request, ServletResponse response) throws ActionExecuteException {
		return upload(request,response,null);
	}

	/**
	 * file upload from request.getInputStream(),for inputStream submit
	 * @param request
	 * @param response
	 * @param saveFilenames
	 * @return List<FileUploadResult>
	 * @throws ActionExecuteException
	 */
	protected List<FileUploadResult> upload(ServletRequest request, ServletResponse response, String[] saveFilenames) throws ActionExecuteException {
		return this.upload(request, response, null, saveFilenames);
	}

	/**
	 * file upload from request.getInputStream(),for inputStream submit
	 * @param request
	 * @param response
	 * @param fileFullName
	 * @return List<FileUploadResult>
	 * @throws ActionExecuteException
	 */
	protected List<FileUploadResult> upload(ServletRequest request, ServletResponse response, String fileFullName, String[] saveFilenames) throws ActionExecuteException {
		response.setContentType(Constants.Http.ContentType.TEXT_PLAIN);
		// get content type of client request
		String contentType = request.getContentType();
		List<FileUploadResult> fileUploadResultList=null;
		try{
			if(contentType!=null) {
				InputStream inputStream=request.getInputStream();
				FileUpload fileUpload=new FileUpload();
				String filePath=StaticVar.UPLOAD_FOLDER;
				fileUpload.setSaveFilePath(filePath);
				// make sure content type is multipart/form-data,form file use
				
				if(contentType.indexOf(Constants.Http.ContentType.MULTIPART_FORM_DATA)>=0){
					fileUploadResultList=fileUpload.upload(inputStream,request.getContentLength(),saveFilenames);
				}
				// make sure content type is application/octet-stream or binary/octet-stream,flash jpeg picture use or file upload use
				else if(contentType.indexOf(Constants.Http.ContentType.APPLICATION_OCTET_STREAM)>=0||contentType.indexOf(Constants.Http.ContentType.BINARY_OCTET_STREAM)>=0){
					if(StringUtil.isNotBlank(fileFullName)){
						fileUploadResultList=new ArrayList<FileUploadResult>();
						FileUploadResult fileUploadResult=fileUpload.upload(inputStream, fileFullName);
						fileUploadResultList.add(fileUploadResult);
					}
				}
			}
		}catch (Exception e) {
			throw new ActionExecuteException(e);
		}
		return fileUploadResultList;
	}
}
