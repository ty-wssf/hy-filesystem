package com.hy.rpc;

import com.hy.common.extension.SPI;

/**
 * ExporterListener. (SPI, Singleton, ThreadSafe)
 */
@SPI
public interface ExporterListener {

    /**
     * The exporter exported.
     *
     * @param exporter
     * @throws RpcException
     * @see com.hy.rpc.Protocol#export(Invoker)
     */
    void exported(Exporter<?> exporter) throws RpcException;

    /**
     * The exporter unexported.
     *
     * @param exporter
     * @throws RpcException
     * @see com.hy.rpc.Exporter#unexport()
     */
    void unexported(Exporter<?> exporter);

}
