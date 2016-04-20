package com.oneliang.frame.ioc.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation DataCacheUpdate for the method of the last arguments which is data cache
 * @author Dandelion
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataCacheUpdate {
	String dataCacheMethod();
}
