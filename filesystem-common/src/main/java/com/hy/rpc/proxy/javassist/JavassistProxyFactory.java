package com.hy.rpc.proxy.javassist;

import com.hy.common.URL;
import com.hy.common.bytecode.Proxy;
import com.hy.common.bytecode.Wrapper;
import com.hy.rpc.Invoker;
import com.hy.rpc.ProxyFactory;
import com.hy.rpc.proxy.AbstractProxyInvoker;
import com.hy.rpc.proxy.InvokerInvocationHandler;

/**
 * JavaassistRpcProxyFactory
 */
public class JavassistProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker) {
        return (T) Proxy.getProxy(invoker.getInterface()).newInstance(new InvokerInvocationHandler(invoker));
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) {
        // TODO Wrapper cannot handle this scenario correctly: the classname contains '$'
        final Wrapper wrapper = Wrapper.getWrapper(proxy.getClass().getName().indexOf('$') < 0 ? proxy.getClass() : type);
        return new AbstractProxyInvoker<T>(proxy, type, url) {
            @Override
            protected Object doInvoke(T proxy, String methodName,
                                      Class<?>[] parameterTypes,
                                      Object[] arguments) throws Throwable {
                return wrapper.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

}
