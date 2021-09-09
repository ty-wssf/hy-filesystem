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
     * 获得对应的实体域invoker
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport. 取消暴露
     * <p>
     * <code>
     * getInvoker().destroy();
     * </code>
     */
    void unexport();

}
