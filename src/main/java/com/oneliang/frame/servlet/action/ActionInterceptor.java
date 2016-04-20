package com.oneliang.frame.servlet.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.oneliang.util.common.StringUtil;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionInterceptor {
	String id() default StringUtil.BLANK;
	Mode mode() default Mode.SINGLE_ACTION;

	public static enum Mode{
		GLOBAL_ACTION_BEFORE,GLOBAL_ACTION_AFTER,SINGLE_ACTION
	}
}
