package com.hy.common.extension.factory;

import com.hy.common.extension.ExtensionFactory;
import com.hy.common.utils.ConcurrentHashSet;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * SpringExtensionFactory
 *
 * @author wyl
 * @since 2021-09-02 18:06:26
 */
public class SpringExtensionFactory implements ExtensionFactory {

    private static final Set<ApplicationContext> contexts = new ConcurrentHashSet<ApplicationContext>();

    public static void addApplicationContext(ApplicationContext context) {
        contexts.add(context);
    }

    public static void removeApplicationContext(ApplicationContext context) {
        contexts.remove(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtension(Class<T> type, String name) {
        for (ApplicationContext context : contexts) {
            if (context.containsBean(name)) {
                Object bean = context.getBean(name);
                if (type.isInstance(bean)) {
                    return (T) bean;
                }
            }
        }
        return null;
    }

}
