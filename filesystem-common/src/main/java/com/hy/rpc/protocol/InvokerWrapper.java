package com.hy.rpc.protocol;

import com.hy.common.URL;
import com.hy.rpc.Invocation;
import com.hy.rpc.Invoker;
import com.hy.rpc.Result;
import com.hy.rpc.RpcException;

/**
 * InvokerWrapper
 * 该类是Invoker的包装类，其中用到类装饰模式，不过并没有实现实际的功能增强。
 */
public class InvokerWrapper<T> implements Invoker<T> {

    private final Invoker<T> invoker;

    private final URL url;

    public InvokerWrapper(Invoker<T> invoker, URL url) {
        this.invoker = invoker;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return invoker.getInterface();
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return invoker.isAvailable();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void destroy() {
        invoker.destroy();
    }

}
