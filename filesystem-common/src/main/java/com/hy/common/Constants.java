package com.hy.common;

import java.util.regex.Pattern;

/**
 * Constants
 */
public class Constants {

    public static final String REMOVE_VALUE_PREFIX = "-";
    public static final String DEFAULT_KEY = "default";
    public static final String HY_PROPERTIES_KEY = "hy.properties.file";
    public static final String DEFAULT_HY_PROPERTIES = "hy.properties";
    /**
     * Default timeout value in milliseconds for server shutdown
     */
    public static final int DEFAULT_SERVER_SHUTDOWN_TIMEOUT = 10000;
    public static final String SHUTDOWN_WAIT_KEY = "hy.service.shutdown.wait";
    @Deprecated
    public static final String SHUTDOWN_WAIT_SECONDS_KEY = "hy.service.shutdown.wait.seconds";
    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");

}
