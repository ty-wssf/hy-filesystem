package com.hy.rpc.service;

/**
 * Echo service. 回声服务接口，用于检测当前服务是否可用
 *
 * @author wyl
 * @since 2021-09-03 17:03:14
 */
public interface EchoService {

    /**
     * echo test.
     *
     * @param message message.
     * @return message.
     */
    Object $echo(Object message);

}
