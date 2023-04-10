package com.carson;

import com.carson.beans.factory.bean.HelloService;
import com.carson.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class AwareInterfaceTest {

    @Test
    public void TestAwareInterface() {
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        Assertions.assertNotNull(helloService.getBeanFactory());
        Assertions.assertNotNull(helloService.getApplicationContext());
        applicationContext.registerShutdownHook();
    }
}
