package com.hy.rpc;

import java.util.regex.Pattern;

/**
 * @author wyl
 * @since 2021-09-07 09:55:03
 */
public class Constants {

    public static final String PATH_KEY = "path";
    public static final String INTERFACE_KEY = "interface";
    public static final String GROUP_KEY = "group";
    public static final String VERSION_KEY = "version";
    public static final String TIMEOUT_KEY = "timeout";
    public static final String TOKEN_KEY = "token";
    public static final String APPLICATION_KEY = "application";
    public static final String BIND_PORT_KEY = "bind.port";
    public static final String GENERIC_SERIALIZATION_DEFAULT = "true";
    public static final String GENERIC_SERIALIZATION_NATIVE_JAVA = "nativejava";
    public static final String GENERIC_SERIALIZATION_BEAN = "bean";
    public static final String PROXY_KEY = "proxy";
    public static final String ASYNC_KEY = "async";
    public static final String ID_KEY = "id";
    public static final String AUTO_ATTACH_INVOCATIONID_KEY = "invocationid.autoattach";
    public static final String $INVOKE = "$invoke";
    public static final String RETURN_KEY = "return";
    public static final String BIND_IP_KEY = "bind.ip";
    public static final String ANYHOST_KEY = "anyhost";
    public static final String ANYHOST_VALUE = "0.0.0.0";
    public static final String SIDE_KEY = "side";
    public static final String PROVIDER_SIDE = "provider";
    public static final String CONSUMER_SIDE = "consumer";
    public static final String HY_VERSION_KEY = "hy";
    public static final String PROVIDER_PROTOCOL = "provider";
    public static final String CONSUMER_PROTOCOL = "consumer";
    public static final String PROVIDER = "provider";
    public static final String CONSUMER = "consumer";
    public static final String REGISTRY_PROTOCOL = "registry";
    public static final String SERVICE_FILTER_KEY = "service.filter";
    public static final String REFERENCE_FILTER_KEY = "reference.filter";
    public static final String EXPORTER_LISTENER_KEY = "exporter.listener";
    public static final String INVOKER_LISTENER_KEY = "invoker.listener";
    public static final String $ECHO = "$echo";
    public static final Pattern SEMICOLON_SPLIT_PATTERN = Pattern
            .compile("\\s*[;]+\\s*");

}
