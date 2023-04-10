package com.carson.core.io;

import cn.hutool.core.io.IoUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author carson_luo
 */
public class ResourceAndResourceLoaderTest {

    @Test
    public void TestResourceAndSResourceLoader() throws Exception{
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        // classpath 下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        Assertions.assertEquals(content, "Hello World!");

        // 加载文件系统资源
        resource = resourceLoader.getResource("src/test/resources/hello.txt");
        Assertions.assertInstanceOf(FileSystemResource.class, resource);
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        Assertions.assertEquals(content, "Hello World!");

        // 加载网络资源
        resource = resourceLoader.getResource("https://www.bing.com");
        Assertions.assertInstanceOf(UrlResource.class, resource);
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
}
