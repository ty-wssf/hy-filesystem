package com.hy.rpc.protocol;

import com.hy.rpc.Exporter;
import com.hy.rpc.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractExporter.
 */
public abstract class AbstractExporter<T> implements Exporter<T> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 实体域
     */
    private final Invoker<T> invoker;

    /**
     * 是否取消暴露服务
     */
    private volatile boolean unexported = false;

    public AbstractExporter(Invoker<T> invoker) {
        if (invoker == null) {
            throw new IllegalStateException("service invoker == null");
        }
        if (invoker.getInterface() == null) {
            throw new IllegalStateException("service type == null");
        }
        if (invoker.getUrl() == null) {
            throw new IllegalStateException("service url == null");
        }
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public void unexport() {
        // 如果已经消取消暴露，则之间返回
        if (unexported) {
            return;
        }
        // 设置为true
        unexported = true;
        // 销毁该实体域
        getInvoker().destroy();
    }

    @Override
    public String toString() {
        return getInvoker().toString();
    }

}
