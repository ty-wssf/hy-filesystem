package com.hy.filesystem.storage.service.impl;

import com.hy.common.extension.Adaptive;
import com.hy.common.extension.ExtensionLoader;
import com.hy.filesystem.storage.service.IFileService;

/**
 * 文件服务自适应类
 *
 * @author wyl
 * @since 2021-09-03 13:22:51
 */
@Adaptive
public class AdaptiveFileService implements IFileService {

    private static volatile String DEFAULT_FILE_SERVIE;

    public static void setDefaultCompiler(String fileServiceName) {
        DEFAULT_FILE_SERVIE = fileServiceName;
    }

    @Override
    public String upload(String bucket, String fileName, byte[] data) {
        IFileService fileService = null;
        ExtensionLoader<IFileService> loader = ExtensionLoader.getExtensionLoader(IFileService.class);
        String name = DEFAULT_FILE_SERVIE; // copy reference
        if (name != null && name.length() > 0) {
            fileService = loader.getExtension(name);
        }
        if (fileService == null) {
            fileService = loader.getDefaultExtension();
        }
        return fileService.upload(bucket, fileName, data);
    }

}
