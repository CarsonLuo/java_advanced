package com.carson.beans.factory;

import com.carson.beans.factory.config.BeanDefinition;
import com.carson.service.HelloService;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class BeanDefinitionAndBeanDefinitionRegistryTest {

    @Test
    public void testBeanFactory() throws Exception {
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("helloService", beanDefinition);

        HelloService s = (HelloService) beanFactory.getBean("helloService");
        s.sayHello();
    }
}