package com.hy.rpc;

import com.hy.common.extension.SPI;

/**
 * ExporterListener. (SPI, Singleton, ThreadSafe)
 * 该接口是服务暴露的监听器接口，定义了两个方法是暴露和取消暴露
 */
@SPI
public interface ExporterListener {

    /**
     * The exporter exported.
     * 暴露服务
     *
     * @param exporter
     * @throws RpcException
     * @see com.hy.rpc.Protocol#export(Invoker)
     */
    void exported(Exporter<?> exporter) throws RpcException;

    /**
     * The exporter unexported.
     * 取消暴露
     *
     * @param exporter
     * @throws RpcException
     * @see com.hy.rpc.Exporter#unexport()
     */
    void unexported(Exporter<?> exporter);

}
