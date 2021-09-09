package com.hy.rpc.config;

import com.hy.common.URL;
import com.hy.common.extension.ExtensionLoader;
import com.hy.rpc.Exporter;
import com.hy.rpc.Invoker;
import com.hy.rpc.Protocol;
import com.hy.rpc.ProxyFactory;
import com.hy.test.Hello;
import com.hy.test.Hello1;
import com.hy.test.HelloImpl;
import com.hy.test.HelloImpl1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * ServiceConfig
 *
 * @author wyl
 * @since 2021-09-08 09:18:15
 */
public class ServiceConfig<T> {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceConfig.class);
    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();
    private static final Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();

    // interface type
    private String interfaceName;
    private Class<?> interfaceClass;
    // reference to interface impl
    private T ref;
    // service name
    private String path;
    protected List<ProtocolConfig> protocols;

    private final List<Exporter<?>> exporters = new ArrayList<Exporter<?>>();

    // whether to export the service
    private transient volatile boolean exported; //当前服务是否已经导出
    private transient volatile boolean unexported; // 当前服务是否已经销毁

    public synchronized void export() {
        doExport();
    }

    protected synchronized void doExport() {
        if (unexported) {
            throw new IllegalStateException("Already unexported!");
        }
        if (exported) {
            return;
        }
        exported = true;
        if (interfaceName == null || interfaceName.length() == 0) {
            throw new IllegalStateException("interface not allow null!");
        }
        try {
            interfaceClass = Class.forName(interfaceName, true, Thread.currentThread()
                    .getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        if (path == null || path.length() == 0) {
            path = interfaceName;
        }

        doExportUrls();

    }

    private void doExportUrls() {
        // 服务暴露多种协议
        for (ProtocolConfig protocolConfig : protocols) {
            Integer port = protocolConfig.getPort();
            if (port == null) {
                final int defaultPort = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolConfig.getName()).getDefaultPort();
                if (port == null || port == 0) {
                    port = defaultPort;
                }
            }
            URL url = new URL(protocolConfig.getName(), protocolConfig.getHost(), port, path, new HashMap<>());

            Invoker<?> invoker = proxyFactory.getInvoker(ref, (Class) interfaceClass, url);

            Exporter<?> exporter = protocol.export(invoker);
            exporters.add(exporter);
        }
    }

    public synchronized void unexport() {
        if (!exported) {
            return;
        }
        if (unexported) {
            return;
        }
        if (!exporters.isEmpty()) {
            for (Exporter<?> exporter : exporters) {
                try {
                    exporter.unexport();
                } catch (Throwable t) {
                    logger.warn("unexpected err when unexport" + exporter, t);
                }
            }
            exporters.clear();
        }
        unexported = true;
    }

    public void setInterface(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setInterface(Class<?> interfaceClass) {
        if (interfaceClass != null && !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        this.interfaceClass = interfaceClass;
        setInterface(interfaceClass == null ? null : interfaceClass.getName());
    }

    public List<ProtocolConfig> getProtocols() {
        return protocols;
    }

    @SuppressWarnings({"unchecked"})
    public void setProtocols(List<? extends ProtocolConfig> protocols) {
        this.protocols = (List<ProtocolConfig>) protocols;
    }

    public ProtocolConfig getProtocol() {
        return protocols == null || protocols.isEmpty() ? null : protocols.get(0);
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocols = Arrays.asList(protocol);
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public static void main(String[] args) {
        ServiceConfig<Hello> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface("com.hy.test.Hello");
        serviceConfig.setRef(new HelloImpl());
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("rmi");
        protocolConfig.setHost("10.20.11.216");
        //protocolConfig.setPort(1000);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.export();


        ServiceConfig<Hello1> serviceConfig1 = new ServiceConfig<>();
        serviceConfig1.setInterface("com.hy.test.Hello1");
        serviceConfig1.setRef(new HelloImpl1());
        serviceConfig1.setProtocol(protocolConfig);
        serviceConfig1.export();
    }

}
