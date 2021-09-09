package com.hy.test;

import com.hy.rpc.service.EchoService;

/**
 * @author wyl
 * @since 2021-09-08 09:40:09
 */
public interface Hello extends EchoService {

    String say();

}
