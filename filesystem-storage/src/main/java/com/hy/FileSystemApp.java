package com.hy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用启动类
 *
 * @author wyl
 * @since 2021-09-03 13:07:25
 */
@SpringBootApplication
public class FileSystemApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FileSystemApp.class, args);
    }

}
