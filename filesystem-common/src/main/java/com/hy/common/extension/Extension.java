package com.hy.common.extension;

import java.lang.annotation.*;

/**
 * @author wyl
 * @since 2021-09-02 17:55:25
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Extension {

    /**
     * @deprecated
     */
    @Deprecated
    String value() default "";

}