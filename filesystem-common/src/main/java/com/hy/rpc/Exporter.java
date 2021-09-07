package com.hy.rpc;

/**
 * Exporter. (API/SPI, Prototype, ThreadSafe)
 *
 * @see com.hy.rpc.Protocol#export(Invoker)
 * @see com.hy.rpc.ExporterListener
 * @see com.hy.rpc.protocol.AbstractExporter
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
