package com.hy.common.extension;

/**
 * ExtensionFactory
 *
 * @author wyl
 * @since 2021-09-02 17:11:35
 */
@SPI
public interface ExtensionFactory {

    /**
     * Get extension.
     *
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getExtension(Class<T> type, String name);

}
