package com.carson.aop;

import com.carson.context.support.ClassPathXmlApplicationContext;
import com.carson.service.WorkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class AutoProxyTest {

    @Test
    public void TestAutoProxy(){
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-auto-proxy.xml");

        // 获取代理对象
        WorkService workService = applicationContext.getBean("workService", WorkService.class);
        workService.explode();
    }

    @Test
    public void TestPopulateProxyBeanWithPropertyValue(){
        var applicationContext = new ClassPathXmlApplicationContext("classpath:spring-populate-proxy-bean-with-property-values.xml");

        // 获取代理对象
        WorkService workService = applicationContext.getBean("workService", WorkService.class);
        workService.explode();
        Assertions.assertEquals("gg", workService.getName());
    }
}
