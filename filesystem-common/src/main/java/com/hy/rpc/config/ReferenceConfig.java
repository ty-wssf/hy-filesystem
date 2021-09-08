package com.hy.rpc.config;

import com.hy.common.URL;
import com.hy.common.extension.ExtensionLoader;
import com.hy.rpc.Invoker;
import com.hy.rpc.Protocol;
import com.hy.rpc.ProxyFactory;
import com.hy.test.Hello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ReferenceConfig
 *
 * @author wyl
 * @since 2021-09-08 10:45:42
 */
public class ReferenceConfig<T> {

    private static final Protocol refprotocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    private static final ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();

    private final List<URL> urls = new ArrayList<URL>();
    // interface name
    private String interfaceName;
    private Class<?> interfaceClass;
    // url for peer-to-peer invocation
    private String url;
    // interface proxy reference
    private transient volatile T ref;
    private transient volatile Invoker<?> invoker;

    private transient volatile boolean destroyed;
    private transient volatile boolean initialized;

    public synchronized T get() {
        if (destroyed) {
            throw new IllegalStateException("Already destroyed!");
        }
        if (ref == null) {
            init();
        }
        return ref;
    }

    private void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        if (interfaceName == null || interfaceName.length() == 0) {
            throw new IllegalStateException("interface not allow null!");
        }
        URL url = new URL("rmi", "10.20.11.216", 1000, new HashMap<>());
        url = url.setPath(interfaceName);
        invoker = refprotocol.refer(interfaceClass, url);
        // create service proxy
        ref = (T) proxyFactory.getProxy(invoker);
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

    public static void main(String[] args) {
        ReferenceConfig<Hello> referenceConfig = new ReferenceConfig();
        referenceConfig.setInterface(Hello.class);
        Hello hello = referenceConfig.get();
        hello.say();
    }

}
