package com.hy.common;

import java.util.regex.Pattern;

/**
 * Constants
 *
 * @author wyl
 * @since 2021-09-02 17:14:13
 */
public class Constants {

    public static final String COMMA_SEPARATOR = ",";
    public static final String GROUP_KEY = "group";
    public static final String INTERFACE_KEY = "interface";
    public static final String VERSION_KEY = "version";
    public static final String ANYHOST_VALUE = "0.0.0.0";
    public static final String LOCALHOST_KEY = "localhost";
    public static final String ANYHOST_KEY = "anyhost";
    public static final String DEFAULT_KEY_PREFIX = "default.";
    public static final String BACKUP_KEY = "backup";
    public static final String DEFAULT_KEY = "default";
    public static final String REMOVE_VALUE_PREFIX = "-";
    public static final String HY_PROPERTIES_KEY = "hy.properties.file";
    public static final String DEFAULT_HY_PROPERTIES = "hy.properties";
    public static final String C_SLASH = "/";
    public static final String PROXY_KEY = "proxy";

    public static final Pattern COMMA_SPLIT_PATTERN = Pattern
            .compile("\\s*[,]+\\s*");

}
