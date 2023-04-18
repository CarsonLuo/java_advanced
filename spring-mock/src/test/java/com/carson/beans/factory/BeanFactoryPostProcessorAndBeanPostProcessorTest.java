package com.carson.beans.factory;

import com.carson.beans.factory.bean.Car;
import com.carson.beans.factory.bean.Person;
import com.carson.common.CustomBeanFactoryPostProcessor;
import com.carson.common.CustomBeanPostProcessor;
import com.carson.beans.factory.support.DefaultListableBeanFactory;
import com.carson.beans.factory.toml.TomlBeanDefinitionReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author carson_luo
 */
public class BeanFactoryPostProcessorAndBeanPostProcessorTest {

    @Test
    public void TestCustomBeanFactoryPostProcessor() {
        var beanFactory = new DefaultListableBeanFactory();

        var tomlBeanDefinitionReader = new TomlBeanDefinitionReader(beanFactory);
        tomlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.toml");

        var customBeanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();
        customBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        Person person = (Person) beanFactory.getBean("person");
        Assertions.assertEquals(person.getName(), "carson");
        Assertions.assertEquals(person.getAge(), 20);
    }

    @Test
    public void TestCustomBeanPostProcessor(){
        var customBeanPostProcessor = new CustomBeanPostProcessor();

        var beanFactory = new DefaultListableBeanFactory();
        beanFactory.addBeanPostProcessor(customBeanPostProcessor);

        var tomlBeanDefinitionReader = new TomlBeanDefinitionReader(beanFactory);
        tomlBeanDefinitionReader.loadBeanDefinitions("classpath:spring.toml");

        Car car = (Car)beanFactory.getBean("car");
        Assertions.assertEquals(car.getBrand(), "benz");
        Assertions.assertEquals(car.getSpeed(), 999.99);
    }
}
