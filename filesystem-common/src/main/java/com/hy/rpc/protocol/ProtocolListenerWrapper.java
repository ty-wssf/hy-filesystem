package com.hy.rpc.protocol;

import com.hy.common.URL;
import com.hy.common.extension.ExtensionLoader;
import com.hy.rpc.*;
import com.hy.rpc.listener.ListenerExporterWrapper;
import com.hy.rpc.listener.ListenerInvokerWrapper;

import java.util.Collections;

/**
 * ListenerProtocol
 */
public class ProtocolListenerWrapper implements Protocol {

    private final Protocol protocol;

    public ProtocolListenerWrapper(Protocol protocol) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null");
        }
        this.protocol = protocol;
    }

    @Override
    public int getDefaultPort() {
        return protocol.getDefaultPort();
    }

    // 该方法是在服务暴露上做了监听器功能的增强，也就是加上了监听器。
    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        // 如果是注册中心，则暴露该invoker
        if (Constants.REGISTRY_PROTOCOL.equals(invoker.getUrl().getProtocol())) {
            return protocol.export(invoker);
        }
        // 创建一个暴露者监听器包装类对象
        return new ListenerExporterWrapper<T>(protocol.export(invoker),
                Collections.unmodifiableList(ExtensionLoader.getExtensionLoader(ExporterListener.class)
                        .getActivateExtension(invoker.getUrl(), Constants.EXPORTER_LISTENER_KEY)));
    }

    // 该方法是在服务引用上做了监听器功能的增强，也就是加上了监听器。
    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        // 如果是注册中心。则直接引用服务
        if (Constants.REGISTRY_PROTOCOL.equals(url.getProtocol())) {
            return protocol.refer(type, url);
        }
        // 创建引用服务监听器包装类对象
        return new ListenerInvokerWrapper<T>(protocol.refer(type, url),
                Collections.unmodifiableList(
                        ExtensionLoader.getExtensionLoader(InvokerListener.class)
                                .getActivateExtension(url, Constants.INVOKER_LISTENER_KEY)));
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }

}
