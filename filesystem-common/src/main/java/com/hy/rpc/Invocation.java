package com.hy.rpc;

import java.util.Map;

/**
 * Invocation. (API, Prototype, NonThreadSafe)
 * Invocation 是会话域，它持有调用过程中的变量，比如方法名，参数等。
 *
 * @serial Don't change the class name and package name.
 * @see com.hy.rpc.Invoker#invoke(Invocation)
 * @see com.hy.rpc.RpcInvocation
 */
public interface Invocation {

    /**
     * get method name.
     * 获得方法名称
     *
     * @return method name.
     * @serial
     */
    String getMethodName();

    /**
     * get parameter types.
     * 获得参数类型
     *
     * @return parameter types.
     * @serial
     */
    Class<?>[] getParameterTypes();

    /**
     * get arguments.
     * 获得参数
     *
     * @return arguments.
     * @serial
     */
    Object[] getArguments();

    /**
     * get attachments.
     * 获得附加值集合
     *
     * @return attachments.
     * @serial
     */
    Map<String, String> getAttachments();

    /**
     * get attachment by key.
     * 获得附加值
     *
     * @return attachment value.
     * @serial
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     * 获得附加值
     *
     * @return attachment value.
     * @serial
     */
    String getAttachment(String key, String defaultValue);

    /**
     * get the invoker in current context.
     * 获得当前上下文的invoker
     *
     * @return invoker.
     * @transient
     */
    Invoker<?> getInvoker();

}
