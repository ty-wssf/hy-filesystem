package com.hy.filesystem.storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 通用映射配置
 *
 * @author ruoyi
 */
@Configuration
@ConfigurationProperties(prefix = "file")
public class ResourcesConfig implements WebMvcConfigurer {

    /**
     * 上传文件存储在本地的根路径
     */
    private String path;

    /**
     * 资源映射路径 前缀
     */
    private String prefix;

    /**
     * 域名或本机访问地址
     */
    private String domain;

    private boolean isRename;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler(prefix + "/**")
                .addResourceLocations("file:" + path + File.separator);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isRename() {
        return isRename;
    }

    public void setRename(boolean rename) {
        isRename = rename;
    }

}