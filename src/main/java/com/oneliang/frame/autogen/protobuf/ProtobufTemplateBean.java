package com.oneliang.frame.autogen.protobuf;

import java.util.ArrayList;
import java.util.List;

import com.oneliang.frame.autogen.JavaTemplateBean;

public class ProtobufTemplateBean extends JavaTemplateBean{

	private List<Field> fieldList=new ArrayList<Field>();
	private boolean needList=false;
	public static class Field{
		private String type=null;
		private String name=null;
		private String rawType=null;
		private String value=null;
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
		 * @return the rawType
		 */
		public String getRawType() {
			return rawType;
		}
		/**
		 * @param rawType the rawType to set
		 */
		public void setRawType(String rawType) {
			this.rawType = rawType;
		}
	}
	/**
	 * @return the fieldList
	 */
	public List<Field> getFieldList() {
		return fieldList;
	}
	/**
	 * add field
	 * @param field
	 * @return boolean
	 */
	public boolean addField(Field field) {
		return this.fieldList.add(field);
	}
	/**
	 * @return the needList
	 */
	public boolean isNeedList() {
		return needList;
	}
	/**
	 * @param needList the needList to set
	 */
	public void setNeedList(boolean needList) {
		this.needList = needList;
	}
}
