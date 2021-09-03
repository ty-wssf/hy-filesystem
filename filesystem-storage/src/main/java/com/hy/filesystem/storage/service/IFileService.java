package com.hy.filesystem.storage.service;

import com.hy.common.extension.SPI;

/**
 * 文件服务接口定义
 *
 * @author wyl
 * @since 2021-09-03 11:38:35
 */
@SPI("localFileServiceImpl")
public interface IFileService {

    String upload(String bucket, String fileName, byte[] data);

}
