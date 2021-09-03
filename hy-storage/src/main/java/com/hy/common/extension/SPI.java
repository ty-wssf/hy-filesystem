package com.hy.common.extension;

import java.lang.annotation.*;

/**
 * Marker for extension interface
 *
 * @author wyl
 * @since 2021-09-02 16:45:17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * default extension name
     */
    String value() default "";

}
