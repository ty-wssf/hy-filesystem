package com.hy.rpc;

/**
 * Exporter. (API/SPI, Prototype, ThreadSafe)
 *
 * @author wyl
 * @since 2021-09-03 15:46:29
 */
public interface Exporter<T> {

    /**
     * get invoker.
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport.
     * <p>
     * <code>
     * getInvoker().destroy();
     * </code>
     */
    void unexport();

}
