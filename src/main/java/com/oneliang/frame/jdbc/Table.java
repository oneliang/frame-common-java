package com.oneliang.frame.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.oneliang.util.common.StringUtil;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	String table();
	boolean dropIfExist() default false;
	String condition() default StringUtil.BLANK;
	Column[] columns() default {};

	@Documented
	@Target(value={ElementType.FIELD,ElementType.ANNOTATION_TYPE})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Column {
		String field() default StringUtil.BLANK;
		String column();
		boolean isId() default false;
		String condition() default StringUtil.BLANK;
	}
}
