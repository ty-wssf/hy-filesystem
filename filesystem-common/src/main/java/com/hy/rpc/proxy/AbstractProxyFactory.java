package com.hy.rpc.proxy;

import com.hy.rpc.Invoker;
import com.hy.rpc.ProxyFactory;
import com.hy.rpc.RpcException;
import com.hy.rpc.service.EchoService;

/**
 * AbstractProxyFactory
 *
 * @author wyl
 * @since 2021-09-03 17:00:13
 */
public abstract class AbstractProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) throws RpcException {
        return getProxy(invoker, new Class<?>[]{invoker.getInterface(), EchoService.class});
    }

    public abstract <T> T getProxy(Invoker<T> invoker, Class<?>[] types);

}
