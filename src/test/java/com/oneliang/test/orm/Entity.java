package com.oneliang.test.orm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.oneliang.util.common.StringUtil;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {

    String table() default StringUtil.BLANK;

    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Column {
        String name() default StringUtil.BLANK;

        boolean isId() default false;

        Condition[] condition();

        @Documented
        @Target(ElementType.FIELD)
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Condition {
            String key();

            String value();
        }
    }
}
