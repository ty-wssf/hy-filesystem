package com.hy.rpc.protocol;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.hy.common.URL;
import com.hy.rpc.Constants;
import com.hy.rpc.Exporter;
import com.hy.rpc.Invoker;
import com.hy.rpc.Protocol;
import com.hy.rpc.support.ProtocolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * abstract ProtocolSupport.
 */
public abstract class AbstractProtocol implements Protocol {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 服务暴露者集合
     */
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();

    /**
     * 服务引用者集合
     */
    //TODO SOFEREFENCE
    protected final Set<Invoker<?>> invokers = new ConcurrentHashSet<Invoker<?>>();

    protected static String serviceKey(URL url) {
        // 获得绑定的端口号
        int port = url.getParameter(Constants.BIND_PORT_KEY, url.getPort());
        return serviceKey(port, url.getPath(), url.getParameter(Constants.VERSION_KEY),
                url.getParameter(Constants.GROUP_KEY));
    }

    // 该方法是为了得到服务key group+"/"+serviceName+":"+serviceVersion+":"+port
    protected static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        return ProtocolUtils.serviceKey(port, serviceName, serviceVersion, serviceGroup);
    }

    // 该方法是对invoker和exporter的销毁。
    @Override
    public void destroy() {
        // 遍历服务引用实体
        for (Invoker<?> invoker : invokers) {
            if (invoker != null) {
                // 从集合中移除
                invokers.remove(invoker);
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Destroy reference: " + invoker.getUrl());
                    }
                    // 销毁
                    invoker.destroy();
                } catch (Throwable t) {
                    logger.warn(t.getMessage(), t);
                }
            }
        }
        // 遍历服务暴露者
        for (String key : new ArrayList<String>(exporterMap.keySet())) {
            // 从集合中移除
            Exporter<?> exporter = exporterMap.remove(key);
            if (exporter != null) {
                try {
                    if (logger.isInfoEnabled()) {
                        logger.info("Unexport service: " + exporter.getInvoker().getUrl());
                    }
                    // 取消暴露
                    exporter.unexport();
                } catch (Throwable t) {
                    logger.warn(t.getMessage(), t);
                }
            }
        }
    }
}