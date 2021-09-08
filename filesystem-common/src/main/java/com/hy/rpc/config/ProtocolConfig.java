package com.hy.rpc.config;

import java.io.Serializable;

/**
 * ProtocolConfig
 *
 * @author wyl
 * @since 2021-09-08 09:32:10
 */
public class ProtocolConfig implements Serializable {

    private static final long serialVersionUID = 6913423882496634749L;

    // protocol name
    private String name;

    // service IP address (when there are multiple network cards available)
    private String host;

    // service port
    private Integer port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
