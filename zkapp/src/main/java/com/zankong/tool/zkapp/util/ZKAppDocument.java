package com.zankong.tool.zkapp.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by YF on 2018/6/22.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface ZKAppDocument {
    String value() default "";
}
