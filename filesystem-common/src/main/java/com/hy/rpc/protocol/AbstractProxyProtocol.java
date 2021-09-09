package com.hy.rpc.protocol;

import com.hy.common.URL;
import com.hy.common.utils.NetUtils;
import com.hy.rpc.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractProxyProtocol
 * 该类继承了AbstractProtocol类，其中利用了代理工厂对AbstractProtocol中的两个集合进行了填充，并且对异常做了处理。
 */
public abstract class AbstractProxyProtocol extends AbstractProtocol {

    /**
     * rpc的异常类集合
     */
    private final List<Class<?>> rpcExceptions = new CopyOnWriteArrayList<Class<?>>();

    /**
     * 代理工厂
     */
    private ProxyFactory proxyFactory;

    public AbstractProxyProtocol() {
    }

    public AbstractProxyProtocol(Class<?>... exceptions) {
        for (Class<?> exception : exceptions) {
            addRpcException(exception);
        }
    }

    public void addRpcException(Class<?> exception) {
        this.rpcExceptions.add(exception);
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 其中分为两个步骤，创建一个exporter，放入到集合汇中。在创建exporter时对unexport方法进行了重写。
     *
     * @param invoker Service invoker
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Exporter<T> export(final Invoker<T> invoker) throws RpcException {
        // 获得uri
        final String uri = serviceKey(invoker.getUrl());
        // 获得服务暴露者
        Exporter<T> exporter = (Exporter<T>) exporterMap.get(uri);
        if (exporter != null) {
            return exporter;
        }
        // 新建一个线程
        final Runnable runnable = doExport(proxyFactory.getProxy(invoker), invoker.getInterface(), invoker.getUrl());
        exporter = new AbstractExporter<T>(invoker) {
            /**
             * 取消暴露
             */
            @Override
            public void unexport() {
                super.unexport();
                exporterMap.remove(uri);
                if (runnable != null) {
                    try {
                        // 启动线程
                        runnable.run();
                    } catch (Throwable t) {
                        logger.warn(t.getMessage(), t);
                    }
                }
            }
        };
        // 加入集合
        exporterMap.put(uri, exporter);
        return exporter;
    }

    /**
     * 该方法是服务引用，先从代理工厂中获得Invoker对象target，然后创建了真实的invoker在重写方法中调用代理的方法，最后加入到集合。
     *
     * @param type Service class
     * @param url  URL address for the remote service
     * @param <T>
     * @return
     * @throws RpcException
     */
    @Override
    public <T> Invoker<T> refer(final Class<T> type, final URL url) throws RpcException {
        // 通过代理获得实体域
        final Invoker<T> target = proxyFactory.getInvoker(doRefer(type, url), type, url);
        Invoker<T> invoker = new AbstractInvoker<T>(type, url) {
            @Override
            protected Result doInvoke(Invocation invocation) throws Throwable {
                try {
                    // 获得调用结果
                    Result result = target.invoke(invocation);
                    Throwable e = result.getException();
                    // 如果抛出异常，则抛出相应异常
                    if (e != null) {
                        for (Class<?> rpcException : rpcExceptions) {
                            if (rpcException.isAssignableFrom(e.getClass())) {
                                throw getRpcException(type, url, invocation, e);
                            }
                        }
                    }
                    return result;
                } catch (RpcException e) {
                    // 抛出未知异常
                    if (e.getCode() == RpcException.UNKNOWN_EXCEPTION) {
                        e.setCode(getErrorCode(e.getCause()));
                    }
                    throw e;
                } catch (Throwable e) {
                    throw getRpcException(type, url, invocation, e);
                }
            }
        };
        // 加入集合
        invokers.add(invoker);
        return invoker;
    }

    protected RpcException getRpcException(Class<?> type, URL url, Invocation invocation, Throwable e) {
        RpcException re = new RpcException("Failed to invoke remote service: " + type + ", method: "
                + invocation.getMethodName() + ", cause: " + e.getMessage(), e);
        re.setCode(getErrorCode(e));
        return re;
    }

    protected String getAddr(URL url) {
        String bindIp = url.getParameter(Constants.BIND_IP_KEY, url.getHost());
        if (url.getParameter(Constants.ANYHOST_KEY, false)) {
            bindIp = Constants.ANYHOST_VALUE;
        }
        return NetUtils.getIpByHost(bindIp) + ":" + url.getParameter(Constants.BIND_PORT_KEY, url.getPort());
    }

    protected int getErrorCode(Throwable e) {
        return RpcException.UNKNOWN_EXCEPTION;
    }

    protected abstract <T> Runnable doExport(T impl, Class<T> type, URL url) throws RpcException;

    protected abstract <T> T doRefer(Class<T> type, URL url) throws RpcException;

}
