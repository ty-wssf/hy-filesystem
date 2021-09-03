package com.hy.rpc.support;

import com.hy.common.Constants;
import com.hy.common.URL;

/**
 * @author wyl
 * @since 2021-09-03 15:57:21
 */
public class ProtocolUtils {

    private ProtocolUtils() {
    }

    public static String serviceKey(URL url) {
        return serviceKey(url.getPort(), url.getPath(), url.getParameter(Constants.VERSION_KEY),
                url.getParameter(Constants.GROUP_KEY));
    }

    /**
     * @param port
     * @param serviceName com.hy.dubbo.UserService
     * @param serviceVersion
     * @param serviceGroup
     * @return
     */
    public static String serviceKey(int port, String serviceName, String serviceVersion, String serviceGroup) {
        StringBuilder buf = new StringBuilder();
        if (serviceGroup != null && serviceGroup.length() > 0) {
            buf.append(serviceGroup);
            buf.append("/");
        }
        buf.append(serviceName);
        if (serviceVersion != null && serviceVersion.length() > 0 && !"0.0.0".equals(serviceVersion)) {
            buf.append(":");
            buf.append(serviceVersion);
        }
        buf.append(":");
        buf.append(port);
        return buf.toString();
    }

}
