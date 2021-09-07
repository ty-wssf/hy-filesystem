package com.hy.rpc.protocol.rmi;

import com.hy.common.URL;
import com.hy.common.Version;
import com.hy.rpc.Constants;
import com.hy.rpc.RpcException;
import com.hy.rpc.protocol.AbstractProxyProtocol;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

/**
 * RmiProtocol.
 * 该类继承了AbstractProxyProtocol类，是rmi协议实现的核心，
 * 跟其他协议一样，也实现了自己的服务暴露和服务引用方法。
 */
public class RmiProtocol extends AbstractProxyProtocol {

    public static final int DEFAULT_PORT = 1099;

    public RmiProtocol() {
        super(RemoteAccessException.class, RemoteException.class);
    }

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    // 服务暴露的逻辑实现。
    @Override
    protected <T> Runnable doExport(final T impl, Class<T> type, URL url) throws RpcException {
        // rmi暴露者
        final RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        // 设置端口
        rmiServiceExporter.setRegistryPort(url.getPort());
        // 设置服务名称
        rmiServiceExporter.setServiceName(url.getPath());
        // 设置接口
        rmiServiceExporter.setServiceInterface(type);
        // 设置服务实现
        rmiServiceExporter.setService(impl);
        try {
            // 初始化bean的时候执行
            rmiServiceExporter.afterPropertiesSet();
        } catch (RemoteException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return new Runnable() {
            @Override
            public void run() {
                try {
                    // 销毁
                    rmiServiceExporter.destroy();
                } catch (Throwable e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        };
    }

    // 该方法是服务引用的逻辑实现。
    @Override
    @SuppressWarnings("unchecked")
    protected <T> T doRefer(final Class<T> serviceType, final URL url) throws RpcException {
        // FactoryBean对于RMI代理，支持传统的RMI服务和RMI调用者，创建RmiProxyFactoryBean对象
        final RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        // RMI needs extra parameter since it uses customized remote invocation object
        // 检测版本
        if (url.getParameter(Constants.HY_VERSION_KEY, Version.getProtocolVersion()).equals(Version.getProtocolVersion())) {
            // Check dubbo version on provider, this feature only support
            // 设置RemoteInvocationFactory以用于此访问器
            rmiProxyFactoryBean.setRemoteInvocationFactory(new RemoteInvocationFactory() {
                @Override
                public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
                    // 自定义调用工厂可以向调用添加更多上下文信息
                    return new RmiRemoteInvocation(methodInvocation);
                }
            });
        }
        // 设置此远程访问者的目标服务的URL。URL必须与特定远程处理提供程序的规则兼容。
        rmiProxyFactoryBean.setServiceUrl(url.toIdentityString());
        // 设置要访问的服务的接口。界面必须适合特定的服务和远程处理策略
        rmiProxyFactoryBean.setServiceInterface(serviceType);
        // 设置是否在找到RMI存根后缓存它
        rmiProxyFactoryBean.setCacheStub(true);
        // 设置是否在启动时查找RMI存根
        rmiProxyFactoryBean.setLookupStubOnStartup(true);
        // 设置是否在连接失败时刷新RMI存根
        rmiProxyFactoryBean.setRefreshStubOnConnectFailure(true);
        // // 初始化bean的时候执行
        rmiProxyFactoryBean.afterPropertiesSet();
        return (T) rmiProxyFactoryBean.getObject();
    }

    @Override
    protected int getErrorCode(Throwable e) {
        if (e instanceof RemoteAccessException) {
            e = e.getCause();
        }
        if (e != null && e.getCause() != null) {
            Class<?> cls = e.getCause().getClass();
            if (SocketTimeoutException.class.equals(cls)) {
                return RpcException.TIMEOUT_EXCEPTION;
            } else if (IOException.class.isAssignableFrom(cls)) {
                return RpcException.NETWORK_EXCEPTION;
            } else if (ClassNotFoundException.class.isAssignableFrom(cls)) {
                return RpcException.SERIALIZATION_EXCEPTION;
            }
        }
        return super.getErrorCode(e);
    }

}
