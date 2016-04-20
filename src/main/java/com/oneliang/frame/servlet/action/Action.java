package com.oneliang.frame.servlet.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {

	@Documented
	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface RequestMapping {
		String value();
		Interceptor[] interceptors() default {};
		Static[] statics() default {};
		String[] httpRequestMethods() default {};

		@Documented
		@Target(ElementType.ANNOTATION_TYPE)
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface Interceptor{
			String id();
			Mode mode();
			public static enum Mode{
				BEFORE,AFTER
			}
		}

		@Documented
		@Target(ElementType.ANNOTATION_TYPE)
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface Static{
			String parameters();
			String filePath();
		}

		@Documented
		@Target(ElementType.PARAMETER)
		@Retention(RetentionPolicy.RUNTIME)
		public static @interface RequestParameter {
			String value();
		}
	}
}