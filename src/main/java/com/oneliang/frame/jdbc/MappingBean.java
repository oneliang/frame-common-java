package com.oneliang.frame.jdbc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MappingBean{

	public static final String TAG_BEAN="bean";
	
	private String table=null;
	private String type=null;
	private List<MappingColumnBean> mappingColumnBeanList=new CopyOnWriteArrayList<MappingColumnBean>();
	
	/**
	 * get column
	 * @param field
	 * @return column
	 */
	public String getColumn(String field){
		String column=null;
		if(field!=null){
			for(MappingColumnBean mappingColumnBean:mappingColumnBeanList){
				String columnField=mappingColumnBean.getField();
				if(columnField!=null&&columnField.equals(field)){
					column=mappingColumnBean.getColumn();
					break;
				}
			}
		}
		return column;
	}
	
	/**
	 * get field
	 * @param column
	 * @return field
	 */
	public String getField(String column){
		String field=null;
		if(column!=null){
			for(MappingColumnBean mappingColumnBean:mappingColumnBeanList){
				String fieldColumn=mappingColumnBean.getColumn();
				if(fieldColumn!=null&&fieldColumn.equals(column)){
					field=mappingColumnBean.getField();
					break;
				}
			}
		}
		return field;
	}
	
	/**
	 * judge the field is id or not
	 * @param field
	 * @return is id
	 */
	public boolean isId(String field){
		boolean isId=false;
		if(field!=null){
			for(MappingColumnBean mappingColumnBean:mappingColumnBeanList){
				String columnField=mappingColumnBean.getField();
				if(columnField!=null&&columnField.equals(field)){
					isId=mappingColumnBean.getIsId();
					break;
				}
			}
		}
		return isId;
	}
	
	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	
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
	 * @param mappingColumnBean
	 * @return boolean
	 */
	public boolean addMappingColumnBean(MappingColumnBean mappingColumnBean){
		return this.mappingColumnBeanList.add(mappingColumnBean);
	}
	
	/**
	 * @return the mappingColumnBeanList
	 */
	public List<MappingColumnBean> getMappingColumnBeanList() {
		return mappingColumnBeanList;
	}
}
