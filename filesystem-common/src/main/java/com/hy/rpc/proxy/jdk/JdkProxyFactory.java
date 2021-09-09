package com.hy.rpc.proxy.jdk;

import com.hy.common.URL;
import com.hy.rpc.Invoker;
import com.hy.rpc.ProxyFactory;
import com.hy.rpc.proxy.AbstractProxyInvoker;
import com.hy.rpc.proxy.InvokerInvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JavaassistRpcProxyFactory
 */
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{invoker.getInterface()}, new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, parameterTypes);
                return method.invoke(proxy, arguments);
            }
        };
    }

}
