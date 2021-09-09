package com.hy.rpc;

import com.hy.common.Node;

/**
 * Invoker. (API/SPI, Prototype, ThreadSafe)
 * 该接口是实体域
 * 它代表了一个可执行体，可以向它发起invoke调用，
 * 这个有可能是一个本地的实现，也可能是一个远程的实现，也可能是一个集群的实现。
 * 它代表了一次调用
 * @see com.hy.rpc.Protocol#refer(Class, com.hy.common.URL)
 * @see com.hy.rpc.InvokerListener
 * @see com.hy.rpc.protocol.AbstractInvoker
 */
public interface Invoker<T> extends Node {

    /**
     * get service interface.
     * 获得服务接口
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     * 调用下一个会话域
     * @param invocation
     * @return result
     * @throws RpcException
     */
    Result invoke(Invocation invocation) throws RpcException;

}