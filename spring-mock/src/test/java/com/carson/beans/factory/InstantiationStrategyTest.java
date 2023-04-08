package com.carson.beans.factory;

import com.carson.beans.factory.config.BeanDefinition;
import com.carson.beans.factory.service.HaveArgsConstructorService;
import com.carson.beans.factory.support.CglibSubclassingInstantiationStrategy;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class InstantiationStrategyTest {

    @Test
    public void TestInstantiationStrategy(){
        BeanDefinition beanDefinition = new BeanDefinition(HaveArgsConstructorService.class);

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("serviceA", beanDefinition);
        beanFactory.setInstantiationStrategy(new CglibSubclassingInstantiationStrategy());

        HaveArgsConstructorService service = (HaveArgsConstructorService) beanFactory.getBean("serviceA");
        service.sayHello();
    }
}
