package com.hy.common.extension.factory;

import com.hy.common.extension.ExtensionFactory;
import com.hy.common.extension.ExtensionLoader;
import com.hy.common.extension.SPI;

/**
 * SpiExtensionFactory
 *
 * @author wyl
 * @since 2021-09-02 18:04:06
 */
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (loader.getSupportedExtensions().size() > 0) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
