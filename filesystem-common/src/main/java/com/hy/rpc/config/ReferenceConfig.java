package com.hy.rpc.config;

import com.hy.common.URL;
import com.hy.common.extension.ExtensionLoader;
import com.hy.rpc.Constants;
import com.hy.rpc.Invoker;
import com.hy.rpc.Protocol;
import com.hy.rpc.ProxyFactory;
import com.hy.test.Hello1;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceConfig
 * 服务引用配置
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
    // interface proxy reference 根据接口生成的代理对象  interface->invoker->ref
    private transient volatile T ref;
    private transient volatile Invoker<?> invoker;

    // 当前服务引用是否被销毁
    private transient volatile boolean destroyed;
    // 当前服务引用是否被初始化
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

        if (url != null && url.length() > 0) { // user specified URL, could be peer-to-peer address, or register center's address.
            String[] us = Constants.SEMICOLON_SPLIT_PATTERN.split(url);
            if (us != null && us.length > 0) {
                for (String u : us) {
                    URL url = URL.valueOf(u);
                    if (url.getPath() == null || url.getPath().length() == 0) {
                        url = url.setPath(interfaceName);
                    }
                    urls.add(url);
                }
            }
        }

        if (urls.isEmpty()) {
            throw new IllegalStateException("url not allow empty!");
        }

        // 单个服务调用
        if (urls.size() == 1) {
            invoker = refprotocol.refer(interfaceClass, urls.get(0));
        } else {// 集群调用

        }

        //create service proxy
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

    public void setUrl(String url) {
        this.url = url;
    }

    public static void main(String[] args) {
        ReferenceConfig<Hello1> referenceConfig = new ReferenceConfig();
        referenceConfig.setInterface(Hello1.class);
        referenceConfig.setUrl("rmi://10.20.11.216:1099");
        Hello1 hello = referenceConfig.get();
        hello.say();//接口调用
        // ((EchoService) hello).$echo("000");//回声测试


        /*ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig();
        referenceConfig.setInterface(Hello.class);
        GenericService genericService = referenceConfig.get();
        // 基本类型以及Date,List,Map等不需要转换，直接调用
        Object result = genericService.$invoke("say", new String[]{},
                new Object[]{});
        System.out.println(result);*/
    }

}
