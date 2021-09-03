package com.hy.common;

/**
 * 节点
 *
 * @author wyl
 * @since 2021-09-03 15:35:30
 */
public interface Node {

    /**
     * get url.
     *
     * @return url.
     */
    URL getUrl();

    /**
     * is available.
     *
     * @return available.
     */
    boolean isAvailable();

    /**
     * destroy.
     */
    void destroy();

}
