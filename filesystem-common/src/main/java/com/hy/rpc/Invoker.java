package com.hy.rpc;

import com.hy.common.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)
 *
 * @author wyl
 * @since 2021-09-03 15:36:37
 */
public interface Invoker<T> extends Node {

    /**
     * get service interface.
     *
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     *
     * @param invocation
     * @return result
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;

}
