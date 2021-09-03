package com.hy.rpc.support;

import com.hy.common.URL;
import com.hy.rpc.*;
import com.hy.rpc.protocol.AbstractInvoker;
import com.hy.rpc.protocol.AbstractProtocol;

/**
 * AbstractProxyProtocol
 *
 * @author wyl
 * @since 2021-09-03 16:18:51
 */
public abstract class AbstractProxyProtocol extends AbstractProtocol {

    private ProxyFactory proxyFactory;

    public AbstractProxyProtocol() {
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    @Override
    public <T> Exporter<T> export(final Invoker<T> invoker) throws RpcException {
        // 获取服务key
        final String uri = serviceKey(invoker.getUrl());
        Exporter<T> exporter = (Exporter<T>) exporterMap.get(uri);
        if (exporter != null) { // 判断是否已经导出当前服务
            return exporter;
        }
        doExport(proxyFactory.getProxy(invoker), invoker.getInterface(), invoker.getUrl());
        exporterMap.put(uri, exporter);
        return exporter;
    }

    @Override
    public <T> Invoker<T> refer(final Class<T> type, final URL url) throws RpcException {
        final Invoker<T> tagert = proxyFactory.getInvoker(doRefer(type, url), type, url);
        Invoker<T> invoker = new AbstractInvoker<T>(type, url) {
            @Override
            protected Result doInvoke(Invocation invocation) throws Throwable {
                try {
                    Result result = tagert.invoke(invocation);
                    Throwable e = result.getException();
                    if (e != null) {

                    }
                    return result;
                } catch (Throwable e) {
                    throw getRpcException(type, url, invocation, e);
                }
            }
        };
        invokers.add(invoker);
        return invoker;
    }

    protected RpcException getRpcException(Class<?> type, URL url, Invocation invocation, Throwable e) {
        RpcException re = new RpcException("Failed to invoke remote service: " + type + ", method: "
                + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        return re;
    }

    // 导出具体的协议
    protected abstract <T> void doExport(T impl, Class<T> type, URL url) throws RpcException;

    // 引用具体的协议
    protected abstract <T> T doRefer(Class<T> type, URL url) throws RpcException;

}
