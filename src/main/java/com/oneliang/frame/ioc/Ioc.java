package com.oneliang.frame.ioc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.oneliang.util.common.StringUtil;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ioc {
	String id() default StringUtil.BLANK;
	boolean proxy() default true;
	String injectType() default IocBean.INJECT_TYPE_AUTO_BY_ID;

	@Documented
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AfterInject{
	}
}