package com.hy.common.utils;

/**
 * Helper Class for hold a value.
 *
 * @author wyl
 * @since 2021-09-02 17:12:22
 */
public class Holder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}
