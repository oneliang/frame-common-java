package com.oneliang.frame.jdbc;

import java.lang.reflect.Field;
import java.util.List;

import com.oneliang.exception.InitializeException;
import com.oneliang.frame.AnnotationContextUtil;
import com.oneliang.frame.jdbc.Table.Column;

public class AnnotationMappingContext extends MappingContext {

	/**
	 * initialize
	 */
	public void initialize(final String parameters) {
		try{
			List<Class<?>> classList=AnnotationContextUtil.parseAnnotationContextParameter(parameters, classLoader, classesRealPath, jarClassLoader, projectRealPath, Table.class);
			if(classList!=null){
				for(Class<?> clazz:classList){
					String className=clazz.getName();
					String classSimpleName=clazz.getSimpleName();
					Table tableAnnotation=clazz.getAnnotation(Table.class);
					AnnotationMappingBean annotationMappingBean=new AnnotationMappingBean();
					annotationMappingBean.setDropIfExist(tableAnnotation.dropIfExist());
					annotationMappingBean.setTable(tableAnnotation.table());
					annotationMappingBean.setType(className);
					annotationMappingBean.setCondition(tableAnnotation.condition());
					Column[] columnAnnotations=tableAnnotation.columns();
					for(Column columnAnnotation:columnAnnotations){
						AnnotationMappingColumnBean annotationMappingColumnBean=new AnnotationMappingColumnBean();
						annotationMappingColumnBean.setField(columnAnnotation.field());
						annotationMappingColumnBean.setColumn(columnAnnotation.column());
						annotationMappingColumnBean.setIsId(columnAnnotation.isId());
						annotationMappingColumnBean.setCondition(columnAnnotation.condition());
						annotationMappingBean.addMappingColumnBean(annotationMappingColumnBean);
					}
					Field[] fields=clazz.getDeclaredFields();
					if(fields!=null){
						for(Field field:fields){
							if(field.isAnnotationPresent(Column.class)){
								Column columnAnnotation=field.getAnnotation(Column.class);
								AnnotationMappingColumnBean annotationMappingColumnBean=new AnnotationMappingColumnBean();
								annotationMappingColumnBean.setField(field.getName());
								annotationMappingColumnBean.setColumn(columnAnnotation.column());
								annotationMappingColumnBean.setIsId(columnAnnotation.isId());
								annotationMappingColumnBean.setCondition(columnAnnotation.condition());
								annotationMappingBean.addMappingColumnBean(annotationMappingColumnBean);
							}
						}
					}
					annotationMappingBean.setCreateTableSqls(SqlUtil.createTableSqls(annotationMappingBean));
					classNameMappingBeanMap.put(className, annotationMappingBean);
					simpleNameMappingBeanMap.put(classSimpleName, annotationMappingBean);
				}
			}
		}catch (Exception e) {
			throw new InitializeException(parameters,e);
		}
	}
}
