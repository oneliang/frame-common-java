package com.oneliang.test.protocol;

import java.util.List;

public class ProtocolTemplateBean {

	private String packageName=null;
	private String className=null;
	private List<Field> fieldList=null;
	public static class Field{
		private String type=null;
		private String name=null;
		private String value=null;
		private int byteCount=0;
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		/**
		 * @return the byteCount
		 */
		public int getByteCount() {
			return byteCount;
		}
		/**
		 * @param byteCount the byteCount to set
		 */
		public void setByteCount(int byteCount) {
			this.byteCount = byteCount;
		}
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the fieldList
	 */
	public List<Field> getFieldList() {
		return fieldList;
	}
	/**
	 * @param fieldList the fieldList to set
	 */
	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}
}
