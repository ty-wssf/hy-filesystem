package com.hy.common.compiler;

import com.hy.common.extension.SPI;

/**
 * Compiler. (SPI, Singleton, ThreadSafe)
 *
 * @author wyl
 * @since 2021-09-02 16:44:22
 */
@SPI("javassist")
public interface Compiler {

    /**
     * Compile java source code.
     *
     * @param code        Java source code
     * @param classLoader classloader
     * @return Compiled class
     */
    Class<?> compile(String code, ClassLoader classLoader);

}
