package com.hy.rpc;

import com.hy.common.extension.SPI;

/**
 * InvokerListener. (SPI, Singleton, ThreadSafe)
 */
@SPI
public interface InvokerListener {

    /**
     * The invoker referred
     *
     * @param invoker
     * @throws RpcException
     * @see com.hy.rpc.Protocol#refer(Class, com.hy.common.URL)
     */
    void referred(Invoker<?> invoker) throws RpcException;

    /**
     * The invoker destroyed.
     *
     * @param invoker
     * @see com.hy.rpc.Invoker#destroy()
     */
    void destroyed(Invoker<?> invoker);

}
