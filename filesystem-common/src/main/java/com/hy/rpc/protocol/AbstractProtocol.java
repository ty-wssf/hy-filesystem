package com.hy.rpc.protocol;

import com.hy.common.URL;
import com.hy.common.utils.ConcurrentHashSet;
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
 *
 * @author wyl
 * @since 2021-09-03 15:52:42
 */
public abstract class AbstractProtocol implements Protocol {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // 服务暴露者集合
    protected final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<String, Exporter<?>>();

    // 服务引用者集合
    //TODO SOFEREFENCE
    protected final Set<Invoker<?>> invokers = new ConcurrentHashSet<Invoker<?>>();

    /**
     * 获取服务唯一标识
     *
     * @param url
     * @return
     */
    // dubbo://10.20.11.216:20881/com.hy.dubbo.UserService?anyhost=true&application=demo-provider&bind.ip=10.20.11.216&bind.port=20881&dubbo=2.6.0&generic=false&interface=com.hy.dubbo.UserService&methods=findUsers,sayHi&pid=40716&side=provider&timestamp=1630548384064
    protected static String serviceKey(URL url) {
        // com.hy.dubbo.UserService:20881
        return ProtocolUtils.serviceKey(url);
    }

    protected static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        return ProtocolUtils.serviceKey(port, serviceName, serviceVersion, serviceGroup);
    }

    // 销毁当前协议
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
