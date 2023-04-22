package com.carson.beans.factory;

import com.carson.context.support.ClassPathXmlApplicationContext;
import com.carson.service.HelloService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class PackageScanTest {

    @Test
    public void TestPackageScan(){
        var applicationCtx = new ClassPathXmlApplicationContext("classpath:spring-package-scan.xml");
        HelloService helloService = applicationCtx.getBean("helloService", HelloService.class);
        Assertions.assertNotNull(helloService);
    }
}
