package com.hy.filesystem.storage.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.extra.spring.SpringUtil;
import com.hy.common.Constants;
import com.hy.filesystem.storage.config.ResourcesConfig;
import com.hy.filesystem.storage.service.IFileService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 本地上传
 *
 * @author wyl
 * @since 2021-09-03 11:44:38
 */
@Slf4j
public class LocalFileServiceImpl implements IFileService {

    private ResourcesConfig resourcesConfig = SpringUtil.getBean(ResourcesConfig.class);

    @Override
    public String upload(String bucket, String fileName, byte[] data) {
        try {
            String md5Name = MD5.create().digestHex(data);
            fileName = fileName.replace(StrUtil.split(FileUtil.getName(fileName), ".").get(0), md5Name);
            File file = new File(resourcesConfig.getPath().concat(File.separator).concat(bucket).concat(File.separator).concat(fileName));
            FileUtil.mkParentDirs(file);// 创建父文件夹

            log.info("文件保存路径：{}", file.getAbsolutePath());
            FileUtil.writeBytes(data, file);
            String url = resourcesConfig.getDomain()
                    .concat(resourcesConfig.getPrefix())
                    .concat(Constants.C_SLASH)
                    .concat(bucket)
                    .concat(fileName);
            log.info("文件访问地址：{}", url);
            return url;
        } catch (Exception e) {
            log.error("文件保存异常", e);
        }
        return null;
    }

}
