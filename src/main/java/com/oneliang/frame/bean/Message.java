package com.oneliang.frame.bean;

import java.util.List;

import com.oneliang.util.common.ClassUtil;
import com.oneliang.util.common.StringUtil;
import com.oneliang.util.common.ClassUtil.ClassProcessor;
import com.oneliang.util.json.JsonArray;
import com.oneliang.util.json.JsonObject;
import com.oneliang.util.json.JsonUtil;
import com.oneliang.util.json.JsonUtil.JsonProcessor;

public class Message<O extends Object,I extends Object>{

	private static final String FIELD_SUCCESS="success";
	private static final String FIELD_MESSAGE="message";
	private static final String FIELD_OBJECT="object";
	private static final String FIELD_OBJECT_LIST="objectList";
	private static final String FIELD_OTHER_INFORMATION="otherInformation";
	private boolean success=false;
	private String message=null;
	private O object=null;
	private List<I> objectList=null;
	private String otherInformation=null;

	public Message() {
	}

	/**
	 * simple class use
	 * @param <O>
	 * @param <I>
	 * @param json
	 * @param objectClass
	 * @param listItemClass
	 * @return Message
	 */
	public static <O extends Object,I extends Object> Message<O,I> jsonToMessage(String json,Class<O> objectClass,Class<I> listItemClass){
		return jsonToMessage(json,objectClass,listItemClass,ClassUtil.DEFAULT_CLASS_PROCESSOR);
	}

	/**
	 * for special class use
	 * @param <O>
	 * @param <I>
	 * @param json
	 * @param objectClass
	 * @param listItemClass
	 * @param classProcessor
	 * @return Message
	 */
	public static <O extends Object,I extends Object> Message<O,I> jsonToMessage(String json,Class<O> objectClass,Class<I> listItemClass,ClassProcessor classProcessor){
		Message<O,I> message=new Message<O,I>();	
		if(StringUtil.isNotBlank(json)){
			JsonObject jsonObject=new JsonObject(json);
			message.setSuccess(Boolean.parseBoolean(ClassUtil.changeType(boolean.class, new String[]{jsonObject.get(FIELD_SUCCESS).toString()}).toString()));
			message.setMessage(ClassUtil.changeType(String.class, new String[]{jsonObject.get(FIELD_MESSAGE).toString()}).toString());
			Object object=jsonObject.get(FIELD_OBJECT);
			if(object instanceof JsonObject){
				JsonObject jsonObjectValue=(JsonObject)object;
				O objectValue=JsonUtil.jsonObjectToObject(jsonObjectValue,objectClass,classProcessor);
				message.setObject(objectValue);
			}
			object=jsonObject.get(FIELD_OBJECT_LIST);
			if(object instanceof JsonArray){
				JsonArray jsonArrayValue=(JsonArray)object;
				message.setObjectList(JsonUtil.jsonArrayToList(jsonArrayValue,listItemClass,classProcessor));
			}
			message.setOtherInformation(ClassUtil.changeType(String.class, new String[]{jsonObject.get(FIELD_OTHER_INFORMATION).toString()}).toString());
		}
		return message;
	}

	/**
	 * obtain success message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainSuccessMessage(String message){
		return new Message<O,I>(true,message,null,null,null);
	}

	/**
	 * obtain success message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @param object
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainSuccessMessage(String message,O object){
		return new Message<O,I>(true,message,object,null,null);
	}

	/**
	 * obtain success message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @param objectList
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainSuccessMessage(String message,List<I> objectList){
		return new Message<O,I>(true,message,null,objectList,null);
	}

	/**
	 * obtain success message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @param object
	 * @param objectList
	 * @param otherInformation
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainSuccessMessage(String message,O object,List<I> objectList,String otherInformation){
		return new Message<O,I>(true,message,object,objectList,otherInformation);
	}

	/**
	 * obtain failure message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainFailureMessage(String message){
		return new Message<O,I>(false,message,null,null,null);
	}

	/**
	 * obtain failure message
	 * @param <O>
	 * @param <I>
	 * @param message
	 * @param object
	 * @param objectList
	 * @param otherInformation
	 * @return Message<O,I>
	 */
	public static <O extends Object,I extends Object> Message<O,I> obtainFailureMessage(String message,O object,List<I> objectList,String otherInformation){
		return new Message<O,I>(false,message,object,objectList,otherInformation);
	}

	/**
	 * constructor
	 * @param success
	 * @param message
	 * @param object
	 * @param objectList
	 */
	public Message(boolean success,String message,O object,List<I> objectList,String otherInformation){
		this.success=success;
		this.message=message;
		this.object=object;
		this.objectList=objectList;
		this.otherInformation=otherInformation;
	}

	/**
	 * to json
	 * @return String
	 */
	public String toJson(){
		return JsonUtil.objectToJson(this);
	}

	/**
	 * to json
	 * @param jsonProcessor
	 * @return String
	 */
	public String toJson(JsonProcessor jsonProcessor){
		return JsonUtil.objectToJson(this, new String[]{}, jsonProcessor);
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the object
	 */
	public O getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(O object) {
		this.object = object;
	}

	/**
	 * @return the objectList
	 */
	public List<I> getObjectList() {
		return objectList;
	}

	/**
	 * @param objectList the objectList to set
	 */
	public void setObjectList(List<I> objectList) {
		this.objectList = objectList;
	}

	/**
	 * @return the otherInformation
	 */
	public String getOtherInformation() {
		return otherInformation;
	}

	/**
	 * @param otherInformation the otherInformation to set
	 */
	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}
}
